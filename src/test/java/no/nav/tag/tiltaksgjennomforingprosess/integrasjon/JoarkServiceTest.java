package no.nav.tag.tiltaksgjennomforingprosess.integrasjon;

import no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost.Journalpost;
import no.nav.tag.tiltaksgjennomforingprosess.properties.JournalpostProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static no.nav.tag.tiltaksgjennomforingprosess.integrasjon.JoarkService.PATH;
import static no.nav.tag.tiltaksgjennomforingprosess.integrasjon.JoarkService.QUERY_PARAM;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JoarkServiceTest {

    private final String TOKEN = "eyxXxx";
    private final URI uri = URI.create("http://localhost:8090");
    private final URI expUri = UriComponentsBuilder.fromUri(uri).path(PATH).query(QUERY_PARAM).build().toUri();

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private JoarkService joarkService = new JoarkService(new JournalpostProperties(uri));

    @Test(expected = RuntimeException.class)
    public void oppretterJournalpost_status_500() {
        when(restTemplate.postForObject(eq(expUri), any(HttpEntity.class), any())).thenThrow(RuntimeException.class);
        joarkService.sendJournalpost(TOKEN, new Journalpost());
    }
}
