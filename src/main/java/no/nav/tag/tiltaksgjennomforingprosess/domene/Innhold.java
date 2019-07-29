package no.nav.tag.tiltaksgjennomforingprosess.domene;

import lombok.Data;

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "Innhold", propOrder = {
    "skjemaInfo",
    "generelt"
})
@Data
public class Innhold {

    private SkjemaInfo skjemaInfo;
    private Generelt generelt;

    public Innhold(SkjemaInfo skjemaInfo, Generelt generelt) {
        this.skjemaInfo = skjemaInfo;
        this.generelt = generelt;
    }
 }
