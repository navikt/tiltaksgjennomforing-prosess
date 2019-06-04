package no.nav.tag.tiltaksgjennomforingprosess.journalpost.request;

import lombok.Data;
import java.util.List;

@Data
public class Dokument {
    private List<DokumentVariant> dokumentVarianter;
}
