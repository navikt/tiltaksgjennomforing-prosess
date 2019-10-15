package no.nav.tag.tiltaksgjennomforingprosess.integrasjon;

import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tiltaksgjennomforingprosess.properties.StsProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;

@Slf4j
@Service
public class StsService {

    private static final String LOG_STR_TOKEN = "STS token er utgått. Henter nytt token";

    private static final String PATH = "/rest/v1/sts/token";
    private static final String PARAM_GRANT_TYPE = "grant_type=client_credentials";
    private static final String PARAM_SCOPE = "scope=openid";
    private final URI uri;
    private final HttpHeaders headers = new HttpHeaders();

    @Autowired
    RestTemplate restTemplate;

    public StsService(StsProperties stsProperties) {
        uri = UriComponentsBuilder.fromUri(stsProperties.getUri())
                .path(PATH)
                .query(PARAM_GRANT_TYPE)
                .query(PARAM_SCOPE)
                .build()
                .toUri();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType((MediaType.APPLICATION_FORM_URLENCODED));
        headers.setBasicAuth(stsProperties.getBruker(), stsProperties.getPassord());
    }

    public String hentNyttStsTokenHvisUtgaatt(String stsToken) {
        if(stsTokenErUtgaatt(stsToken)){
            log.debug(LOG_STR_TOKEN);
            stsToken = hentNyttToken();
        }
        return stsToken;
    }

    private boolean stsTokenErUtgaatt(String stsToken) {
        if(stsToken == null){
            return true;
        }

        try {
            final SignedJWT jwt = SignedJWT.parse(stsToken);
            return jwt.getJWTClaimsSet().getExpirationTime().before(Date.from(LocalDateTime.now().atZone( ZoneId.systemDefault()).toInstant()));
        } catch (ParseException e) {
            log.error("Feil ved behandling av STS token ");
            throw new RuntimeException(e);
        }
    }

    private String hentNyttToken() {
        ResponseEntity<StsTokenResponse> response;
        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            response = restTemplate.exchange(uri, HttpMethod.GET, entity, StsTokenResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Kall til STS feilet: " + e.getMessage());
        }
        return response.getBody().getAccessToken();
    }
}