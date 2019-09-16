package no.nav.tag.tiltaksgjennomforingprosess.factory;

import no.nav.tag.tiltaksgjennomforingprosess.TestData;
import no.nav.tag.tiltaksgjennomforingprosess.domene.avtale.Avtale;
import no.nav.tag.tiltaksgjennomforingprosess.domene.avtale.Maal;
import no.nav.tag.tiltaksgjennomforingprosess.domene.avtale.Oppgave;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.Ignore;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.UUID;

import static junit.framework.TestCase.assertTrue;

public class AvtaleTilPdfTest {
    private final static String ID_AVTALE = "72c365e7-177a-43ad-9d91-48c6479a6cf0";

    private AvtaleTilPdf avtaleTilPdf = new AvtaleTilPdf();

    @Ignore("Til manuell sjekk av pdf layout")
    @Test
    public void lagerForventetPDF() throws Exception {
        Avtale avtale = TestData.opprettAvtale();

        byte[] bytes = avtaleTilPdf.tilBytesAvPdf(avtale);

        String encAvtale = Base64.getEncoder().encodeToString(bytes);
        byte[] decAvtale = Base64.getDecoder().decode(encAvtale);

        PDDocument pdf = PDDocument.load(new ByteArrayInputStream(decAvtale));
        pdf.save("src/test/resources/Resultat.pdf");
        pdf.close();
    }

    @Test
    public void lagerAvtalePdf() throws IOException {
        Avtale avtale = TestData.opprettAvtale();
        avtale.setId(UUID.fromString(ID_AVTALE));

        byte[] bytes = avtaleTilPdf.tilBytesAvPdf(avtale);
        PDDocument dokument = PDDocument.load(bytes);
        sjekkPdfInnhold(avtale, dokument);
    }

    private void sjekkPdfInnhold(Avtale avtale, PDDocument dokument) throws IOException {
            String textInPdf = new PDFTextStripper().getText(dokument);
            boolean harAlt = textInPdf.contains(avtale.getId().toString()) && textInPdf.contains(avtale.getDeltakerFnr()) && textInPdf.contains(avtale.getBedriftNr())
                    && textInPdf.contains(avtale.getDeltakerFornavn() + " " + avtale.getDeltakerEtternavn())
                    && textInPdf.contains(avtale.getArbeidsgiverFornavn() + " " + avtale.getArbeidsgiverEtternavn()) && textInPdf.contains(avtale.getArbeidsgiverTlf())
                    && textInPdf.contains(avtale.getVeilederFornavn() + " " + avtale.getVeilederEtternavn())
                    && sjekkPdfOppfolgingInnhold(textInPdf, avtale) && sjekkPdfTilretteleggingInnhold(textInPdf, avtale)
                    && textInPdf.contains(avtale.getArbeidstreningLengde().toString())
                    && textInPdf.contains(avtale.getArbeidstreningStillingprosent().toString())
                    && sjekkPdfMaalListInnhold(textInPdf, avtale)
                    && sjekkPdfOppgaveListInnhold(textInPdf, avtale);
            assertTrue(harAlt);
            assertTrue("StartDato",textInPdf.contains(avtale.getStartDato().format(DateTimeFormatter.ofPattern(Avtale.DATOFORMAT_NORGE))));
            assertTrue("GodkjentAvDeltaker", textInPdf.contains(avtale.getGodkjentAvDeltaker()));
            assertTrue("GodkjentAvArbeidsgiver", textInPdf.contains(avtale.getGodkjentAvArbeidsgiver()));
            assertTrue("GodkjentAvVeileder", textInPdf.contains(avtale.getGodkjentAvVeileder()));
    }

    private boolean sjekkPdfMaalListInnhold(String textInPdf, Avtale avtale) throws IOException {
        boolean result = true;
        AvtaleTilPdf avtaleTilPdf = new AvtaleTilPdf();
        for (Maal maal : avtale.getMaal()
        ) {
            result = result && textInPdf.contains(maal.getKategori());
            for (String str : avtaleTilPdf.possibleWrapText(maal.getBeskrivelse(), new PDPage(PDRectangle.A4))
            ) {
                result = result && textInPdf.contains(str);
            }
        }
        return result;
    }

    private boolean sjekkPdfOppgaveListInnhold(String textInPdf, Avtale avtale) throws IOException {
        boolean result = true;
        AvtaleTilPdf avtaleTilPdf = new AvtaleTilPdf();
        for (Oppgave oppgave : avtale.getOppgaver()
        ) {
            result = result && textInPdf.contains(oppgave.getTittel());
            for (String str : avtaleTilPdf.possibleWrapText(oppgave.getBeskrivelse(), new PDPage(PDRectangle.A4))
            ) {
                result = result && textInPdf.contains(str);
            }
            for (String str : avtaleTilPdf.possibleWrapText(oppgave.getOpplaering(), new PDPage(PDRectangle.A4))
            ) {

                result = result && textInPdf.contains(str);
            }

        }
        return result;
    }

    private boolean sjekkPdfOppfolgingInnhold(String textInPdf, Avtale avtale) throws IOException {
        boolean result = true;
        AvtaleTilPdf avtaleTilPdf = new AvtaleTilPdf();
        for (String str : avtaleTilPdf.possibleWrapText(avtale.getOppfolging(), new PDPage(PDRectangle.A4))
        ) {
            result = result && textInPdf.contains(str);
        }
        return result;
    }

    private boolean sjekkPdfTilretteleggingInnhold(String textInPdf, Avtale avtale) throws IOException {
        boolean result = true;
        AvtaleTilPdf avtaleTilPdf = new AvtaleTilPdf();
        for (String str : avtaleTilPdf.possibleWrapText(avtale.getOppfolging(), new PDPage(PDRectangle.A4))
        ) {
            result = result && textInPdf.contains(str);
        }
        return result;
    }
}
