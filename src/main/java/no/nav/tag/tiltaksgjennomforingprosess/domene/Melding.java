package no.nav.tag.tiltaksgjennomforingprosess.domene;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Data
@NoArgsConstructor
public class Melding {

    private Innhold innhold;

    public Melding(Innhold innhold) {
        this.innhold = innhold;
    }

}
