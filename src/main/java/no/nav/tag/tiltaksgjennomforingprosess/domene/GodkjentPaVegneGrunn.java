package no.nav.tag.tiltaksgjennomforingprosess.domene;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.UUID;


@Data
public class GodkjentPaVegneGrunn {
    @Id
    private UUID avtale;
    private boolean ikkeBankId;
    private boolean reservert;
    private boolean digitalKompetanse;


}

