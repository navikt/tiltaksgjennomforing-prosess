package no.nav.tag.tiltaksgjennomforingprosess.leader;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
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
    final String path;
    HttpEntity<String> entity;

    public LeaderPodCheckImpl(LeaderPodProperties leaderPodProperties, RestTemplate restTemplate) {
        this.leaderPodProperties = leaderPodProperties;
        this.restTemplate = restTemplate;
        this.path = "http://" + leaderPodProperties.getPath() + "/";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        entity = new HttpEntity<>(headers);
    }


    @Override
    public boolean isLeaderPod() {
        log.info("leader-elector url={}", path);

        String hostname;
        String leader;
        try {
            JSONObject leaderJson = getJSONFromUrl(path);
            leader = leaderJson.getAsString("name");
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            log.error("Feil v/henting av host. Dropper jobb", e);
            return false;
        } catch (Exception e) {
            log.error("Feil v/oppslag i leader-elector", e);
            throw e;
        }
        return hostname.equals(leader);
    }

    private JSONObject getJSONFromUrl(String electorPath) {
        ResponseEntity<JSONObject> responseEntity = restTemplate.exchange(electorPath, HttpMethod.GET, entity, JSONObject.class);
        log.info("RESP.toString: {}", responseEntity.toString());
        log.info("RESP.status: {}", responseEntity.getStatusCodeValue());
        log.info("RESP.body: {}", responseEntity.getBody());
        return responseEntity.getBody();
    }
}
