package no.nav.tag.tiltaksgjennomforingprosess;

import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tiltaksgjennomforingprosess.domene.avtale.Avtale;
import no.nav.tag.tiltaksgjennomforingprosess.factory.JournalpostFactory;
import no.nav.tag.tiltaksgjennomforingprosess.integrasjon.JoarkService;
import no.nav.tag.tiltaksgjennomforingprosess.integrasjon.TiltaksgjennomfoeringApiService;
import no.nav.tag.tiltaksgjennomforingprosess.integrasjon.StsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;
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

    @Scheduled(cron = "${prosess.cron}")
    public void avtalerTilJournalfoering() {

        if(!enabled){
            log.warn("Prosessen ble skrudd av pga. en feil. Sjekk tidligere feilmelding i loggen");
            return;
        }

        final String stsToken = stsService.hentToken();
        List<Avtale> avtalerTilJournalforing = tiltaksgjennomfoeringApiService.finnAvtalerTilJournalfoering(stsToken);

        if(avtalerTilJournalforing.isEmpty()){
            return;
        }

        log.info("Hentet {} avtaler som skal journalføres", avtalerTilJournalforing.size());
        Map<UUID, String> journalfoerteAvtaler = journalfoerAvtaler(stsToken, avtalerTilJournalforing);
        registrerAvtalerSomJournalfoert(stsToken, journalfoerteAvtaler);
    }

    private Map<UUID, String> journalfoerAvtaler(String stsToken, List<Avtale> avtalerTilJournalforing) {
        return avtalerTilJournalforing
                .parallelStream()
                .map(avtale -> {
                    avtale.setJournalpostId(
                            joarkService.sendJournalpost(stsToken, journalpostFactory.konverterTilJournalpost(avtale))
                    );
                    return avtale;
                })
                .collect(Collectors.toMap(Avtale::getId, Avtale::getJournalpostId));
    }

    private void registrerAvtalerSomJournalfoert(final String stsToken, Map<UUID, String> journalfoeringer) {

        try {
            tiltaksgjennomfoeringApiService.settAvtalerTilJournalfoert(stsToken, journalfoeringer);
        } catch (Exception e) {
            log.error("FEIL Journalførte avtaler ble ikke lagret Tiltaksgjennomføring databasen! Avtaler som ble journalført (avtale-id :: journalpost-id): {}", avtalerJournalfortInfo(journalfoeringer), e);
            deaktiverJobb();
        }
        log.info("Ferdig journalført {} avtaler: {}", journalfoeringer.size(), avtalerJournalfortInfo(journalfoeringer));
    }

    private void deaktiverJobb() {
        log.info("Deaktiverer jobb - hindrer ny journalføring av de samme avtalene");
        enabled = false;
    }

    private List<String> avtalerJournalfortInfo(Map<UUID, String> journalfoeringer){
        return journalfoeringer.keySet().stream().map(uuid -> uuid.toString() + " :: " + journalfoeringer.get(uuid)).collect(Collectors.toList());
    }
}

