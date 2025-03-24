package no.nav.tag.tiltaksgjennomforingprosess.persondata;

import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tiltaksgjennomforingprosess.persondata.cache.PDLCacheConfig;
import no.nav.tag.tiltaksgjennomforingprosess.persondata.domene.PdlRequest;
import no.nav.tag.tiltaksgjennomforingprosess.persondata.domene.PdlRespons;
import no.nav.tag.tiltaksgjennomforingprosess.persondata.domene.PdlResponsBolk;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Set;

@Slf4j
@Component
public class PersondataClient {
    private static final String BEHANDLINGSNUMMER = "B662";

    private final RestTemplate azureRestTemplate;
    private final PersondataProperties persondataProperties;

    @Value("classpath:pdl/hentPersonBolk.graphql")
    private Resource adressebeskyttelseBolkQueryResource;

    @Value("classpath:pdl/hentPersondata.graphql")
    private Resource persondataQueryResource;

    public PersondataClient(
        RestTemplate azureRestTemplate,
        PersondataProperties persondataProperties
    ) {
        this.azureRestTemplate = azureRestTemplate;
        this.persondataProperties = persondataProperties;
    }

    public PdlResponsBolk hentPersonBolk(Set<String> fnr) {
        try {
            PdlRequest<PdlRequest.BolkVariables> pdlRequest = new PdlRequest<>(
                adressebeskyttelseBolkQueryResource,
                new PdlRequest.BolkVariables(new ArrayList<>(fnr))
            );
            return azureRestTemplate.postForObject(persondataProperties.getUri(), createRequestEntity(pdlRequest), PdlResponsBolk.class);
        } catch (RestClientException exception) {
            log.error("Feil fra PDL med request-url: {}", persondataProperties.getUri(), exception);
            throw exception;
        }
    }

    @Cacheable(PDLCacheConfig.PDL_CACHE)
    public PdlRespons hentPersondata(String fnr) {
        try {
            PdlRequest<PdlRequest.Varaibles> pdlRequest = new PdlRequest<>(
                persondataQueryResource,
                new PdlRequest.Varaibles(fnr)
            );
            return azureRestTemplate.postForObject(persondataProperties.getUri(), createRequestEntity(pdlRequest), PdlRespons.class);
        } catch (RestClientException exception) {
            log.error("Feil fra PDL med request-url: {}", persondataProperties.getUri(), exception);
            throw exception;
        }
    }

    private HttpEntity<String> createRequestEntity(PdlRequest<?> pdlRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Behandlingsnummer", BEHANDLINGSNUMMER);
        return new HttpEntity(pdlRequest, headers);
    }
}
