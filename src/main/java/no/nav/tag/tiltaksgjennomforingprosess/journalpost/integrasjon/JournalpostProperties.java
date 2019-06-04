package no.nav.tag.tiltaksgjennomforingprosess.journalpost.integrasjon;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.net.URI;

@Data
@Component
@ConfigurationProperties(prefix = "journalpost.integrasjon")
public class JournalpostProperties {
    private URI uri;
}
