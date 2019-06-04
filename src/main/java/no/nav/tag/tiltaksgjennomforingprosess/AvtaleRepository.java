package no.nav.tag.tiltaksgjennomforingprosess;

import no.nav.tag.tiltaksgjennomforingprosess.domene.Avtale;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface AvtaleRepository extends CrudRepository<Avtale, UUID> {

    @Query("SELECT * FROM avtale a WHERE a.journalpost_id is null AND a.godkjent_av_veileder")
    List<Avtale> finnIkkeJournalfoerte();
}
