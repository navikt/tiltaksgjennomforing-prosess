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
    private static final String API_FEIL = "Ikke kontakt med tiltaksgjennomfoering-api";
    private URI uri;

    public HealthCheckController(TiltakApiProperties properties){
        uri = UriComponentsBuilder.fromUri(properties.getUri())
                .path(PATH)
                .build()
                .toUri();
    }

    @GetMapping(value = PATH)
    public String healthcheck() {
        String ping = null;
        try{
            ping = restTemplate.getForObject(uri, String.class);
        } catch (Throwable t){
            return API_FEIL;
        }

        if(ping == null || !ping.equals("ok")){
            return API_FEIL;
        }
        return ping;

        //TODO 'pinge' sts og joark
    }
}
