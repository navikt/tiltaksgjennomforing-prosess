package no.nav.tag.tiltaksgjennomforingprosess.domene;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {
        "arbeidsgiver",
        "arbeidstaker",
        "avtale"
})
@Data
@NoArgsConstructor
public class Generelt {

    private Arbeidsgiver arbeidsgiver;
    private Arbeidstaker arbeidstaker;
    private Avtale avtale;

    public Generelt(Arbeidsgiver arbeidsgiver, Arbeidstaker arbeidstaker, Avtale avtale) {
        this.arbeidsgiver = arbeidsgiver;
        this.arbeidstaker = arbeidstaker;
        this.avtale = avtale;
    }
}
