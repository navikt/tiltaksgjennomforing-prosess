package no.nav.tag.tiltaksgjennomforingprosess.controller;

import no.nav.tag.tiltaksgjennomforingprosess.properties.TiltakApiProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
public class HealthCheckController {

    @Autowired
    private RestTemplate restTemplate;

    private static final String PATH = "/internal/healthcheck";
    private URI uri;

    public HealthCheckController(TiltakApiProperties properties){
        uri = UriComponentsBuilder.fromUri(properties.getUri())
                .path(PATH)
                .build()
                .toUri();
    }

    @GetMapping(value = PATH)
    public String healthcheck() {
        String ping = restTemplate.getForObject(uri, String.class);

        if(!ping.equals("ok")){
            return "Ikke kontakt med tiltaksgjennomfoering-api";
        }
        return "ok";

        //TODO 'pinge' sts og joark
    }
}
