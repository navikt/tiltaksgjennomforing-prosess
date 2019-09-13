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

        final String stsToken = stsService.hentToken();
        List<Avtale> avtalerTilJournalforing = tiltaksgjennomfoeringApiService.finnAvtalerTilJournalfoering(stsToken);
        log.info("Hentet {} avtaler som skal journalføres", avtalerTilJournalforing.size());

        if(avtalerTilJournalforing.isEmpty()){
            return;
        }
        prosesserAvtaler(stsToken, avtalerTilJournalforing);
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
            log.error("FEIL Journalførte avtaler ble ikke lagret Tiltaksgjennomføring databasen! Avtaler som ble journalført (avtale-id :: journalpost-id): {}", avtalerJournalfortInfo(journalfoeringer), e);
            stopServer();
        }
        log.info("Ferdig journalført {} avtaler: {}", avtalerTilJournalforing.size(), avtalerJournalfortInfo(journalfoeringer));
    }

    private void stopServer() {
        log.info("Tar ned server - hindrer ny journalføring av de samme avtalene");
        System.exit(1);
    }

    private List<String> avtalerJournalfortInfo(Map<UUID, String> journalfoeringer){
        return journalfoeringer.keySet().stream().map(uuid -> uuid.toString() + " :: " + journalfoeringer.get(uuid)).collect(Collectors.toList());
    }
}

