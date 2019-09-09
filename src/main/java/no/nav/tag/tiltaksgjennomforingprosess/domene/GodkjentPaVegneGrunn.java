package no.nav.tag.tiltaksgjennomforingprosess.domene;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GodkjentPaVegneGrunn {

    private boolean ikkeBankId;
    private boolean reservert;
    private boolean digitalKompetanse;
}

