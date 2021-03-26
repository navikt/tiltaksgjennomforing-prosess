package no.nav.tag.tiltaksgjennomforingprosess.leader;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import no.nav.tag.tiltaksgjennomforingprosess.properties.LeaderPodProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
@Slf4j
@Profile("!local")
@AllArgsConstructor
public class LeaderPodCheckImpl implements LeaderPodCheck {

    final LeaderPodProperties leaderPodProperties;
    final RestTemplate restTemplate;

    @Override
    public boolean isLeaderPod() {
        JSONObject leaderJson = getJSONFromUrl(leaderPodProperties.getPath());
        String leader = leaderJson.getAsString("name");
        String hostname;
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            log.error("Feil v/henting av host. Dropper jobb", e);
            return false;
        }
        return hostname.equals(leader);
    }

    private JSONObject getJSONFromUrl(String electorPath) {
        return restTemplate.getForEntity(electorPath, JSONObject.class).getBody();
    }
}
