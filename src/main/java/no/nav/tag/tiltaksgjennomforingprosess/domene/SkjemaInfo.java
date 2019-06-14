package no.nav.tag.tiltaksgjennomforingprosess.domene;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class SkjemaInfo {

    private static final String TILTAKSTYPE = "arbeidstrening";
    private static final String TYPE_BEHANDLING = "ab0000"; //TODO Sett inn riktig

    private String tiltaksType = TILTAKSTYPE;
    private String typeBehandling = TYPE_BEHANDLING;
    private LocalDate fraDato;

    public SkjemaInfo(LocalDate fraDato) {
        this.fraDato = fraDato;
    }

}
