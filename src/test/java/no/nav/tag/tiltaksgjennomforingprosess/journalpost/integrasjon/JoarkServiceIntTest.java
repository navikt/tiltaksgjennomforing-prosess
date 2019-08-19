package no.nav.tag.tiltaksgjennomforingprosess.journalpost.integrasjon;

import no.nav.tag.tiltaksgjennomforingprosess.TestData;
import no.nav.tag.tiltaksgjennomforingprosess.domene.Avtale;
import no.nav.tag.tiltaksgjennomforingprosess.journalpost.factory.JournalpostFactory;
import no.nav.tag.tiltaksgjennomforingprosess.properties.JournalpostProperties;
import org.junit.Ignore;
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
public class JoarkServiceIntTest {

    private final String TOKEN = "eyxXxx";
    private JoarkService joarkService;

    @Autowired
    private JournalpostProperties journalpostProperties;

    @Autowired
    private JournalpostFactory journalpostFactory;

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

}
