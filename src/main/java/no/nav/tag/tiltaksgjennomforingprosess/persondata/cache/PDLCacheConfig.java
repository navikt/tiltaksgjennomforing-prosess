package no.nav.tag.tiltaksgjennomforingprosess.persondata.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@EnableCaching
public class PDLCacheConfig {
    public static final String PDL_CACHE = "pdl_cache";
    @Bean
    public CacheManager cacheManager(CacheDto cacheDto) {
        CaffeineCacheManager caffMan = new CaffeineCacheManager();
        cacheDto.getCaffeine().forEach(cache ->
                caffMan.registerCustomCache(cache.getName(), Caffeine.newBuilder()
                        .expireAfterWrite(Duration.ofMinutes(cache.getExpiryInMinutes()))
                        .maximumSize(cache.getMaximumSize())
                        .build()));
        return caffMan;
    }
}
