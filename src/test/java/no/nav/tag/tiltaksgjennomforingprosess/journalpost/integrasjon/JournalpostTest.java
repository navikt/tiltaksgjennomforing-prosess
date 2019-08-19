package no.nav.tag.tiltaksgjennomforingprosess.journalpost.integrasjon;

import com.fasterxml.jackson.core.JsonProcessingException;
import no.nav.tag.tiltaksgjennomforingprosess.TestData;
import no.nav.tag.tiltaksgjennomforingprosess.domene.Avtale;
import no.nav.tag.tiltaksgjennomforingprosess.journalpost.factory.AvtaleTilPdf;
import no.nav.tag.tiltaksgjennomforingprosess.journalpost.factory.JournalpostFactory;
import no.nav.tag.tiltaksgjennomforingprosess.journalpost.request.Journalpost;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class JournalpostTest {

    private Journalpost journalpost;

    @Test
    // @Ignore
    public void oppretter_journalpost() throws Exception {
        Avtale avtale = TestData.opprettAvtale();
        JournalpostFactory journalpostFactory = new JournalpostFactory();

        Assert.assertTrue("Kunne ikke lese Pdf filen", journalpostFactory.encodeToBase64(new AvtaleTilPdf().generererPdf(avtale), true) != "");
    }


    @Test
    public void whenSerializingJava8Date_thenCorrect() throws JsonProcessingException {

    String avtale = TestData.avtaleTilJSON(TestData.opprettAvtale());
        System.out.println(avtale);
    }
}
