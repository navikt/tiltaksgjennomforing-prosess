package no.nav.tag.tiltaksgjennomforingprosess.sts;

import no.nav.tag.tiltaksgjennomforingprosess.journalpost.integrasjon.JournalpostResponse;
import no.nav.tag.tiltaksgjennomforingprosess.journalpost.request.Journalpost;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;

@Service
public class StsService {

    private static final String PATH = "/rest/v1/sts/token";
    private static final String PARAM_GRANT_TYPE = "grant_type=client_credentials";
    private static final String PARAM_SCOPE = "scope=openid";
    private URI uri;
    private final HttpHeaders headers = new HttpHeaders();

    public StsService(StsProperties stsProperties) {
        uri = UriComponentsBuilder.fromUri(stsProperties.getUri())
                .path(PATH)
                .query(PARAM_GRANT_TYPE)
                .query(PARAM_SCOPE)
                .build()
                .toUri();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType((MediaType.APPLICATION_JSON));
        headers.setBasicAuth(stsProperties.getBruker(), stsProperties.getPassord());
    }

    public String hentToken() {
        ResponseEntity<StsTokenResponse> response;
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        try {
            response = new RestTemplate().exchange(uri, HttpMethod.GET, entity, StsTokenResponse.class);
        } catch (Exception e) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Kall til STS feilet: " + e.getMessage());
        }
        return response.getBody().getAccessToken();
    }
}