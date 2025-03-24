package no.nav.tag.tiltaksgjennomforingprosess.controller;

import lombok.extern.slf4j.Slf4j;
import no.nav.security.token.support.core.api.Unprotected;
import no.nav.tag.tiltaksgjennomforingprosess.properties.TiltakApiProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@Unprotected
@Slf4j
public class HealthCheckController {

    private final RestTemplate restTemplate;
    private final URI uri;

    private static final String PATH = "/internal/healthcheck";
    private static final String API_FEIL = "Ikke kontakt med tiltaksgjennomforing-api";

    public HealthCheckController(TiltakApiProperties properties, RestTemplate restTemplate) {
        uri = UriComponentsBuilder.fromUri(properties.getUri())
                .path(PATH)
                .build()
                .toUri();
        this.restTemplate = restTemplate;
    }

    @GetMapping(value = PATH)
    public String healthcheck() {
        String ping;
        try {
            ping = restTemplate.getForObject(uri, String.class);
        } catch (Throwable t) {
            log.error("PING KALL TIL TILTAKSGJENNOMFORING FEILET",t);
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, API_FEIL);
        }

        if (!"ok".equals(ping)) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, API_FEIL);
        }
        return ping;

        //TODO 'pinge' sts og joark
    }
}
