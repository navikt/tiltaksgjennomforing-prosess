package no.nav.tag.tiltaksgjennomforingprosess.domene.avtale;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Arbeidstaker {

    public Arbeidstaker(String foedselsnummer) {
        this.foedselsnummer = foedselsnummer;
    }

    private String foedselsnummer;
}
