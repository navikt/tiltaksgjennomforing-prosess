package no.nav.tag.tiltaksgjennomforingprosess.domene;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {
        "arbeidsgiver",
        "arbeidstaker",
        "avtale"
})
@Data
@NoArgsConstructor
public class Generelt {

    protected Arbeidsgiver arbeidsgiver;
    protected Arbeidstaker arbeidstaker;
    protected Avtale avtale;

    public Generelt(Arbeidsgiver arbeidsgiver, Arbeidstaker arbeidstaker, Avtale avtale) {
        this.arbeidsgiver = arbeidsgiver;
        this.arbeidstaker = arbeidstaker;
        this.avtale = avtale;
    }
}
