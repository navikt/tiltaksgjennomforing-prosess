package no.nav.tag.tiltaksgjennomforingprosess.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "prosess.pilot")
public class PilotProperties {
    private List<String> pilotvirksomheter;
}
