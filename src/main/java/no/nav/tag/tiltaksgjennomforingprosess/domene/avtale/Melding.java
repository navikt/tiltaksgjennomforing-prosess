package no.nav.tag.tiltaksgjennomforingprosess.domene.avtale;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@XStreamAlias("melding")
public class Melding {

    @XStreamAlias("Innhold")
    private Innhold innhold;

    public Melding(Innhold innhold) {
        this.innhold = innhold;
    }

}
