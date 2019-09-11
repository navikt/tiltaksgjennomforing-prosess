package no.nav.tag.tiltaksgjennomforingprosess.journalpost.jobb;

import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tiltaksgjennomforingprosess.domene.Avtale;
import no.nav.tag.tiltaksgjennomforingprosess.journalpost.factory.JournalpostFactory;
import no.nav.tag.tiltaksgjennomforingprosess.journalpost.integrasjon.JoarkService;
import no.nav.tag.tiltaksgjennomforingprosess.journalpost.integrasjon.TiltaksgjennomfoeringApiService;
import no.nav.tag.tiltaksgjennomforingprosess.sts.StsService;
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

    @Autowired
    private TiltaksgjennomfoeringApiService tiltaksgjennomfoeringApiService;

    @Autowired
    private JournalpostFactory journalpostFactory;

    @Autowired
    private JoarkService joarkService;

    @Autowired
    private StsService stsService;

    @Scheduled(cron = "${prosess.jobb.cron}")
    public void JournalfoerAvtaler() {
        log.info("Ser etter avtaler til journalfoering");

        final String stsToken = stsService.hentToken();
        List<Avtale> avtalerTilJournalforing = tiltaksgjennomfoeringApiService.finnAvtalerTilJournalfoering(stsToken);

        if(avtalerTilJournalforing.isEmpty()){
            return;
        }

        log.info("Hentet {} avtaler som skal journalføres: {}",
                avtalerTilJournalforing.size(),
                avtalerTilJournalforing.stream().map(avtale -> avtale.getId().toString()).collect(Collectors.toList()));

        prosesserAvtaler(stsToken, avtalerTilJournalforing);
        log.info("Ferdig journalført {} avtaler", avtalerTilJournalforing.size());
    }

    private void prosesserAvtaler(String stsToken, List<Avtale> avtalerTilJournalforing) {
        Map<UUID, String> journalfoeringer = avtalerTilJournalforing
                .parallelStream()
                .map(avtale -> {
                    avtale.setJournalpostId(
                            joarkService.sendJournalpost(stsToken, journalpostFactory.konverterTilJournalpost(avtale))
                    );
                    return avtale;
                })
                .collect(Collectors.toMap(Avtale::getId, Avtale::getJournalpostId));

        log.info("Oppdaterer avtaler i Tiltaksgjennomforing-api");
        try {
            tiltaksgjennomfoeringApiService.settAvtalerTilJournalfoert(stsToken, journalfoeringer);
        } catch (Exception e) {
            List<String> journalfoerteStr = journalfoeringer.keySet().stream().map(uuid -> uuid.toString() + " :: " + journalfoeringer.get(uuid)).collect(Collectors.toList());
            log.error("FEIL Journalførte avtaler ble ikke lagret Tiltaksgjennomføring databasen! Avtaler som ble journalført (avtale-id :: journalpost-id): {}", journalfoerteStr, e);
            stopServer();
        }
    }

    private void stopServer() {
        log.info("Tar ned server - hindrer ny journalføring av de samme avtalene");
        System.exit(1);
    }
}

