package no.nav.tag.tiltaksgjennomforingprosess.integrasjon;

import no.nav.tag.tiltaksgjennomforingprosess.TestData;
import no.nav.tag.tiltaksgjennomforingprosess.domene.avtale.Avtale;
import no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost.Bruker;
import no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost.Dokument;
import no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost.DokumentVariant;
import no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost.Journalpost;
import no.nav.tag.tiltaksgjennomforingprosess.factory.JournalpostFactory;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost.DokumentVariant.*;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
@DirtiesContext
public class JoarkServiceIntTest {

    @Autowired
    private JoarkService joarkService;

    @Autowired
    private JournalpostFactory journalpostFactory;

    @Test
    public void oppretterJournalpost_til_arena() {
        Avtale avtale = TestData.opprettAvtale();
        avtale.setVersjon(1);

        Journalpost journalpost = journalpostFactory.konverterTilJournalpost(avtale);
        String jounalpostId = joarkService.sendJournalpost(journalpost);
        assertEquals("001", jounalpostId);
    }

    @Test
    public void oppretterJournalpost_ikke_til_arena() {
        Avtale avtale = TestData.opprettAvtale();
        avtale.setVersjon(2);
        Journalpost journalpost = journalpostFactory.konverterTilJournalpost(avtale);

        String jounalpostId = joarkService.sendJournalpost(journalpost);
        assertEquals("001", jounalpostId);
    }

}
