package no.nav.tag.tiltaksgjennomforingprosess.domene.avtale;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SkjemaInfo {

    private static final String TILTAKSTYPE = "Arbeidstrening";
    private static final String TYPE_BEHANDLING = "ab0422";

    private String tiltaksType = TILTAKSTYPE;
    private String typeBehandling = TYPE_BEHANDLING;
    private LocalDate fraDato;
    private LocalDate tilDato;

    public SkjemaInfo(LocalDate fraDato, LocalDate tilDato) {
        this.fraDato = fraDato;
        this.tilDato = tilDato;
    }

}
