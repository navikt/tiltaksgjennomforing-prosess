package no.nav.tag.tiltaksgjennomforingprosess.journalpost.integrasjon;

import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tiltaksgjennomforingprosess.domene.Avtale;
import no.nav.tag.tiltaksgjennomforingprosess.properties.TiltakApiProperties;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private RestTemplate restTemplate;

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

    public List<Avtale> finnAvtalerTilJournalfoering(String stsToken){
        headers.setBearerAuth(stsToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<List<Avtale>> response = restTemplate.exchange(uri, HttpMethod.GET, entity, new ParameterizedTypeReference<List<Avtale>>() {});
        return response.getBody();
    }

    public void settAvtalerTilJournalfoert(String stsToken, Map<UUID, String> avtalerTilJournalfoert){
        ResponseEntity response;
        headers.setBearerAuth(stsToken);
        HttpEntity<Map<UUID, String>> entity = new HttpEntity<>(avtalerTilJournalfoert, headers);
        restTemplate.exchange(uri, HttpMethod.PUT, entity, Void.class);
    }
}
