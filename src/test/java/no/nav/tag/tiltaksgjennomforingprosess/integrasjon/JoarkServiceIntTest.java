package no.nav.tag.tiltaksgjennomforingprosess.integrasjon;

import no.nav.tag.tiltaksgjennomforingprosess.factory.JournalpostFactory;
import no.nav.tag.tiltaksgjennomforingprosess.integrasjon.JoarkService;
import no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost.Bruker;
import no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost.Dokument;
import no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost.DokumentVariant;
import no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost.Journalpost;
import no.nav.tag.tiltaksgjennomforingprosess.properties.JournalpostProperties;
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

    private final String TOKEN = "eyxXxx";
    private JoarkService joarkService;

    @Autowired
    private JournalpostProperties journalpostProperties;

    @Autowired
    private JournalpostFactory journalpostFactory;

    @Autowired
    public void setJoarkService(JoarkService joarkService){
        this.joarkService = joarkService;
    }


    @Test
    public void oppretterJournalpost() {
        Journalpost journalpost = new Journalpost();

        Bruker bruker  = new Bruker();
        bruker.setId("88888899999");
        journalpost.setBruker(bruker);

        Dokument dokument = new Dokument();
        dokument.setDokumentVarianter(Arrays.asList(new DokumentVariant(FILTYPE_XML, VARIANFORMAT_XML, "xmlxmlxml"), new DokumentVariant(FILTYPE_PDF, VARIANFORMAT_PDF, "pdfpdfpdf")));
        journalpost.setDokumenter(Arrays.asList(dokument));

        String jounalpostId = joarkService.sendJournalpost(TOKEN, journalpost);
        assertEquals("001", jounalpostId);
    }

}
