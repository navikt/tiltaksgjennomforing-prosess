package no.nav.tag.tiltaksgjennomforingprosess.journalpost.request;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Bruker {
    private final String idType = "FNR";
    private String id;
}
