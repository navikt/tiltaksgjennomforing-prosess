package no.nav.tag.tiltaksgjennomforingprosess.properties;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.net.URI;

@Data
@Component
@ConfigurationProperties(prefix = "prosess.integrasjon.api")
public class TiltakApiProperties {
    private URI uri;
}
