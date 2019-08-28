package no.nav.tag.tiltaksgjennomforingprosess.journalpost.integrasjon;

import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tiltaksgjennomforingprosess.domene.Avtale;
import no.nav.tag.tiltaksgjennomforingprosess.properties.TiltakApiProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class TiltaksgjennomfoeringApiService {

    private static final String PATH = "/internal/avtaler";
    private URI uri;
    private final HttpHeaders headers = new HttpHeaders();

    public TiltaksgjennomfoeringApiService(TiltakApiProperties properties) {
        uri = UriComponentsBuilder.fromUri(properties.getUri())
                .path(PATH)
                .build()
                .toUri();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType((MediaType.APPLICATION_JSON));
    }

    public List<Avtale> finnAvtalerTilJournalfoering(){
        ResponseEntity<List<Avtale>> response;
        HttpEntity<String> entity = new HttpEntity<>(headers);
        response = new RestTemplate().exchange(uri, HttpMethod.GET, entity, new ParameterizedTypeReference<List<Avtale>>() {});
        return response.getBody();
    }

    public void settAvtalerTilJournalfoert(String stsToken, Map<UUID, String> avtalerTilJournalfoert){
        ResponseEntity<List<Avtale>> response;
        HttpEntity<String> entity = new HttpEntity<>(headers);
        new RestTemplate().put(uri, avtalerTilJournalfoert);
    }
}
