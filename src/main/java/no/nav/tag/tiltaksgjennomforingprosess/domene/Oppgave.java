package no.nav.tag.tiltaksgjennomforingprosess.domene;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Oppgave {

    private String tittel;
    private String beskrivelse;
    private String opplaering;
}