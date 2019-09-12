package no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Tilleggsopplysning {

    public final static String NØKKEL_KANALREFERANSE = "kanalReferanseId";

    private String nøkkel;
    private String verdi;
}
