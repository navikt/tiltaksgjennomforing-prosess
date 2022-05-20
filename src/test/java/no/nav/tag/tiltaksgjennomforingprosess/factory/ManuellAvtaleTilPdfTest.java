package no.nav.tag.tiltaksgjennomforingprosess.factory;

import no.nav.tag.tiltaksgjennomforingprosess.TestData;
import no.nav.tag.tiltaksgjennomforingprosess.domene.avtale.Avtale;
import no.nav.tag.tiltaksgjennomforingprosess.domene.avtale.Tiltakstype;
import no.nav.tag.tiltaksgjennomforingprosess.integrasjon.DokgenAdapter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.io.ByteArrayInputStream;

@Disabled("Til manuell sjekk av pdf layout - kjøres mot tiltak-dokgen lokal. husk å endre port i application.local til porten doken kjører på lokalt.")
@SpringBootTest
@ActiveProfiles("local")
@DirtiesContext
public class ManuellAvtaleTilPdfTest {

    @Autowired
    DokgenAdapter dokgenAdapter;

    @Test
    public void lagerForventetArbeidstreningPDF() throws Exception {
        Avtale arbeidstrening = TestData.opprettArbeidstreningAvtale();
        opprettPdfFil(arbeidstrening);
    }

    @Test
    public void lagerForventetMidlertidigLonnstilskuddPDF() throws Exception {
        Avtale lonnstilskudd = TestData.opprettLonnstilskuddsAvtale();
        opprettPdfFil(lonnstilskudd);
    }

    @Test
    public void lagerForventetVarigLonnstilskuddPDF() throws Exception {
        Avtale lonnstilskudd = TestData.opprettLonnstilskuddsAvtale();
        lonnstilskudd.setTiltakstype(Tiltakstype.VARIG_LONNSTILSKUDD);
        opprettPdfFil(lonnstilskudd);
    }

    @Test
    public void lagerForventetMentortilskuddPDF() throws Exception {
        Avtale mentorAvtale = TestData.opprettMentorAvtale();
        opprettPdfFil(mentorAvtale);
    }

    private void opprettPdfFil(Avtale avtale) throws Exception {

        byte[] bytes = dokgenAdapter.genererPdf(avtale);
        PDDocument pdf = PDDocument.load(new ByteArrayInputStream(bytes));
        pdf.save("src/test/resources/pdf/" + avtale.getTiltakstype().getDokforTiltakskodeSkjema() + ".pdf");
        pdf.close();
    }


}
