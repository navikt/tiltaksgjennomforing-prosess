package no.nav.tag.tiltaksgjennomforingprosess.domene.avtale;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SkjemaInfo {

    private String tiltaksType;
    private String typeBehandling;
    private LocalDate fraDato;
    private LocalDate tilDato;

    public SkjemaInfo(Tiltakstype tiltakstypeEnum, LocalDate fraDato, LocalDate tilDato) {
        this.tiltaksType = tiltakstypeEnum.getDokforTiltakskodeSkjema();
        this.typeBehandling = tiltakstypeEnum.getBehandlingstema();
        this.fraDato = fraDato;
        this.tilDato = tilDato;
    }
}
