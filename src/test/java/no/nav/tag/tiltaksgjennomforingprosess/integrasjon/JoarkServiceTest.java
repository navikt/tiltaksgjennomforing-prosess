package no.nav.tag.tiltaksgjennomforingprosess.integrasjon;

import no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost.Journalpost;
import no.nav.tag.tiltaksgjennomforingprosess.properties.JournalpostProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

//@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
public class JoarkServiceTest {

    private final URI uri = URI.create("http://localhost:8090");
    private final URI expUriTilArena = UriComponentsBuilder.fromUri(uri).path("/rest/journalpostapi/v1/journalpost").query("forsoekFerdigstill=false").build().toUri();
    private final URI expUriIkkeTilArena = UriComponentsBuilder.fromUri(uri).path("/rest/journalpostapi/v1/journalpost").query("forsoekFerdigstill=true").build().toUri();

    private Journalpost journalpost = new Journalpost();

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private StsService stsService;

    @Spy
    private JournalpostProperties journalpostProperties = new JournalpostProperties(uri);

    @InjectMocks
    private JoarkService joarkService;

    @Test
    public void kall_mot_joark_ok_skal_returnere_journalpostid_på_1ste_avtaleversjon() {
        journalpost.setAvtaleVersjon(1);
        JoarkResponse joarkResponse = new JoarkResponse();
        joarkResponse.setJournalpostId("123");
        when(restTemplate.postForObject(eq(expUriTilArena), any(HttpEntity.class), any())).thenReturn(joarkResponse);
        assertThat(joarkService.sendJournalpost(journalpost, false)).isEqualTo("123");
    }

    @Test
    public void kall_mot_joark_ok_skal_returnere_journalpostid_på_nye_avtaleversjoner() {
        journalpost.setAvtaleVersjon(2);
        JoarkResponse joarkResponse = new JoarkResponse();
        joarkResponse.setJournalpostId("123");
        when(restTemplate.postForObject(eq(expUriIkkeTilArena), any(HttpEntity.class), any())).thenReturn(joarkResponse);
        assertThat(joarkService.sendJournalpost(journalpost, true)).isEqualTo("123");
    }

    @Test
    public void oppretterJournalpost_status_500() {
        assertThrows(RuntimeException.class, () -> {
            journalpost.setAvtaleVersjon(1);
            when(restTemplate.postForObject(eq(expUriTilArena), any(HttpEntity.class), any())).thenThrow(RuntimeException.class);
            joarkService.sendJournalpost(journalpost, false);

        });
    }

    @Test
    public void feil_mot_tjeneste_skal_hente_nytt_sts_token_og_forsøke_på_nytt() {
        journalpost.setAvtaleVersjon(1);
        when(restTemplate.postForObject(eq(expUriTilArena), any(HttpEntity.class), any())).thenThrow(RuntimeException.class).thenReturn(new JoarkResponse());
        joarkService.sendJournalpost(journalpost, false);
        verify(stsService).evict();
        verify(stsService, times(2)).hentToken();
        verify(restTemplate, times(2)).postForObject(eq(expUriTilArena), any(HttpEntity.class), any());
    }

}
