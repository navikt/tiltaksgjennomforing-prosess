package no.nav.tag.tiltaksgjennomforingprosess.domene.avtale;

import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
public class SkjemaInfo {

    public static final String DATOFORMAT_ARENA = "YYYY-MM-dd";
    private final static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATOFORMAT_ARENA);

    private static final String TILTAKSTYPE = "Arbeidstrening";
    private static final String TYPE_BEHANDLING = "ab0422";

    private String tiltaksType = TILTAKSTYPE;
    private String typeBehandling = TYPE_BEHANDLING;
    private String fraDato;
    private String tilDato;

    public SkjemaInfo(LocalDate fraDato, LocalDate tilDato) {
        this.fraDato = fraDato.format(dateTimeFormatter);

        //TODO Påkrevd av Arena. Ta bort dette når sluttDato på avtalen er på plass
        if (tilDato == null) {
            this.tilDato = fraDato.plusMonths(3).format(dateTimeFormatter);
            return;
        }

        this.tilDato = tilDato.format(dateTimeFormatter);
    }

}
