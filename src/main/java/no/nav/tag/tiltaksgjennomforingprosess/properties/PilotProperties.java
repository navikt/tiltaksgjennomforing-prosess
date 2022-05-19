package no.nav.tag.tiltaksgjennomforingprosess.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "prosess.pilot")
public class PilotProperties {
    private List<String> pilotvirksomheter;
}
