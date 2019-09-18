package no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost;

import lombok.Data;
import java.util.List;

@Data
public class Journalpost {
    private final static String JOURNALPOST_TYPE = "INNGAAENDE";
    private final static String BEHANDLINGSTEMA = "ab0422";
    private final static String KANAL = "NAV_NO";
    private final static String TEMA = "TIL";

    final static String TITTEL = "Avtale om arbeidstrening";
    public final static String EKSTREF_PREFIKS = "AVT";

    private final String journalposttype = JOURNALPOST_TYPE;
    private final String behandlingsTema = BEHANDLINGSTEMA;
    private final String kanal = KANAL;
    private final String tema = TEMA;
    private String eksternReferanseId;

    private Bruker bruker;
    private final String tittel = TITTEL;
    private List<Dokument> dokumenter;
}