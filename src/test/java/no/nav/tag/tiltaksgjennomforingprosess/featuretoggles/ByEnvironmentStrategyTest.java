package no.nav.tag.tiltaksgjennomforingprosess.featuretoggles;

import org.junit.Test;
import org.springframework.core.env.Environment;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ByEnvironmentStrategyTest {
    @Test
    public void miljø_som_ikke_matcher() {
        Environment environment = mock(Environment.class);
        when(environment.getActiveProfiles()).thenReturn(new String[]{"dev"});
        ByEnvironmentStrategy strategy = new ByEnvironmentStrategy(environment);
        assertThat(strategy.isEnabled(Map.of("miljø", "test"))).isFalse();
    }

    @Test
    public void miljø_som_matcher() {
        Environment environment = mock(Environment.class);
        when(environment.getActiveProfiles()).thenReturn(new String[]{"dev", "foo"});
        ByEnvironmentStrategy strategy = new ByEnvironmentStrategy(environment);
        assertThat(strategy.isEnabled(Map.of("miljø", "foobar,dev"))).isTrue();
    }
}