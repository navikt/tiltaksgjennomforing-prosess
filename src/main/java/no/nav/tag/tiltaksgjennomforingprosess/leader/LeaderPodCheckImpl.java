package no.nav.tag.tiltaksgjennomforingprosess.leader;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tiltaksgjennomforingprosess.properties.LeaderPodProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

@Component
@Slf4j
@Profile("!local")
public class LeaderPodCheckImpl implements LeaderPodCheck {

    final LeaderPodProperties leaderPodProperties;
    final RestTemplate restTemplate;
    final ObjectMapper objectMapper;
    final String path;
    HttpEntity<String> entity;

    public LeaderPodCheckImpl(LeaderPodProperties leaderPodProperties, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.leaderPodProperties = leaderPodProperties;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.path = "http://" + leaderPodProperties.getPath() + "/";
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.ALL));
        entity = new HttpEntity<>(headers);
    }

    @Override
    public boolean isLeaderPod() {
        String hostname;
        String leader;
        try {
            leader = getJSONFromUrl(path).name;
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            log.error("Feil v/henting av host. Dropper jobb", e);
            return false;
        } catch (Exception e) {
            log.error("Feil v/oppslag i leader-elector", e);
            throw new RuntimeException(e);
        }
        return hostname.equals(leader);
    }

    private Elector getJSONFromUrl(String electorPath) throws JsonProcessingException {
        ResponseEntity responseEntity = restTemplate.exchange(electorPath, HttpMethod.GET, entity, String.class);
        return objectMapper.readValue((String) responseEntity.getBody(), Elector.class);
    }

    @Data
    private static class Elector {
        String name;
    }
}
