package no.nav.tag.tiltaksgjennomforingprosess.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

@Configuration
@Profile({"dev", "preprod", "prod"})
public class RestTemplatePlain {

    @Bean(name = "restTemplate")
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
