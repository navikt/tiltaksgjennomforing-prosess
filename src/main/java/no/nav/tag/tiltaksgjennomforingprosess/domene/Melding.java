package no.nav.tag.tiltaksgjennomforingprosess.domene;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@XStreamAlias("melding")
public class Melding {

    private Innhold innhold;

    public Melding(Innhold innhold) {
        this.innhold = innhold;
    }

}
