package no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost;

import lombok.Data;

import java.util.List;

@Data
public class Dokument {
    private String tittel;
    private List<DokumentVariant> dokumentVarianter;
}
