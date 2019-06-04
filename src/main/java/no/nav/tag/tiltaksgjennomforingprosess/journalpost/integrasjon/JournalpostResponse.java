package no.nav.tag.tiltaksgjennomforingprosess.journalpost.integrasjon;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JournalpostResponse {

    private String journalpostId;
    private String journalstatus;
    private String melding;
}

