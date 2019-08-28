package no.nav.tag.tiltaksgjennomforingprosess.domene;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

//import static no.nav.tag.tiltaksgjennomforingprosess.domene.Avtale.DATETIME_PATTERN;

@Data
@ToString
public class Maal {

    private String kategori;
    private String beskrivelse;

//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIME_PATTERN)
//    private LocalDateTime opprettetTidspunkt;

}
