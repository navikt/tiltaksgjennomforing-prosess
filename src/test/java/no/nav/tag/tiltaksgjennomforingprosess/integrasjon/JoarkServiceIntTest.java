package no.nav.tag.tiltaksgjennomforingprosess.integrasjon;

import no.nav.tag.tiltaksgjennomforingprosess.integrasjon.JoarkService;
import no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost.Bruker;
import no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost.Dokument;
import no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost.DokumentVariant;
import no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost.Journalpost;
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

    @Test
    public void oppretterJournalpost() {
        Journalpost journalpost = new Journalpost();
        journalpost.setBehandlingsTema("ab0422");

        Bruker bruker  = new Bruker();
        bruker.setId("88888899999");
        journalpost.setBruker(bruker);

        Dokument dokument = new Dokument();
        dokument.setDokumentVarianter(Arrays.asList(new DokumentVariant(FILTYPE_XML, VARIANFORMAT_XML, "xmlxmlxml"), new DokumentVariant(FILTYPE_PDF, VARIANFORMAT_PDF, "pdfpdfpdf")));
        journalpost.setDokumenter(Arrays.asList(dokument));

        String jounalpostId = joarkService.sendJournalpost(journalpost);
        assertEquals("001", jounalpostId);
    }

}
