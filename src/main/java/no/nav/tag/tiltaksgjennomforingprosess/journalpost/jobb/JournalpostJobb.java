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
import java.util.stream.Collector;
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

    @Scheduled(cron = "${prosess.jobb.cron}")
    public void hentAvtalerTilJournalfoering() {
        log.info("Ser etter avtaler til journalfoering");

        final String stsToken = stsService.hentToken();
        List<Avtale> avtalerTilJournalforing = tiltaksgjennomfoeringApiService.finnAvtalerTilJournalfoering(stsToken);

        log.info("Hentet {} avtaler som skal journalføres: {}",
                avtalerTilJournalforing.size(),
                avtalerTilJournalforing.stream().map(avtale -> avtale.getId().toString()).collect(Collectors.toList()));

        try {
            prosesserAvtaler(stsToken, avtalerTilJournalforing);
            log.info("Ferdig journalført {} avtaler", avtalerTilJournalforing.size());
        } catch (HttpServerErrorException e) {
            log.error("Feil ved journalføring av avtalene: {}", e.getMessage());
        }
    }

    private void prosesserAvtaler(String stsToken, List<Avtale> avtalerTilJournalforing) {
        Map<UUID, String> journalfoeringer = avtalerTilJournalforing
                .parallelStream()
                .map(avtale -> {
                    String jornalpostId = joarkService.opprettOgSendJournalpost(stsToken, avtale);
                    avtale.setJournalpostId(jornalpostId);
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

