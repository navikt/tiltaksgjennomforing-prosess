package no.nav.tag.tiltaksgjennomforingprosess.journalpost.request;

import lombok.Data;

@Data
public class DokumentVariant {
    private final String variantformat = "ARKIV";
    private final String filtype;
    private final String fysiskDokument;

    public DokumentVariant(String filtype, String fysiskDokument) {
        this.filtype = filtype;
        this.fysiskDokument = fysiskDokument;
    }
}
