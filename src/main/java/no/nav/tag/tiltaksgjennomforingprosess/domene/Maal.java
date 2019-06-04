package no.nav.tag.tiltaksgjennomforingprosess.domene;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class Maal {
    @Id
    private UUID id;
    private LocalDateTime opprettetTidspunkt;
    private String kategori;
    private String beskrivelse;
}
