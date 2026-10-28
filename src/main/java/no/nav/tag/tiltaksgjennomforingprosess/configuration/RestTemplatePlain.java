package no.nav.tag.tiltaksgjennomforingprosess.configuration;

import no.nav.tag.tiltaksgjennomforingprosess.factory.RequestResponseLoggingInterceptor;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplatePlain {

    /**
     * Bygges via Spring Boots RestTemplateBuilder slik at JSON-konvertereren bruker
     * den autokonfigurerte JsonMapper-en (Jackson 3). En ren {@code new RestTemplate()}
     * får Jackson 3-standardinnstillinger, som blant annet feiler på ukjente felter.
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
            .requestFactory(() -> new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()))
            .interceptors(new RequestResponseLoggingInterceptor())
            .build();
    }
}
