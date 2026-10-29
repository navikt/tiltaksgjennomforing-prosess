package no.nav.tag.tiltaksgjennomforingprosess.integrasjon;

import lombok.extern.slf4j.Slf4j;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.json.JsonMapper;
import no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost.Journalpost;
import no.nav.tag.tiltaksgjennomforingprosess.properties.JournalpostProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;

@Slf4j
@Service
public class JoarkService {

    private static final JsonMapper JSON_MAPPER = JsonMapper.builder()
            .enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .build();

    private final HttpHeaders headers = new HttpHeaders();
    private final RestTemplate restTemplate;
    private final StsService stsService;
    private final JournalpostProperties journalpostProperties;

    public JoarkService(JournalpostProperties journalpostProperties, RestTemplate restTemplate, StsService stsService) {
        this.journalpostProperties = journalpostProperties;
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        this.restTemplate = restTemplate;
        this.stsService = stsService;
    }

    public String sendJournalpost(final Journalpost journalpost, boolean ferdigstill) {
        debugLogJournalpost(journalpost);
        JoarkResponse response = null;
        try {
            log.info("Forsøker journalføring av type: {}, på sak: {}, med uri: {}", journalpost.getTittel(), journalpost.getSak() , uri(ferdigstill));
            response = restTemplate.postForObject(uri(ferdigstill), entityMedStsToken(journalpost), JoarkResponse.class);
        } catch (Exception e1) {
            stsService.evict();
            log.warn("Feil ved kommunikasjon mot journalpost-API. Henter nytt sts-token og forsøker igjen");
            try {
                response = restTemplate.postForObject(uri(ferdigstill), entityMedStsToken(journalpost), JoarkResponse.class);
            } catch (HttpClientErrorException clientErrorException) {
                // Om det er 409 duplicate, returnere journalpost-id som kommer i responsen fra Joark.
                if(clientErrorException.getStatusCode().equals(HttpStatus.CONFLICT)) {
                    if(isNotBlank(clientErrorException.getResponseBodyAsString())) {
                        try {
                            JoarkResponse joarkResponse = JSON_MAPPER.readValue(clientErrorException.getResponseBodyAsString(), JoarkResponse.class);
                            log.warn("Konflikt i Joark, journalført versjon {} av avtale {}", journalpost.getAvtaleVersjon(), journalpost.getAvtaleId());
                            return joarkResponse.getJournalpostId();
                        } catch (JacksonException e) {
                            log.error("Kall til Joark feilet", clientErrorException);
                            throw new RuntimeException("Kall til Joark feilet: ", clientErrorException);
                        }
                    }
                }
                log.error("Kall til Joark feilet: {}", response != null ? response.getMelding() : "", clientErrorException);
                throw new RuntimeException("Kall til Joark feilet: " + clientErrorException.getMessage());
            } catch (Exception e2) {
                log.error("Kall til Joark feilet: {}", response != null ? response.getMelding() : "", e2);
                throw new RuntimeException("Kall til Joark feilet: " + e2);
            }
        }
        log.info("Journalført versjon {} av avtale {}", journalpost.getAvtaleVersjon(), journalpost.getAvtaleId());
        return response.getJournalpostId();
    }

    private URI uri(boolean ferdigstill) {
        UriComponentsBuilder uri = UriComponentsBuilder.fromUri(journalpostProperties.getUri()).path("/rest/journalpostapi/v1/journalpost");
        if (ferdigstill) {
            return uri.query("forsoekFerdigstill=true").build().toUri();
        } else {
            return uri.query("forsoekFerdigstill=false").build().toUri();
        }
    }

    private HttpEntity<Journalpost> entityMedStsToken(final Journalpost journalpost) {
        headers.setBearerAuth(stsService.hentToken());
        HttpEntity<Journalpost> entity = new HttpEntity<>(journalpost, headers);
        return entity;
    }

    private void debugLogJournalpost(Journalpost journalpost) {
        if (log.isDebugEnabled()) {
            try {
                log.debug("JSON REQ: {}", JSON_MAPPER.writeValueAsString(journalpost));
            } catch (JacksonException e) {
            }
        }
    }

    private static boolean isNotBlank(String muligTomStreng) {
        return muligTomStreng != null && !muligTomStreng.trim().isEmpty();
    }

}
