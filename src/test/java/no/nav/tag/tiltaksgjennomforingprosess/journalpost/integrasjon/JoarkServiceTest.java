package no.nav.tag.tiltaksgjennomforingprosess.journalpost.integrasjon;

import no.nav.tag.tiltaksgjennomforingprosess.TestData;
import no.nav.tag.tiltaksgjennomforingprosess.domene.Avtale;
import no.nav.tag.tiltaksgjennomforingprosess.journalpost.factory.JournalpostFactory;
import no.nav.tag.tiltaksgjennomforingprosess.properties.JournalpostProperties;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpServerErrorException;

import java.net.URI;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;



@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest
public class JoarkServiceTest {

    private final String TOKEN = "eyxXxx";

    @MockBean
    private JournalpostFactory journalpostFactory;

    @MockBean
    private JournalpostProperties journalpostProperties;

    @InjectMocks
    private JoarkService joarkService = new JoarkService(journalpostProperties);

    @Test(expected = HttpServerErrorException.class)
    public void oppretter_journalpost_status_500() {

        Avtale avtale = TestData.opprettAvtale();


        when(journalpostProperties.getUri()).thenReturn(URI.create("http://localhost:8090"));
        when(journalpostFactory.konverterTilJournalpost(eq(avtale))).thenReturn(any());

        joarkService.opprettOgSendJournalpost(TOKEN, avtale);
    }
}
