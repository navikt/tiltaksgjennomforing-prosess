package no.nav.tag.tiltaksgjennomforingprosess.integrasjon;

import no.finn.unleash.FakeUnleash;
import no.nav.tag.tiltaksgjennomforingprosess.TestData;
import no.nav.tag.tiltaksgjennomforingprosess.domene.avtale.Avtale;
import no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost.Journalpost;
import no.nav.tag.tiltaksgjennomforingprosess.factory.AvtaleTilXml;
import no.nav.tag.tiltaksgjennomforingprosess.factory.JournalpostFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static no.nav.tag.tiltaksgjennomforingprosess.JournalpostJobb.ferdigstill;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
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

    private FakeUnleash unleash;

    private JournalpostFactory journalpostFactory;

    @Before
    public void setUp() {
        unleash = new FakeUnleash();
        journalpostFactory = new JournalpostFactory(avtaleTilXml, dokgenAdapter, unleash);
    }

    @Test
    public void oppretterJournalpost_til_arena() {
        unleash.disableAll();
        Avtale avtale = TestData.opprettLonnstilskuddsAvtale();
        avtale.setVersjon(1);

        Journalpost journalpost = journalpostFactory.konverterTilJournalpost(avtale);
        String jounalpostId = joarkService.sendJournalpost(journalpost, ferdigstill(journalpost, avtale));
        assertEquals("002", jounalpostId);
    }

    @Test
    public void sommerjobb_ikke_til_arena() {
        unleash.disableAll();
        Avtale avtale = TestData.opprettSommerjobbAvtale();
        avtale.setVersjon(1);

        Journalpost journalpost = journalpostFactory.konverterTilJournalpost(avtale);
        String jounalpostId = joarkService.sendJournalpost(journalpost, ferdigstill(journalpost, avtale));

        assertEquals("9999", journalpost.getJournalfoerendeEnhet());
        assertEquals("005", jounalpostId);
    }

    @Test
    public void oppretterJournalpost_ikke_til_arena() {
        unleash.disableAll();
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
        unleash.enableAll();
        Avtale avtale = TestData.opprettLonnstilskuddsAvtale();
        avtale.setVersjon(1);

        Journalpost journalpost = journalpostFactory.konverterTilJournalpost(avtale);
        String jounalpostId = joarkService.sendJournalpost(journalpost, ferdigstill(journalpost, avtale));
        assertEquals("002", jounalpostId);
    }

    @Test
    public void oppretterJournalpost_ikke_til_arena_med_dokgen() {
        unleash.enableAll();
        Avtale avtale = TestData.opprettArbeidstreningAvtale();
        avtale.setBedriftNavn("Maura og Kolbu regnskap");
        avtale.setBedriftNr("910825518");
        avtale.setVersjon(2);

        Journalpost journalpost = journalpostFactory.konverterTilJournalpost(avtale);
        String jounalpostId = joarkService.sendJournalpost(journalpost, ferdigstill(journalpost, avtale));
        assertEquals("001", jounalpostId);
    }

    @Test(expected = RuntimeException.class)
    public void journalføring_gir_duplikatfeil() {
        unleash.enableAll();
        Avtale avtale = TestData.opprettArbeidstreningAvtale();
        avtale.setBedriftNavn("Maura og Kolbu regnskap");
        avtale.setBedriftNr("999999999");
        avtale.setVersjon(2);

        Journalpost journalpost = journalpostFactory.konverterTilJournalpost(avtale);
        assertNotNull(journalpost);
        joarkService.sendJournalpost(journalpost, ferdigstill(journalpost, avtale));
    }

}
