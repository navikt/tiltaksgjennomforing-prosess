package no.nav.tag.tiltaksgjennomforingprosess.journalpost.request;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class Bruker {
    private final String idType = "FNR"; //TODO Sjekk global enum
    private String id;
}
