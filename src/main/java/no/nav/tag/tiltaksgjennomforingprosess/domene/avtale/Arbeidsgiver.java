package no.nav.tag.tiltaksgjennomforingprosess.domene.avtale;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Arbeidsgiver {

    private String organisasjonsnummer;
    private String organisasjonsnavn;

    public Arbeidsgiver(String organisasjonsnummer, String organisasjonsnavn) {
        this.organisasjonsnummer = organisasjonsnummer;
        this.organisasjonsnavn = organisasjonsnavn;
    }
}
