package no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost;

import lombok.Data;
import java.util.List;

import static no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost.Journalpost.TITTEL;

@Data
public class Dokument {
    private final String tittel = TITTEL;
    private List<DokumentVariant> dokumentVarianter;
}
