package no.nav.tag.tiltaksgjennomforingprosess.integrasjon;

import no.nav.tag.tiltaksgjennomforingprosess.domene.avtale.Avtale;
import no.nav.tag.tiltaksgjennomforingprosess.properties.TiltakApiProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;

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

    @Autowired
    private StsService stsService;

    static final String PATH = "/internal/avtaler";
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
        headers.setBearerAuth(stsService.hentToken());
        try {
            return restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers), new ParameterizedTypeReference<List<Avtale>>() {}).getBody();
        } catch (Exception e) {
            log.warn("Feil ved kommunikasjon mot avtale-API. Henter nytt sts-token og forsøker igjen");
            stsService.evict();
            headers.setBearerAuth(stsService.hentToken());
            return restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers), new ParameterizedTypeReference<List<Avtale>>() {}).getBody();
        }
    }

    public void settAvtalerTilJournalfoert(Map<UUID, String> avtalerTilJournalfoert){
        headers.setBearerAuth(stsService.hentToken());
        try {
            restTemplate.exchange(uri, HttpMethod.PUT, new HttpEntity<>(avtalerTilJournalfoert, headers), Void.class);
        } catch (Exception e) {
            log.warn("Feil ved kommunikasjon mot avtale-API. Henter nytt sts-token og forsøker igjen");
            stsService.evict();
            headers.setBearerAuth(stsService.hentToken());
            restTemplate.exchange(uri, HttpMethod.PUT, new HttpEntity<>(avtalerTilJournalfoert, headers), Void.class);
        }
    }
}
