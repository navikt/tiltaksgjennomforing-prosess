package no.nav.tag.tiltaksgjennomforingprosess.integrasjon;

import no.nav.tag.tiltaksgjennomforingprosess.properties.StsProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;

import static no.nav.tag.tiltaksgjennomforingprosess.integrasjon.StsCacheConfig.STS_CACHE;

@Service
public class StsService {

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

    @Cacheable(STS_CACHE)
    public String hentToken() {
        ResponseEntity<StsTokenResponse> response;
        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            response = restTemplate.exchange(uri, HttpMethod.GET, entity, StsTokenResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Kall til STS feilet: " + e.getMessage());
        }
        return response.getBody().getAccessToken();
    }
 
    @CacheEvict(STS_CACHE)
    public void evict() {
    }
    
}