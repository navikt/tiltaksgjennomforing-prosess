package no.nav.tag.tiltaksgjennomforingprosess.integrasjon;

import no.nav.tag.tiltaksgjennomforingprosess.TestData;
import no.nav.tag.tiltaksgjennomforingprosess.domene.avtale.Avtale;
import no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost.Journalpost;
import no.nav.tag.tiltaksgjennomforingprosess.factory.AvtaleTilXml;
import no.nav.tag.tiltaksgjennomforingprosess.factory.JournalpostFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static no.nav.tag.tiltaksgjennomforingprosess.JournalpostJobb.ferdigstill;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("local")
@DirtiesContext
public class JoarkServiceIntTest {

    @Autowired
    private JoarkService joarkService;

    @Autowired
    private AvtaleTilXml avtaleTilXml;
    @Autowired
    private DokgenAdapter dokgenAdapter;

    private JournalpostFactory journalpostFactory;

    @BeforeEach
    public void setUp() {
        journalpostFactory = new JournalpostFactory(avtaleTilXml, dokgenAdapter);
    }

    @Test
    public void oppretterJournalpost_til_arena() {
        Avtale avtale = TestData.opprettMentorAvtale();
        avtale.setVersjon(1);

        Journalpost journalpost = journalpostFactory.konverterTilJournalpost(avtale);
        String jounalpostId = joarkService.sendJournalpost(journalpost, ferdigstill(journalpost, avtale));
        assertEquals("003", jounalpostId);
    }

    @Test
    public void sommerjobb_ikke_til_arena() {
        Avtale avtale = TestData.opprettSommerjobbAvtale();
        avtale.setVersjon(1);

        Journalpost journalpost = journalpostFactory.konverterTilJournalpost(avtale);
        String jounalpostId = joarkService.sendJournalpost(journalpost, ferdigstill(journalpost, avtale));

        assertEquals("9999", journalpost.getJournalfoerendeEnhet());
        assertEquals("005", jounalpostId);
    }

    @Test
    public void oppretterJournalpost_ikke_til_arena() {
        Avtale avtale = TestData.opprettArbeidstreningAvtale();
        avtale.setBedriftNavn("Maura og Kolbu regnskap");
        avtale.setBedriftNr("910825518");
        avtale.setVersjon(2);

        Journalpost journalpost = journalpostFactory.konverterTilJournalpost(avtale);
        String jounalpostId = joarkService.sendJournalpost(journalpost, ferdigstill(journalpost, avtale));
        assertEquals("001", jounalpostId);
    }

    @Test
    public void oppretterJournalpost_til_arena_med_dokgen() {
        Avtale avtale = TestData.opprettMentorAvtale();
        avtale.setVersjon(1);

        Journalpost journalpost = journalpostFactory.konverterTilJournalpost(avtale);
        String jounalpostId = joarkService.sendJournalpost(journalpost, ferdigstill(journalpost, avtale));
        assertEquals("003", jounalpostId);
    }

    @Test
    public void oppretterJournalpost_ikke_til_arena_med_dokgen() {
        Avtale avtale = TestData.opprettArbeidstreningAvtale();
        avtale.setBedriftNavn("Maura og Kolbu regnskap");
        avtale.setBedriftNr("910825518");
        avtale.setVersjon(2);

        Journalpost journalpost = journalpostFactory.konverterTilJournalpost(avtale);
        String jounalpostId = joarkService.sendJournalpost(journalpost, ferdigstill(journalpost, avtale));
        assertEquals("001", jounalpostId);
    }

    @Test
    public void journalføring_gir_duplikatfeil() {
        assertThrows(RuntimeException.class, () -> {
            Avtale avtale = TestData.opprettArbeidstreningAvtale();
            avtale.setBedriftNavn("Maura og Kolbu regnskap");
            avtale.setBedriftNr("999999999");
            avtale.setVersjon(2);

            Journalpost journalpost = journalpostFactory.konverterTilJournalpost(avtale);
            assertNotNull(journalpost);
            joarkService.sendJournalpost(journalpost, ferdigstill(journalpost, avtale));
        });
    }

}
