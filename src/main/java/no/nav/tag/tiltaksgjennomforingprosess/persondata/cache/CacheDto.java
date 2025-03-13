package no.nav.tag.tiltaksgjennomforingprosess.persondata.cache;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@ConfigurationProperties(prefix = "caches")
public class CacheDto {

    private List<Cache> caffeine;

    @Data
    public static class Cache {

        private String name;

        private int expiryInMinutes;

        private int maximumSize;
    }
}

