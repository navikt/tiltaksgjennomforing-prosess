package no.nav.tag.tiltaksgjennomforingprosess.journalpost.integrasjon;

import no.nav.tag.tiltaksgjennomforingprosess.TestData;
import no.nav.tag.tiltaksgjennomforingprosess.domene.Avtale;
import no.nav.tag.tiltaksgjennomforingprosess.journalpost.factory.JournalpostFactory;
import no.nav.tag.tiltaksgjennomforingprosess.journalpost.request.Bruker;
import no.nav.tag.tiltaksgjennomforingprosess.journalpost.request.Journalpost;
import no.nav.tag.tiltaksgjennomforingprosess.properties.JournalpostProperties;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
@DirtiesContext
@Ignore
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
        Journalpost journalpost = new Journalpost();
        String jounalpostId = joarkService.sendJournalpost(TOKEN, journalpost);
        assertEquals("001", jounalpostId);
    }

}
