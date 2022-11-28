package no.nav.tag.tiltaksgjennomforingprosess.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "prosess.pilot")
public class PilotProperties {
    private List<String> pilotvirksomheter = new ArrayList<>();
    private List<String> pilotenheter = new ArrayList<>();
}
