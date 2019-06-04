package no.nav.tag.tiltaksgjennomforingprosess.journalpost.integrasjon;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tiltaksgjennomforingprosess.domene.Avtale;
import no.nav.tag.tiltaksgjennomforingprosess.journalpost.builder.JournalpostFactory;
import no.nav.tag.tiltaksgjennomforingprosess.journalpost.request.Journalpost;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;

@Slf4j
@Service
public class JoarkService {

    private static final String PATH = "/rest/journalpostapi/v1/journalpost";
    private static final String QUERY_PARAM = "forsoekFerdigstill=false";
    private URI uri;
    private final HttpHeaders headers = new HttpHeaders();

    public JoarkService(JournalpostProperties journalpostProperties) {
        uri = UriComponentsBuilder.fromUri(journalpostProperties.getUri())
                .path(PATH)
                .query(QUERY_PARAM)
                .build()
                .toUri();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType((MediaType.APPLICATION_JSON));
    }

    public String opprettOgSendJournalpost(final String token, final Avtale avtale) {
        Journalpost journalpost = new JournalpostFactory().konverterTilJournalpost(avtale);

        //TODO Fjerne
        try {
            log.info("URI={}", uri);
            log.info("JSON REQ: {}", new ObjectMapper().writeValueAsString(journalpost));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        headers.setBearerAuth(token);
        HttpEntity<Journalpost> entity = new HttpEntity<>(journalpost, headers);
        JournalpostResponse response;
        try {
            response = new RestTemplate().postForObject(uri, entity, JournalpostResponse.class);
        } catch (Exception e) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Kall til Joark feilet: " + e.getMessage());
        }
        return response.getJournalpostId();
    }
}
