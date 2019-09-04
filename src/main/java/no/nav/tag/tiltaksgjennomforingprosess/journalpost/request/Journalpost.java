package no.nav.tag.tiltaksgjennomforingprosess.journalpost.request;

import lombok.Data;
import java.util.List;

@Data
public class Journalpost {

    private final String journalposttype = "INNGAAENDE";
    private final String behandlingsTema = "ab0422";
    private final String kanal = "NAV_NO";
    private final String tema = "ARBTREN";
    private final String tittel = "Avtale om arbeidstrening";

    private Bruker bruker;
    private List<Dokument> dokumenter;





}