package no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost;

import lombok.Data;
import java.util.List;

@Data
public class Dokument {
    private List<DokumentVariant> dokumentVarianter;
}
