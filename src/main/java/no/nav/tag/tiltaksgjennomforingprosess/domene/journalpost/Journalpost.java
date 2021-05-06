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


    public final static String JORURNALFØRENDE_ENHET = "9999";

    private final String journalposttype = JOURNALPOST_TYPE;
    private final String kanal = KANAL;
    private final String tema = TEMA;

    private String tittel;

    private Bruker bruker;

    private List<Dokument> dokumenter;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Sak sak;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Avsender avsenderMottaker;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String eksternReferanseId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String behandlingsTema;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String journalfoerendeEnhet;

    @JsonIgnore
    private String avtaleId;

    @JsonIgnore
    private String avtaleVersjonId;

    @JsonIgnore
    private Integer avtaleVersjon;

    public boolean skalBehandlesIArena(){
        //TODO: Oppdatere til behandlingstemaet til sommerjobb nar det er pa plass.
        return avtaleVersjon == 1;
    }
}