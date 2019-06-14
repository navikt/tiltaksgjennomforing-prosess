package no.nav.tag.tiltaksgjennomforingprosess.domene;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Innhold", propOrder = {
    "skjemaInfo",
    "generelt"
})
@Data
public class Innhold {

    protected SkjemaInfo skjemaInfo;
    protected Generelt generelt;

    public Innhold(SkjemaInfo skjemaInfo, Generelt generelt) {
        this.skjemaInfo = skjemaInfo;
        this.generelt = generelt;
    }
 }
