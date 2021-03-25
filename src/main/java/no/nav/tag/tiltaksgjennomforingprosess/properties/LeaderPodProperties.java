package no.nav.tag.tiltaksgjennomforingprosess.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "elector")
public class LeaderPodProperties {
    private String path;
}
