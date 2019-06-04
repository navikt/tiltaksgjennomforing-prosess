package no.nav.tag.tiltaksgjennomforingprosess;

import no.nav.tag.tiltaksgjennomforingprosess.domene.Avtale;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
@DirtiesContext
public class AvtaleRepositoryTest {

    @Autowired
    public AvtaleRepository avtaleRepository;

    @Test
    public void henterAvtalerSomSkalJournalfores(){
        avtaleRepository.count();
        List<Avtale> avtaler = avtaleRepository.finnIkkeJournalfoerte();
        assertFalse("Ingen avtaler funnet", avtaler.isEmpty());
        avtaler.forEach(avtale -> {
            assertTrue(avtale.isGodkjentAvArbeidsgiver());
            assertTrue(avtale.isGodkjentAvDeltaker());
            assertTrue(avtale.isGodkjentAvVeileder());
            assertNull(avtale.getJournalpostId());
        });
    }
}
