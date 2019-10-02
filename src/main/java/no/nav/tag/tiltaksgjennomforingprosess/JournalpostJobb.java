package no.nav.tag.tiltaksgjennomforingprosess;

import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tiltaksgjennomforingprosess.domene.avtale.Avtale;
import no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost.Journalpost;
import no.nav.tag.tiltaksgjennomforingprosess.factory.JournalpostFactory;
import no.nav.tag.tiltaksgjennomforingprosess.integrasjon.JoarkService;
import no.nav.tag.tiltaksgjennomforingprosess.integrasjon.StsService;
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

    private static boolean enabled = true;

    @Autowired
    private TiltaksgjennomfoeringApiService tiltaksgjennomfoeringApiService;

    @Autowired
    private JournalpostFactory journalpostFactory;

    @Autowired
    private JoarkService joarkService;

    @Autowired
    private StsService stsService;

    static final String MAPPING_FEIL = "FEILET";

    @Scheduled(cron = "${prosess.cron}")
    public void avtalerTilJournalfoering() {

        if (!enabled) {
            log.warn("Prosessen ble skrudd av pga. en feil. Sjekk tidligere feilmelding i loggen");
            return;
        }

        final String stsToken = stsService.hentToken();
        List<Avtale> avtalerTilJournalforing = tiltaksgjennomfoeringApiService.finnAvtalerTilJournalfoering(stsToken);

        if (avtalerTilJournalforing.isEmpty()) {
            return;
        }

        log.info("Hentet {} avtaler som skal journalføres", avtalerTilJournalforing.size());
        Map<UUID, String> journalfoerteAvtaler = journalfoerAvtaler(stsToken, avtalerTilJournalforing);
        registrerAvtalerSomJournalfoert(stsToken, journalfoerteAvtaler);
    }

    private Map<UUID, String> journalfoerAvtaler(String stsToken, List<Avtale> avtalerTilJournalforing) {

        Map<UUID, String> journalfoerteAvtaler = new HashMap<>(avtalerTilJournalforing.size());
        avtalerTilJournalforing
                .forEach(avtale -> {
                    Optional<Journalpost> optJournalpost;
                    try {
                        optJournalpost = Optional.of(journalpostFactory.konverterTilJournalpost(avtale));
                    } catch (Throwable t) {
                        journalfoerteAvtaler.put(avtale.getId(), MAPPING_FEIL);
                        optJournalpost = Optional.empty();
                    }
                    optJournalpost.ifPresent(journalpost -> {
                        String journalpostId = joarkService.sendJournalpost(stsToken, journalpost);
                        journalfoerteAvtaler.put(avtale.getId(), journalpostId);
                    });
                });
        return journalfoerteAvtaler;
    }

    private void registrerAvtalerSomJournalfoert(final String stsToken, Map<UUID, String> journalfoeringer) {

        try {
            tiltaksgjennomfoeringApiService.settAvtalerTilJournalfoert(stsToken, journalfoeringer);
        } catch (Exception e) {
            log.error("FEIL Journalførte avtaler ble ikke lagret Tiltaksgjennomføring databasen! Avtaler som ble journalført (avtale-id :: journalpost-id): {}", avtalerJournalfortInfo(journalfoeringer), e);
            deaktiverJobb();
        }
        log.info("Ferdig journalført {} avtaler: {}", journalfoeringer.keySet().stream().filter(key -> !journalfoeringer.get(key).equals(MAPPING_FEIL)).count(), avtalerJournalfortInfo(journalfoeringer));
    }

    private void deaktiverJobb() {
        log.info("Deaktiverer jobb - hindrer ny journalføring av de samme avtalene");
        enabled = false;
    }

    private List<String> avtalerJournalfortInfo(Map<UUID, String> journalfoeringer) {
        return journalfoeringer.keySet().stream().map(uuid -> uuid.toString() + " :: " + journalfoeringer.get(uuid)).collect(Collectors.toList());
    }
}

