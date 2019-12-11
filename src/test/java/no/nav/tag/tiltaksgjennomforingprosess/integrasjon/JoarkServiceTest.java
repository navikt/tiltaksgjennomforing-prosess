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

import static no.nav.tag.tiltaksgjennomforingprosess.integrasjon.JoarkService.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class JoarkServiceTest {

    private final URI uri = URI.create("http://localhost:8090");
    private final URI expUriTilArena = UriComponentsBuilder.fromUri(uri).path(PATH).query(FORSOEK_FERDIGSTILL_FALSE).build().toUri();
    private final URI expUriIkkeTilArena = UriComponentsBuilder.fromUri(uri).path(PATH).query(FORSOEK_FERDIGSTILL_TRUE).build().toUri();

    private Journalpost journalpost = new Journalpost();

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private StsService stsService;

    @InjectMocks
    private JoarkService joarkService = new JoarkService(new JournalpostProperties(uri));

    @Test
    public void kall_mot_joark_ok_skal_returnere_journalpostid_på_1ste_avtaleversjon() {
        journalpost.setBehandlesIArena(true);
        JoarkResponse joarkResponse = new JoarkResponse();
        joarkResponse.setJournalpostId("123");
        when(restTemplate.postForObject(eq(expUriTilArena), any(HttpEntity.class), any())).thenReturn(joarkResponse);
        assertThat(joarkService.sendJournalpost(journalpost), equalTo("123"));
    }

    @Test
    public void kall_mot_joark_ok_skal_returnere_journalpostid_på_nye_avtaleversjoner() {
        journalpost.setBehandlesIArena(false);
        JoarkResponse joarkResponse = new JoarkResponse();
        joarkResponse.setJournalpostId("123");
        when(restTemplate.postForObject(eq(expUriIkkeTilArena), any(HttpEntity.class), any())).thenReturn(joarkResponse);
        assertThat(joarkService.sendJournalpost(journalpost), equalTo("123"));
    }

    @Test(expected = RuntimeException.class)
    public void oppretterJournalpost_status_500() {
        journalpost.setBehandlesIArena(true);
        when(restTemplate.postForObject(eq(expUriTilArena), any(HttpEntity.class), any())).thenThrow(RuntimeException.class);
        joarkService.sendJournalpost(journalpost);
    }

    @Test
    public void feil_mot_tjeneste_skal_hente_nytt_sts_token_og_forsøke_på_nytt() {
        journalpost.setBehandlesIArena(true);
        when(restTemplate.postForObject(eq(expUriTilArena), any(HttpEntity.class), any())).thenThrow(RuntimeException.class).thenReturn(new JoarkResponse());
        joarkService.sendJournalpost(journalpost);
        verify(stsService).evict();
        verify(stsService, times(2)).hentToken();
        verify(restTemplate, times(2)).postForObject(eq(expUriTilArena), any(HttpEntity.class), any());
    }

}
