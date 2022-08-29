package no.nav.tag.tiltaksgjennomforingprosess.domene.avtale;

import lombok.Data;
import lombok.ToString;
import java.util.UUID;

@Data
@ToString
public class Inkluderingstilskuddsutgift {

    private UUID id;
    private Integer beløp;
    private InkluderingstilskuddsutgiftType type;
}
