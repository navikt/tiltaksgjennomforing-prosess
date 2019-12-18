package no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Sak {

    private static final String TYPE_SAK = "GENERELL_SAK";
    private final String sakstype = TYPE_SAK;
}
