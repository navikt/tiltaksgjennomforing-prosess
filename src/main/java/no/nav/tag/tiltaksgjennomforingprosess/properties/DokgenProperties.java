package no.nav.tag.tiltaksgjennomforingprosess.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.net.URI;

@Data
@Component
@ConfigurationProperties(prefix = "prosess.integrasjon.dokgen")
public class DokgenProperties {
    private URI uri;
}
