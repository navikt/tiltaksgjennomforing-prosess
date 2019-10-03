package no.nav.tag.tiltaksgjennomforingprosess.integrasjon;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost.Journalpost;
import no.nav.tag.tiltaksgjennomforingprosess.properties.JournalpostProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;

@Slf4j
@Service
public class JoarkService {

    static final String PATH = "/rest/journalpostapi/v1/journalpost";
    static final String QUERY_PARAM = "forsoekFerdigstill=false";
    private URI uri;
    private final HttpHeaders headers = new HttpHeaders();

    @Autowired
    private RestTemplate restTemplate;

    public JoarkService(JournalpostProperties journalpostProperties) {
        uri = UriComponentsBuilder.fromUri(journalpostProperties.getUri())
                .path(PATH)
                .query(QUERY_PARAM)
                .build()
                .toUri();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType((MediaType.APPLICATION_JSON));
    }

    public String sendJournalpost(final String token, final Journalpost journalpost) {
        debugLogJournalpost(journalpost);
        headers.setBearerAuth(token);
        HttpEntity<Journalpost> entity = new HttpEntity<>(journalpost, headers);
        JoarkResponse response = null;
        try {
            log.info("Forsøker å journalføre avtale {}", journalpost.getEksternReferanseId());
            response = restTemplate.postForObject(uri, entity, JoarkResponse.class);
        } catch (Exception e) {
            log.error("Kall til Joark feilet: {}", response != null ? response.getMelding() : "", e);
            throw new RuntimeException("Kall til Joark feilet: " + e.getMessage());
        }
        log.info("Journalført avtale {}", journalpost.getEksternReferanseId());
        return response.getJournalpostId();
    }

    private void debugLogJournalpost(Journalpost journalpost) {
        if (log.isDebugEnabled()) {
            try {
                log.info("JSON REQ: {}", new ObjectMapper().writeValueAsString(journalpost));
            } catch (JsonProcessingException e) {
            }
        }
    }

}
