package no.nav.tag.tiltaksgjennomforingprosess.integrasjon;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost.Journalpost;
import no.nav.tag.tiltaksgjennomforingprosess.factory.RequestResponseLoggingInterceptor;
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
import java.util.Collections;

@Slf4j
@Service
public class JoarkService {

    static final String PATH = "/rest/journalpostapi/v1/journalpost";
    static final String FORSOEK_FERDIGSTILL_FALSE = "forsoekFerdigstill=false";
    static final String FORSOEK_FERDIGSTILL_TRUE = "forsoekFerdigstill=true";
    private final URI uri;
    private final URI uriArena;
    private final HttpHeaders headers = new HttpHeaders();

    private final RestTemplate restTemplate;
    private final StsService stsService;

    public JoarkService(JournalpostProperties journalpostProperties, RestTemplate restTemplate, StsService stsService) {
        uri = UriComponentsBuilder.fromUri(journalpostProperties.getUri()).path(PATH).query(FORSOEK_FERDIGSTILL_TRUE).build().toUri();
        uriArena = UriComponentsBuilder.fromUri(journalpostProperties.getUri()).path(PATH).query(FORSOEK_FERDIGSTILL_FALSE).build().toUri();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        this.restTemplate = restTemplate;
        this.stsService = stsService;
    }

    public String sendJournalpost(final Journalpost journalpost) {
        debugLogJournalpost(journalpost);
        JoarkResponse response = null;
        try {
            response = restTemplate.postForObject(uri(journalpost), entityMedStsToken(journalpost), JoarkResponse.class);
        } catch (Exception e1) {
            stsService.evict();
            log.warn("Feil ved kommunikasjon mot journalpost-API. Henter nytt sts-token og forsøker igjen");
            try {
                response = restTemplate.postForObject(uri(journalpost), entityMedStsToken(journalpost), JoarkResponse.class);
            } catch (Exception e2) {
                log.error("Kall til Joark feilet: {}", response != null ? response.getMelding() : "", e2);
                throw new RuntimeException("Kall til Joark feilet: " + e2);
            }
        }
        log.info("Journalført versjon {} av avtale {}", journalpost.getAvtaleVersjon(), journalpost.getAvtaleId());
        return response.getJournalpostId();
    }

    private URI uri(Journalpost journalpost) {
        if (journalpost.skalBehandlesIArena()) {
            return uriArena;
        }
        return uri;
    }

    private HttpEntity<Journalpost> entityMedStsToken(final Journalpost journalpost) {
        headers.setBearerAuth(stsService.hentToken());
        HttpEntity<Journalpost> entity = new HttpEntity<>(journalpost, headers);
        return entity;
    }

    private void debugLogJournalpost(Journalpost journalpost) {
        if (log.isDebugEnabled()) {
            try {
                log.debug("JSON REQ: {}", new ObjectMapper().writeValueAsString(journalpost));
            } catch (JsonProcessingException e) {
            }
        }
    }

}
