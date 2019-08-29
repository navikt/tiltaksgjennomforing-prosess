package no.nav.tag.tiltaksgjennomforingprosess.journalpost.jobb;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tiltaksgjennomforingprosess.domene.Avtale;
import no.nav.tag.tiltaksgjennomforingprosess.journalpost.integrasjon.JoarkService;
import no.nav.tag.tiltaksgjennomforingprosess.journalpost.integrasjon.TiltaksgjennomfoeringApiService;
import no.nav.tag.tiltaksgjennomforingprosess.sts.StsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;

import java.io.IOException;
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

    private static ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule()).disable(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS);

    @Autowired
    private JoarkService joarkService;

    @Autowired
    private StsService stsService;

//    @Scheduled(cron = "${journalpost.jobb.cron}")
    @Scheduled(fixedDelay = 5000)
    public void hentAvtalerTilJournalfoering() {

        log.debug("Ser etter avtaler til journalfoering");

        final String stsToken = "fjasogvas.fjasogvas.fjasogvas"; //stsService.hentToken();
        List<Avtale> avtalerTilJournalforing = tiltaksgjennomfoeringApiService.finnAvtalerTilJournalfoering(stsToken);
        log.info("Hentet {} avtaler som skal journalføres: {}",avtalerTilJournalforing.size(), avtalerTilJournalforing.stream().map(Avtale::getId));
        prosesserAvtaler(stsToken, avtalerTilJournalforing);
        log.info("Ferdig journalført {} avtaler", avtalerTilJournalforing.size());
    }

    private void prosesserAvtaler(String stsToken, List<Avtale> avtalerTilJournalforing) {
        Map<UUID, String> journalfoeringer = avtalerTilJournalforing
                .parallelStream()
                .map(avtale -> {
                    try {
                        log.info("Sender journalpost til Joark. AvtaleId=", avtale.getId());
                        String jornalpostId = joarkService.opprettOgSendJournalpost(stsToken, avtale);
                        avtale.setJournalpostId(jornalpostId);
                    } catch (HttpServerErrorException e) {
                        log.error("Feil ved journalføring av avtale {}: {}", avtale.getId(), e.getMessage());
                    }
                    return avtale;
                })
                .collect(Collectors.toMap(Avtale::getId, Avtale::getJournalpostId));
        log.info("Oppdaterer avtaler i Tiltaksgjennomforing-api");
        tiltaksgjennomfoeringApiService.settAvtalerTilJournalfoert(stsToken, journalfoeringer);
    }


    public void prosesserAvtale(String avtaleJson) throws IOException {

        Avtale avtale = objectMapper.readValue(avtaleJson, Avtale.class);
        try {
            final String token = stsService.hentToken();
            joarkService.opprettOgSendJournalpost(token, avtale);
        } catch (Exception e) {
            log.error("Feil ved sending av melding til Joark - avtaleId=" + avtale.getId(), e);
        }
    }
}

