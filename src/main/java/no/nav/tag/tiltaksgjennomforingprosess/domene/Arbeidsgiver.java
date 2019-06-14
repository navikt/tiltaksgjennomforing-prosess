package no.nav.tag.tiltaksgjennomforingprosess.domene;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Arbeidsgiver {

    private String organisasjonsnummer;

    public Arbeidsgiver(String organisasjonsnummer) {
        this.organisasjonsnummer = organisasjonsnummer;
    }
}
