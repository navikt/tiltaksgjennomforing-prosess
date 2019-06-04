package no.nav.tag.tiltaksgjennomforingprosess.integrasjon.journalpost;

import no.nav.tag.tiltaksgjennomforingprosess.TestData;
import no.nav.tag.tiltaksgjennomforingprosess.domene.Avtale;
import no.nav.tag.tiltaksgjennomforingprosess.journalpost.integrasjon.JournalpostProperties;
import no.nav.tag.tiltaksgjennomforingprosess.journalpost.integrasjon.JoarkService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpServerErrorException;

import java.net.URI;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
@DirtiesContext
public class JoarkServiceTest {

    private final String TOKEN = "eyxXxx";
    private JoarkService joarkService;

    @Autowired
    JournalpostProperties journalpostProperties;

    @Autowired
    public void setJoarkService(JoarkService joarkService){
        this.joarkService = joarkService;
    }

    @Test
    public void oppretter_journalpost() {
        Avtale avtale = TestData.opprettAvtale();
        String jounalpostId = joarkService.opprettOgSendJournalpost(TOKEN, avtale);
        assertEquals("001", jounalpostId);
    }

    @Test(expected = HttpServerErrorException.class)
    public void oppretter_journalpost_status_500() {
        Avtale avtale = TestData.opprettAvtale();
        journalpostProperties.setUri(URI.create("http://localhost:8091"));
        joarkService = new JoarkService(journalpostProperties);
        joarkService.opprettOgSendJournalpost(TOKEN, avtale);
    }

}
