package no.nav.tag.tiltaksgjennomforingprosess.factory;

import no.nav.tag.tiltaksgjennomforingprosess.TestData;
import no.nav.tag.tiltaksgjennomforingprosess.domene.avtale.Avtale;
import no.nav.tag.tiltaksgjennomforingprosess.integrasjon.DokgenAdapter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayInputStream;

@Ignore("Til manuell sjekk av pdf layout")
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
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
    public void lagerForventetLonnstilskuddPDF() throws Exception {
        Avtale lonnstilskudd = TestData.opprettLonnstilskuddsAvtale();
        opprettPdfFil(lonnstilskudd);
    }

    @Test
    public void lagerForventetPDFAvGammelPDFGen() throws Exception {
        Avtale arbeidstrening = TestData.opprettArbeidstreningAvtale();
        byte[] bytes = new AvtaleTilPdf().tilBytesAvPdf(arbeidstrening);

        PDDocument pdf = PDDocument.load(new ByteArrayInputStream(bytes));
        pdf.save("src/test/resources/PDF/gammel-pdfGen.pdf");
        pdf.close();
    }

    private void opprettPdfFil(Avtale avtale) throws Exception {

        byte[] bytes = dokgenAdapter.genererPdf(avtale);
        PDDocument pdf = PDDocument.load(new ByteArrayInputStream(bytes));
        pdf.save("src/test/resources/PDF/" + avtale.getTiltakstype().getTiltaksType() +".pdf");
        pdf.close();
    }


}
