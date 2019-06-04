package no.nav.tag.tiltaksgjennomforingprosess.journalpost.request;

import lombok.Data;
import no.nav.tag.tiltaksgjennomforingprosess.domene.Avtale;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Data
public class Journalpost {

    private final String journalposttype = "INNGAAENDE";
    private final String behandlingsTema = "ab01600"; //TODO Finn/opprett riktig
    private final String kanal = "NAV_NO";
    private final String tema = "AAP"; //TODO Finn/opprett riktig. Er nå Arbeidsavklaringspenger
    private final String tittel = "Avtale om arbeidstrening";

    private Bruker bruker;
    private List<Dokument> dokumenter;





}