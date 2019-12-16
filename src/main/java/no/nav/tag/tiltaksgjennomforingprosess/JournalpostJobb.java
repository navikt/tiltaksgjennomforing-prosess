package no.nav.tag.tiltaksgjennomforingprosess;

import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tiltaksgjennomforingprosess.domene.avtale.Avtale;
import no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost.Journalpost;
import no.nav.tag.tiltaksgjennomforingprosess.factory.JournalpostFactory;
import no.nav.tag.tiltaksgjennomforingprosess.integrasjon.JoarkService;
import no.nav.tag.tiltaksgjennomforingprosess.integrasjon.TiltaksgjennomfoeringApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@EnableScheduling
public class JournalpostJobb {

    static boolean enabled = true;

    @Autowired
    private TiltaksgjennomfoeringApiService tiltaksgjennomfoeringApiService;

    @Autowired
    private JournalpostFactory journalpostFactory;

    @Autowired
    private JoarkService joarkService;

    static final String MAPPING_FEIL = "FEILET";

    @Scheduled(cron = "${prosess.cron}")
    public void avtalerTilJournalfoering() {

        if (!enabled) {
            log.warn("Prosessen ble skrudd av pga. en feil. Sjekk tidligere feilmelding i loggen");
            return;
        }

        List<Avtale> avtalerTilJournalforing = tiltaksgjennomfoeringApiService.finnAvtalerTilJournalfoering();

        if (avtalerTilJournalforing.isEmpty()) {
            return;
        }

        log.info("Hentet {} avtaler som skal journalføres", avtalerTilJournalforing.size());
        Map<UUID, String> journalfoerteAvtaler = journalfoerAvtaler(avtalerTilJournalforing);
        registrerAvtalerSomJournalfoert(journalfoerteAvtaler);
    }

    private Map<UUID, String> journalfoerAvtaler(List<Avtale> avtalerTilJournalforing) {

        Map<UUID, String> journalfoerteAvtaler = new HashMap<>(avtalerTilJournalforing.size());
        avtalerTilJournalforing
                .forEach(avtale -> {
                    Optional<Journalpost> optJournalpost = opprettJournalpost(avtale, journalfoerteAvtaler);
                    optJournalpost.ifPresent(journalpost -> journalfoer(avtale, journalpost, journalfoerteAvtaler));
                });
        return journalfoerteAvtaler;
    }

    private Optional<Journalpost> opprettJournalpost(Avtale avtale, Map<UUID, String> journalfoerteAvtaler){
        log.debug("avtaleId={}, fraDato={}, tilDato={}", avtale.getAvtaleId(), avtale.getStartDato(), avtale.getSluttDato());
        try {
            return Optional.of(journalpostFactory.konverterTilJournalpost(avtale));
        } catch (Throwable t) {
            log.error("Feil ved mapping av avtale {} versjon {} til journalpost: ", avtale.getAvtaleId(), avtale.getVersjon(), t);
            journalfoerteAvtaler.put(avtale.getAvtaleVersjonId(), MAPPING_FEIL);
            return Optional.empty();
        }
    }

    private void journalfoer(Avtale avtale, Journalpost journalpost, Map<UUID, String> journalfoerteAvtaler){
        try {
            String journalpostId = joarkService.sendJournalpost(journalpost);
            journalfoerteAvtaler.put(avtale.getAvtaleVersjonId(), journalpostId);
        } catch (Exception e) {
            log.error("Feil oppsto ved journalføring av en avtale versjon {}", journalpost.getAvtaleId(), e);
        }
    }

    private void registrerAvtalerSomJournalfoert(Map<UUID, String> journalfoeringer) {
        if(journalfoeringer.isEmpty()){
            log.warn("Ingen avtaler registert som journalført. Sjekk feil v/journalføring");
            return;
        }
        try {
            tiltaksgjennomfoeringApiService.settAvtalerTilJournalfoert(journalfoeringer);
        } catch (Exception e) {
            log.error("FEIL Journalførte avtaler ble ikke lagret Tiltaksgjennomføring databasen! Avtaler som ble journalført (avtale-id :: journalpost-id): {}", avtalerJournalfortInfo(journalfoeringer), e);
            JournalpostJobb.deaktiverJobb();
        }
        log.info("Ferdig journalført {} avtaler: {}", journalfoeringer.keySet().stream().filter(key -> !journalfoeringer.get(key).equals(MAPPING_FEIL)).count(), avtalerJournalfortInfo(journalfoeringer));
    }

    private static void deaktiverJobb() {
        log.info("Deaktiverer jobb - hindrer ny journalføring av de samme avtalene");
        enabled = false;
    }

    private List<String> avtalerJournalfortInfo(Map<UUID, String> journalfoeringer) {
        return journalfoeringer.keySet().stream().map(uuid -> uuid.toString() + " :: " + journalfoeringer.get(uuid)).collect(Collectors.toList());
    }
}

