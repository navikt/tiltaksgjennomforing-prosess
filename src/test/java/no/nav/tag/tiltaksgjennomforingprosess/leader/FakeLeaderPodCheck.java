package no.nav.tag.tiltaksgjennomforingprosess.leader;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("local")
public class FakeLeaderPodCheck implements LeaderPodCheck {
    @Override
    public boolean isLeaderPod() {
        return true;
    }
}
