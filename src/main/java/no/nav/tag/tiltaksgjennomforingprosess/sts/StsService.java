package no.nav.tag.tiltaksgjennomforingprosess.sts;

import no.nav.tag.tiltaksgjennomforingprosess.properties.StsProperties;
import org.springframework.beans.factory.annotation.Autowired;
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

    public String hentToken() {
        ResponseEntity<StsTokenResponse> response;
        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            response = restTemplate.exchange(uri, HttpMethod.GET, entity, StsTokenResponse.class);
        } catch (Exception e) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Kall til STS feilet: " + e.getMessage());
        }
        return response.getBody().getAccessToken();
//        return "eyJraWQiOiJsb2NhbGhvc3Qtc2lnbmVyIiwidHlwIjoiSldUIiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiJzcnZ0aWx0YWsiLCJhdWQiOiJhdWQtbmFpcyIsImFjciI6IkxldmVsNCIsInZlciI6IjEuMCIsIm5iZiI6MTU2NzExMTU0MywiYXV0aF90aW1lIjoxNTY3MTExNTQzLCJpc3MiOiJuYWlzIiwiZXhwIjoyMzQ0NzExNTQzLCJub25jZSI6Im15Tm9uY2UiLCJpYXQiOjE1NjcxMTE1NDMsImp0aSI6IjkwNzA0NmYyLTFiNzgtNDUwMi1iNzEzLTE5M2I1MWE5N2Q2NSJ9.eV-q5i-PtVizwuAhwgy-ZClHA9IGDfucN7FXqgZCUnOkLK7gPMDGLflb0mDCLSp6SRVhB4OAxpu8LPxY6-uHGgLha-ES_SDaX-tEMCJ8xDz6mUmlZge1GEaqwdQ85E4kCmP0itCNs8eiC3dbMVclXUTF-PSJGOIxKyY6YpUAOU23PP6W29LDL1ovPG9WS4VWmXLIkBxgOK_qccptiIDtWhVDIHUHp_2DqcJZ8alUy2BWbuvv8RQfTV9rp1gsBGf82IN4bruFzERpAiXha0AkCDHL4IkUTcES9MeBUwFz9w_6SXJQLG3LZWaglxCQbtPY4sjR5gTaC_7p8THrbTgqPg";
    }
}