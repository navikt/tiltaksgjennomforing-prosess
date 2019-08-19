package no.nav.tag.tiltaksgjennomforingprosess.journalpost.jobb;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tiltaksgjennomforingprosess.domene.Avtale;
import no.nav.tag.tiltaksgjennomforingprosess.journalpost.integrasjon.JoarkService;
import no.nav.tag.tiltaksgjennomforingprosess.sts.StsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class JournalpostJobb {

    private static ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule()).disable(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS);

    @Autowired
    private JoarkService joarkService;

    @Autowired
    private StsService stsService;

    public void prosesserAvtale(String avtaleJson) throws IOException {
        Avtale avtale = objectMapper.readValue(avtaleJson, Avtale.class);
        String token = stsService.hentToken();
        joarkService.opprettOgSendJournalpost(token, avtale);
    }
}


