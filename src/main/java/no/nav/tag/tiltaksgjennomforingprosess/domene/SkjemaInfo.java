package no.nav.tag.tiltaksgjennomforingprosess.domene;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.time.LocalDate;

@XmlAccessorType(XmlAccessType.FIELD)
@Data
@NoArgsConstructor
public class SkjemaInfo {

    private static final String TILTAKSTYPE = "arbeidstrening";
    private static final String TYPE_BEHANDLING = "ab0000"; //TODO Sett inn riktig

    protected String tiltaksType = TILTAKSTYPE;
    protected String typeBehandling = TYPE_BEHANDLING;
    protected LocalDate fraDato;

    public SkjemaInfo(LocalDate fraDato) {
        this.fraDato = fraDato;
    }

}
