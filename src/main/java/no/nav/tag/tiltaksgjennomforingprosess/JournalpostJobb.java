package no.nav.tag.tiltaksgjennomforingprosess;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.finn.unleash.Unleash;
import no.nav.tag.tiltaksgjennomforingprosess.domene.PdfGenException;
import no.nav.tag.tiltaksgjennomforingprosess.domene.avtale.Avtale;
import no.nav.tag.tiltaksgjennomforingprosess.domene.avtale.Tiltakstype;
import no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost.Journalpost;
import no.nav.tag.tiltaksgjennomforingprosess.factory.JournalpostFactory;
import no.nav.tag.tiltaksgjennomforingprosess.integrasjon.JoarkService;
import no.nav.tag.tiltaksgjennomforingprosess.integrasjon.TiltaksgjennomfoeringApiService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@EnableScheduling
@AllArgsConstructor
public class JournalpostJobb {

    static boolean enabled = true;

    private final Unleash unleash;

    private TiltaksgjennomfoeringApiService tiltaksgjennomfoeringApiService;

    private JournalpostFactory journalpostFactory;

    private JoarkService joarkService;

    static final String MAPPING_FEIL = "FEILET";

    @Scheduled(cron = "${prosess.cron}")
    public void avtalerTilJournalfoering() {

        if (!enabled) {
            log.warn("Prosessen ble skrudd av pga. en feil. Sjekk tidligere feilmelding i loggen");
            return;
        }

        Set<Avtale> avtalerTilJournalforing = tiltaksgjennomfoeringApiService.finnAvtalerTilJournalfoering();
        avtalerTilJournalforing = filtrerTiltakPaaFeatureToggles(avtalerTilJournalforing);

        if (avtalerTilJournalforing.isEmpty()) {
            return;
        }

        log.info("Hentet {} avtaleversjoner som skal journalføres", avtalerTilJournalforing.size());
        Map<UUID, String> journalfoerteAvtaler = new HashMap<>(avtalerTilJournalforing.size());
        avtalerTilJournalforing.forEach(avtale -> journalfoerteAvtaler.put(avtale.getAvtaleVersjonId(), null));

        journalfoerAvtaler(avtalerTilJournalforing, journalfoerteAvtaler);
        registrerAvtalerSomJournalfoert(journalfoerteAvtaler);
    }

    private Map<UUID, String> journalfoerAvtaler(Set<Avtale> avtalerTilJournalforing, Map<UUID, String> journalfoerteAvtaler) {
        avtalerTilJournalforing
                .forEach(avtale -> {
                    Optional<Journalpost> optJournalpost = opprettJournalpost(avtale, journalfoerteAvtaler);
                    optJournalpost.ifPresent(journalpost -> journalfoer(avtale, journalpost, journalfoerteAvtaler));
                });
        return journalfoerteAvtaler;
    }

    private Optional<Journalpost> opprettJournalpost(Avtale avtale, Map<UUID, String> journalfoerteAvtaler) {
        log.debug("avtaleVersjonId={}, tiltak={}, fraDato={}, tilDato={}", avtale.getAvtaleVersjonId(), avtale.getTiltakstype(), avtale.getStartDato(), avtale.getSluttDato());
        try {
            return Optional.of(journalpostFactory.konverterTilJournalpost(avtale));
        } catch (PdfGenException e) {
            return Optional.empty(); //retry
        } catch (Throwable t) {
            log.error("Feil ved mapping av avtaleVersjon {} til journalpost: ", avtale.getAvtaleVersjonId(), t);
            journalfoerteAvtaler.put(avtale.getAvtaleVersjonId(), MAPPING_FEIL); //Ikke retry
            return Optional.empty();
        }
    }

    private void journalfoer(Avtale avtale, Journalpost journalpost, Map<UUID, String> journalfoerteAvtaler) {
        log.info("Forsøker å journalføre versjon {} av avtale {} med versjonId {} på tiltak {}", avtale.getVersjon(), avtale.getAvtaleId(), avtale.getAvtaleVersjonId(), avtale.getTiltakstype());
        try {
            String journalpostId = joarkService.sendJournalpost(journalpost);
            journalfoerteAvtaler.put(avtale.getAvtaleVersjonId(), journalpostId);
        } catch (Exception e) {
            log.error("Feil oppsto ved journalføring av avtale {} versjon {}", journalpost.getAvtaleId(), journalpost.getAvtaleVersjon(), e);
        }
    }

    private void registrerAvtalerSomJournalfoert(Map<UUID, String> journalfoeringer) {
        try {
            tiltaksgjennomfoeringApiService.settAvtalerTilJournalfoert(journalfoeringer);
        } catch (Exception e) {
            log.error("FEIL Journalførte avtaler ble ikke lagret Tiltaksgjennomføring databasen! Avtaler som ble journalført (avtaleVersjon-id :: journalpost-id): {}", avtalerJournalfortInfo(journalfoeringer), e);
            JournalpostJobb.deaktiverJobb();
        }
        log.info("Av {} avtaler ble disse ferdig journalført: {}", journalfoeringer.size(), avtalerJournalfortInfo(journalfoeringer));
    }

    private static void deaktiverJobb() {
        log.info("Deaktiverer jobb - hindrer ny journalføring av de samme avtalene");
        enabled = false;
    }

    private List<String> avtalerJournalfortInfo(Map<UUID, String> journalfoeringer) {
        return journalfoeringer.keySet().stream().map(uuid -> uuid.toString() + " :: " + journalfoeringer.get(uuid)).collect(Collectors.toList());
    }

    private Set<Avtale> filtrerTiltakPaaFeatureToggles(Set<Avtale> avtalerTilJournalforing) {

        if (!unleash.isEnabled("tag.tiltak.prosess.mentor")) {
            if (avtalerTilJournalforing.stream().anyMatch(avtale -> avtale.getTiltakstype().equals(Tiltakstype.MENTOR))) {
                log.warn("Feature 'tag.tiltak.mentor' er ikke skrudd på. Kan ikke behandle mentor-avtaler.");
                avtalerTilJournalforing = avtalerTilJournalforing.stream().filter(avtale -> !avtale.getTiltakstype().equals(Tiltakstype.MENTOR)).collect(Collectors.toSet());
            }
        }
        return avtalerTilJournalforing;
    }
}

