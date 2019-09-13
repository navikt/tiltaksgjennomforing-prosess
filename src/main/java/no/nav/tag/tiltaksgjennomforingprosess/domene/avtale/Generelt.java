package no.nav.tag.tiltaksgjennomforingprosess.domene.avtale;

import lombok.Data;
import lombok.NoArgsConstructor;

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
