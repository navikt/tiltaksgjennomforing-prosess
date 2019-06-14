package no.nav.tag.tiltaksgjennomforingprosess.domene;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
@Data
@NoArgsConstructor
public class Arbeidsgiver {

    protected String organisasjonsnummer;

    public Arbeidsgiver(String organisasjonsnummer) {
        this.organisasjonsnummer = organisasjonsnummer;
    }
}
