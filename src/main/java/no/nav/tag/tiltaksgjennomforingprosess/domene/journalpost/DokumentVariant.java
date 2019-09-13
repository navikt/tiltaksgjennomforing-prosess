package no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost;

import lombok.Data;

@Data
public class DokumentVariant {

    public static final String FILTYPE_XML = "XML";
    public static final String FILTYPE_PDF = "PDFA";
    public static final String VARIANFORMAT_XML = "ORIGINAL";
    public static final String VARIANFORMAT_PDF = "ARKIV";

    private final String filtype;
    private final String variantformat;
    private final String fysiskDokument;

    public DokumentVariant(String filtype, String variantFormat, String fysiskDokument) {
        this.filtype = filtype;
        this.variantformat = variantFormat;
        this.fysiskDokument = fysiskDokument;
    }
}
