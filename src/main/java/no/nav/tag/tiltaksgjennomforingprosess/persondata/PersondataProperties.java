package no.nav.tag.tiltaksgjennomforingprosess.persondata;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.net.URI;

@Data
@Component
@ConfigurationProperties(prefix = "prosess.integrasjon.persondata")
public class PersondataProperties {
    private URI uri;
}
