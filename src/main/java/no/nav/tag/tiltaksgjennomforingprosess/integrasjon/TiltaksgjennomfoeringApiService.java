package no.nav.tag.tiltaksgjennomforingprosess.integrasjon;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tiltaksgjennomforingprosess.domene.avtale.Avtale;
import no.nav.tag.tiltaksgjennomforingprosess.properties.TiltakApiProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
public class TiltaksgjennomfoeringApiService {

    @Autowired
    ObjectMapper objectMapper;

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

    public Set<Avtale> finnAvtalerTilJournalfoering() {
        headers.setBearerAuth(stsService.hentToken());
        try {
            return restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers), new ParameterizedTypeReference<Set<Avtale>>() {
            }).getBody();
        } catch (Exception e) {
            log.warn("Feil ved kommunikasjon mot avtale-API. Henter nytt sts-token og forsøker igjen");
            stsService.evict();
            headers.setBearerAuth(stsService.hentToken());
            return restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers), new ParameterizedTypeReference<Set<Avtale>>() {
            }).getBody();
        }
    }

    public void settAvtalerTilJournalfoert(Map<UUID, String> avtalerTilJournalfoert) {
        debugApiKall(avtalerTilJournalfoert);
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

    private void debugApiKall(Map<UUID, String> avtalerTilJournalfoert) {
        if (log.isDebugEnabled()) {
            try {
                log.debug("JOURNALFØRT JSON REQ: {}", objectMapper.writeValueAsString(avtalerTilJournalfoert));
            } catch (JsonProcessingException e) {
            }
        }
    }
}
