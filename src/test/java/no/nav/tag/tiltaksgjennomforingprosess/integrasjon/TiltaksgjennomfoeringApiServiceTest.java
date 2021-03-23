package no.nav.tag.tiltaksgjennomforingprosess.integrasjon;

import no.nav.tag.tiltaksgjennomforingprosess.domene.avtale.Avtale;
import no.nav.tag.tiltaksgjennomforingprosess.properties.TiltakApiProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;
import java.util.Set;

import static no.nav.tag.tiltaksgjennomforingprosess.integrasjon.TiltaksgjennomfoeringApiService.PATH;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TiltaksgjennomfoeringApiServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private StsService stsService;

    private final URI uri = URI.create("http://localhost:8090");
    private final URI expUri = UriComponentsBuilder.fromUri(uri).path(PATH).build().toUri();

    @InjectMocks
    private TiltaksgjennomfoeringApiService tiltaksgjennomfoeringApiService = new TiltaksgjennomfoeringApiService(new TiltakApiProperties(uri));


    @Test
    public void kall_mot_finn_avtaler_ok_skal_returnere_avtaler() {
        Set<Avtale> avtaler = Set.of(new Avtale());
        when(restTemplate.exchange(eq(expUri), eq(HttpMethod.GET), any(HttpEntity.class), any(ParameterizedTypeReference.class))).thenReturn(ResponseEntity.of(Optional.of(avtaler)));
        assertThat(tiltaksgjennomfoeringApiService.finnAvtalerTilJournalfoering(), equalTo(avtaler));
    }
    
    @Test
    public void kall_mot_finn_avtaler_feiler_skal_hente_nytt_sts_token_og_forsøke_på_nytt() {
        Set<Avtale> avtaler = Set.of(new Avtale());
        when(restTemplate.exchange(eq(expUri), eq(HttpMethod.GET), any(HttpEntity.class), any(ParameterizedTypeReference.class)))
                .thenThrow(RuntimeException.class)
                .thenReturn(ResponseEntity.of(Optional.of(avtaler)));
        assertThat(tiltaksgjennomfoeringApiService.finnAvtalerTilJournalfoering(), equalTo(avtaler));
        verify(stsService).evict();
        verify(stsService, times(2)).hentToken();
        verify(restTemplate, times(2)).exchange(eq(expUri), eq(HttpMethod.GET), any(HttpEntity.class), any(ParameterizedTypeReference.class));
    }

    @Test(expected = RuntimeException.class)
    public void kall_mot_finn_avtaler_feiler_skal_gi_exception() {
        when(restTemplate.exchange(eq(expUri), eq(HttpMethod.GET), any(HttpEntity.class), any(ParameterizedTypeReference.class))).thenThrow(RuntimeException.class);
        tiltaksgjennomfoeringApiService.finnAvtalerTilJournalfoering();
    }

}
