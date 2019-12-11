package no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import java.util.List;

@Data
public class Journalpost {
    private final static String JOURNALPOST_TYPE = "INNGAAENDE";
    private final static String KANAL = "NAV_NO";
    private final static String TEMA = "TIL";
    final static String TITTEL = "Avtale om arbeidstrening";

    private final String journalposttype = JOURNALPOST_TYPE;
    private final String kanal = KANAL;
    private final String tema = TEMA;
    private final String tittel = TITTEL;

    private Bruker bruker;

    private List<Dokument> dokumenter;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String eksternReferanseId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String behandlingsTema;

    @JsonIgnore
    private Boolean behandlesIArena;
}