package no.nav.tag.tiltaksgjennomforingprosess.integrasjon;

import no.nav.tag.tiltaksgjennomforingprosess.properties.StsProperties;
import no.nav.tag.tiltaksgjennomforingprosess.token.JwtTokenGenerator;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StsServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private StsService stsService = new StsService(stsProperties);

    private static StsProperties stsProperties;
    private StsTokenResponse tokenResponse;
    private ResponseEntity responseEntity;

    @BeforeClass
    public static void setUpBeforeClass(){
        stsProperties = new StsProperties();
        stsProperties.setUri(URI.create("urlen"));
        stsProperties.setBruker("bruker");
        stsProperties.setPassord("passord");
    }

    @Before
    public void setUp(){
        tokenResponse = new StsTokenResponse();
        tokenResponse.setAccessToken("");
        responseEntity = new ResponseEntity<StsTokenResponse>(tokenResponse, HttpStatus.OK);
    }

    @Test
    public void henterIkkeNyttToken(){
        String token = JwtTokenGenerator.signedJWTAsString();
        stsService.hentNyttStsTokenHvisUtgaatt(token);
        verify(restTemplate, never()).exchange(any(URI.class), any(HttpMethod.class), any(HttpEntity.class), eq(StsTokenResponse.class));
    }

    @Test
    public void henterNyttTokenVedOppstart(){
        String token = null;

        when(restTemplate.exchange(any(URI.class), any(HttpMethod.class), any(HttpEntity.class), eq(StsTokenResponse.class)))
                .thenReturn(responseEntity);

        stsService.hentNyttStsTokenHvisUtgaatt(token);
        verify(restTemplate, times(1)).exchange(any(URI.class), any(HttpMethod.class), any(HttpEntity.class), eq(StsTokenResponse.class));
    }

    @Test
    public void henterNyttToken() throws InterruptedException {
        String token = JwtTokenGenerator.signedJWTAsString(1L);

        when(restTemplate.exchange(any(URI.class), any(HttpMethod.class), any(HttpEntity.class), eq(StsTokenResponse.class)))
                .thenReturn(responseEntity);

        stsService.hentNyttStsTokenHvisUtgaatt(token);
        verify(restTemplate, times(1)).exchange(any(URI.class), any(HttpMethod.class), any(HttpEntity.class), eq(StsTokenResponse.class));
    }

}
