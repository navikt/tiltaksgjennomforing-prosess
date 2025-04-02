package no.nav.tag.tiltaksgjennomforingprosess.integrasjon;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tiltaksgjennomforingprosess.domene.avtale.Avtale;
import no.nav.tag.tiltaksgjennomforingprosess.persondata.PersondataService;
import no.nav.tag.tiltaksgjennomforingprosess.properties.TiltakApiProperties;
import no.nav.team_tiltak.felles.persondata.pdl.domene.Diskresjonskode;
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
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TiltaksgjennomfoeringApiService {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private StsService stsService;

    @Autowired
    private PersondataService persondataService;

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

    public List<Avtale> finnAvtalerTilJournalfoering() {
        headers.setBearerAuth(stsService.hentToken());
        List<Avtale> avtaleList;
        try {
            avtaleList = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers), new ParameterizedTypeReference<List<Avtale>>() {
            }).getBody();
        } catch (Exception e) {
            log.warn("Feil ved kommunikasjon mot avtale-API ved get ({}). Henter nytt sts-token og forsøker igjen", e.getMessage());
            stsService.evict();
            headers.setBearerAuth(stsService.hentToken());
            avtaleList = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers), new ParameterizedTypeReference<List<Avtale>>() {
            }).getBody();
        }
        return sladdeKode6og7Avtaler(avtaleList);
    }

    public void settAvtalerTilJournalfoert(Map<UUID, String> avtalerTilJournalfoert) {
        debugApiKall(avtalerTilJournalfoert);
        headers.setBearerAuth(stsService.hentToken());
        try {
            restTemplate.exchange(uri, HttpMethod.PUT, new HttpEntity<>(avtalerTilJournalfoert, headers), Void.class);
        } catch (Exception e) {
            log.warn("Feil ved kommunikasjon mot avtale-API ved put ({}).  Henter nytt sts-token og forsøker igjen", e.getMessage());
            stsService.evict();
            headers.setBearerAuth(stsService.hentToken());
            restTemplate.exchange(uri, HttpMethod.PUT, new HttpEntity<>(avtalerTilJournalfoert, headers), Void.class);
        }
    }

    public List<Avtale> sladdeKode6og7Avtaler(List<Avtale> avtaler){
        Set<String> fnrDeltakere = avtaler.stream().map(Avtale::getDeltakerFnr).collect(Collectors.toSet());
        Map<String, Optional<Diskresjonskode>> diskresjonskoder = persondataService.hentDiskresjonskoder(fnrDeltakere);
        avtaler.forEach(avtale -> {
            var diskresjonskode = diskresjonskoder.get(avtale.getDeltakerFnr()).orElseThrow(
                () -> new IllegalStateException(
                    "Klarte ikke utlede diskresjonskode for deltaker. Vet derfor ikke om avtale skal sladdes eller ikke"
                )
            );
            if (diskresjonskode.erKode6Eller7()) {
                avtale.utførSladdingAvFelter();
            }
        });
        return avtaler;
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
