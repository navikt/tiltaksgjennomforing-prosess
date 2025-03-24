package no.nav.tag.tiltaksgjennomforingprosess.persondata.domene;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Value;
import no.nav.tag.tiltaksgjennomforingprosess.persondata.Diskresjonskode;

@Value
public class Adressebeskyttelse {
    public static final Adressebeskyttelse INGEN_BESKYTTELSE = new Adressebeskyttelse(Diskresjonskode.UGRADERT);

    @JsonDeserialize(using = Diskresjonskode.DiskresjonskodeDeserializer.class)
    private final Diskresjonskode gradering;

    @JsonCreator
    public Adressebeskyttelse(@JsonProperty("gradering") Diskresjonskode gradering) {
        this.gradering = gradering;
    }
}
