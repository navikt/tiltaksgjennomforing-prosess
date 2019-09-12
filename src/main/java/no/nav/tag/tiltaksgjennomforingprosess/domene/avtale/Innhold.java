package no.nav.tag.tiltaksgjennomforingprosess.domene.avtale;

import lombok.Data;

@Data
public class Innhold {

    private SkjemaInfo skjemaInfo;
    private Generelt generelt;

    public Innhold(SkjemaInfo skjemaInfo, Generelt generelt) {
        this.skjemaInfo = skjemaInfo;
        this.generelt = generelt;
    }
 }
