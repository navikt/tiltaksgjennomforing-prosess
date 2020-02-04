package no.nav.tag.tiltaksgjennomforingprosess.featuretoggles;

import com.google.common.base.Joiner;
import no.finn.unleash.strategy.Strategy;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Component
public class ByEnvironmentStrategy implements Strategy {

    private final Environment environment;

    public ByEnvironmentStrategy(Environment environment) {
        this.environment = environment;
    }

    @Override
    public String getName() {
        return "byEnvironment";
    }

    @Override
    public boolean isEnabled(Map<String, String> parameters) {
        return Optional.ofNullable(parameters)
                .map(map -> map.get("miljø"))
                .map(env -> env.split(","))
                .map(Stream::of)
                .map(stringStream -> stringStream.anyMatch(Arrays.asList(environment.getActiveProfiles())::contains))
                .orElse(false);
    }

    public String aktivtMiljø() {
        return Joiner.on(",").join(environment.getActiveProfiles());
    }
}
