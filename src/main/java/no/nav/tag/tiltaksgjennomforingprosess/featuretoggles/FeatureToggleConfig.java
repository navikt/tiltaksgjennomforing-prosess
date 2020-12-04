package no.nav.tag.tiltaksgjennomforingprosess.featuretoggles;

import java.net.URI;

import lombok.extern.slf4j.Slf4j;
import no.finn.unleash.DefaultUnleash;
import no.finn.unleash.FakeUnleash;
import no.finn.unleash.Unleash;
import no.finn.unleash.util.UnleashConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Slf4j
@Configuration
public class FeatureToggleConfig {

    private static final String APP_NAME = "tiltaksgjennomforing-prosess";

    @Bean
    @Profile({"preprod", "prod"})
    public Unleash initializeUnleash(@Value(
            "${prosess.integrasjon.unleash.uri}") URI unleashUrl,
                                     ByEnvironmentStrategy byEnvironmentStrategy) {
        UnleashConfig config = UnleashConfig.builder()
                .appName(APP_NAME)
                .instanceId(APP_NAME + "-" + byEnvironmentStrategy.aktivtMiljø())
                .unleashAPI(unleashUrl)
                .build();
        log.info("Unleash URL: {}", config.getUnleashURLs().getFetchTogglesURL());

        return new DefaultUnleash(
                config,
                byEnvironmentStrategy
        );
    }

    @Bean
    @Profile("!(preprod | prod)")
    public Unleash unleashMock() {
        FakeUnleash fakeUnleash = new FakeUnleash();
        fakeUnleash.enable("tag.tiltak.prosess.mentor");
        return fakeUnleash;
    }
}
