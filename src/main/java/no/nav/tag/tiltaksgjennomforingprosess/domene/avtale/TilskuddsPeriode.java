package no.nav.tag.tiltaksgjennomforingprosess.domene.avtale;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;

@Data
@ToString
public class TilskuddsPeriode {
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate sluttDato;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDato;
    private Integer beløp;
    private Integer lonnstilskuddProsent;

}
