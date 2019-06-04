package no.nav.tag.tiltaksgjennomforingprosess.sts;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.net.URI;

@Data
@Component
@ConfigurationProperties(prefix = "sts")
public class StsProperties {
    private URI uri;
    private String bruker;
    private String passord;
}
