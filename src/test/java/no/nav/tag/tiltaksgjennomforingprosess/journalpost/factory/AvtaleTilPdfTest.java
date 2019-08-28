package no.nav.tag.tiltaksgjennomforingprosess.journalpost.factory;

import no.nav.tag.tiltaksgjennomforingprosess.TestData;
import no.nav.tag.tiltaksgjennomforingprosess.domene.Avtale;
import no.nav.tag.tiltaksgjennomforingprosess.domene.Maal;
import no.nav.tag.tiltaksgjennomforingprosess.domene.Oppgave;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class AvtaleTilPdfTest {
    private final static String ID_AVTALE = "72c365e7-177a-43ad-9d91-48c6479a6cf0";
    private final static String ID_MAAL_1 = "161de513-9df5-4ea6-b0ac-7d5ce5a02805";
    private final static String ID_MAAL_2 = "eb0f0ceb-4a17-4dca-85f3-13b38fdeebe3";
    private final static String ID_OPPG_1 = "a63ce05a-8337-400a-86d4-3b4a6459e263";
    private final static String ID_OPPG_2 = "8eea897f-40f9-472b-beb1-de64ab632075";

    private AvtaleTilPdf avtaleTilPdf = new AvtaleTilPdf();

    @Test
    public void lagerAvtalePdf() {
        Avtale avtale = TestData.opprettAvtale();
        avtale.setId(UUID.fromString(ID_AVTALE));
//        avtale.getMaal().get(0).setId(UUID.fromString(ID_MAAL_1));
//        avtale.getMaal().get(1).setId(UUID.fromString(ID_MAAL_2));
//        avtale.getOppgaver().get(0).setId(UUID.fromString(ID_OPPG_1));
//        avtale.getOppgaver().get(1).setId(UUID.fromString(ID_OPPG_2));

        String pdfFilNavn = avtaleTilPdf.generererPdf(avtale);
        Assert.assertTrue("Fil ikke eksisteres", sjekkOmPdfFilEksist(pdfFilNavn));
        Assert.assertTrue("Feil innhold i filen", sjekkPdfInnhold(pdfFilNavn, avtale, avtaleTilPdf));
    }

    private boolean sjekkOmPdfFilEksist(String filNavn) {
        try {
            PDDocument doc = PDDocument.load(new File(filNavn));
            if (doc != null) return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean sjekkPdfInnhold(String filNavn, Avtale avtale, AvtaleTilPdf avtaleTilPdf) {
        try {
            PDDocument doc = PDDocument.load(new File(filNavn));
            String textInPdf = new PDFTextStripper().getText(doc);
            return textInPdf.contains(avtale.getId().toString()) && textInPdf.contains(avtale.getDeltakerFnr()) && textInPdf.contains(avtale.getBedriftNr())
                    && textInPdf.contains(avtale.getDeltakerFornavn() + " " + avtale.getDeltakerEtternavn())
                    && textInPdf.contains(avtale.getArbeidsgiverFornavn() + " " + avtale.getArbeidsgiverEtternavn()) && textInPdf.contains(avtale.getArbeidsgiverTlf())
                    && textInPdf.contains(avtale.getVeilederFornavn() + " " + avtale.getVeilederEtternavn())
                    && sjekkPdfOppfolgingInnhold(textInPdf, avtale) && sjekkPdfTilretteleggingInnhold(textInPdf, avtale)
 //                   && textInPdf.contains(avtale.getStartDato().format(DateTimeFormatter.ofPattern(AvtaleTilPdf.DATOFORMAT_NORGE))) && textInPdf.contains(avtale.getArbeidstreningLengde().toString())
                    && textInPdf.contains(avtale.getArbeidstreningStillingprosent().toString())
  //                  && textInPdf.contains(avtale.getGodkjentAvDeltaker().format(DateTimeFormatter.ofPattern(AvtaleTilPdf.DATOFORMAT_NORGE)))
 //                   && textInPdf.contains(avtale.getGodkjentAvArbeidsgiver().format(DateTimeFormatter.ofPattern(AvtaleTilPdf.DATOFORMAT_NORGE)))
 //                   && textInPdf.contains(avtale.getGodkjentAvVeileder().format(DateTimeFormatter.ofPattern(AvtaleTilPdf.DATOFORMAT_NORGE)))
                    && sjekkPdfMaalListInnhold(textInPdf, avtale)
                    && sjekkPdfOppgaveListInnhold(textInPdf, avtale);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

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
