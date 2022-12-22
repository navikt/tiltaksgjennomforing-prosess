package no.nav.tag.tiltaksgjennomforingprosess.domene.avtale;

import lombok.Data;

@Data
public class GodkjentPaVegneGrunn {

    private boolean ikkeBankId;
    private boolean reservert;
    private boolean digitalKompetanse;
    private boolean arenaMigreringDeltaker;
}

