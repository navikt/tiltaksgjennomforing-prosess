package no.nav.tag.tiltaksgjennomforingprosess.journalpost.factory;

import lombok.Data;
import no.nav.tag.tiltaksgjennomforingprosess.domene.Avtale;
import no.nav.tag.tiltaksgjennomforingprosess.domene.Maal;
import no.nav.tag.tiltaksgjennomforingprosess.domene.Oppgave;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@Data
public class AvtaleTilPdf {
    private final String DATOFORMAT_NORGE = "dd.MM.YYYY";
    private static int paragraphWidth = 70;
    private PDFont font = PDType1Font.COURIER;
    private PDFont font_Bold = PDType1Font.COURIER_BOLD;
    private int fontSize = 12;
    private int totalSider = 1;
    private int aktulLinjerISiden = 20;
    private final int maksLinjerPerSide = 45;
    int[] startSidenXY = new int[]{100, 700};
    float leadingNormal = 14f;
    float leadingSmaa = 1f;

    /**
     * Genrerer PDF fil fra sendte avtale.
     *
     * @param avtale
     * @return filNavn
     */
    public String generererPdf(Avtale avtale) {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);
        try {
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.setFont(font_Bold, fontSize);
            contentStream.beginText();
            contentStream.newLineAtOffset(startSidenXY[0], startSidenXY[1]);
            contentStream.setLeading(leadingNormal);
            contentStream.showText("Avtale for arbeidsTrening");
            contentStream.newLine();
            contentStream.setFont(font, fontSize);
            contentStream.showText("Avtale Nr:  " + avtale.getId().toString());
            contentStream.showText(" Version Nr: " + avtale.getVersjon().toString());
            contentStream.newLine();
            contentStream.newLineAtOffset(-50, -30);
            contentStream.setFont(font_Bold, fontSize);
            contentStream.showText("Avtalens parter");
            contentStream.setLeading(leadingSmaa);
            contentStream.newLine();
            contentStream.showText("_________________________________________________________________________________");
            contentStream.setLeading(leadingNormal);
            contentStream.newLine();
            contentStream.setFont(font, fontSize);
            contentStream.showText("Deltaker  ");
            contentStream.newLine();
            contentStream.setFont(font_Bold, fontSize);
            contentStream.showText(avtale.getDeltakerFornavn() + " " + avtale.getDeltakerEtternavn());
            contentStream.newLine();
            contentStream.setFont(font, fontSize);
            contentStream.showText("Fødselsnummer: " + avtale.getDeltakerFnr());
            contentStream.newLine();
            contentStream.showText("Telefon: " + avtale.getDeltakerTlf());
            contentStream.newLine();
            contentStream.showText("Arbeidsgiver ");
            contentStream.newLine();
            contentStream.setFont(font_Bold, fontSize);
            contentStream.showText(avtale.getBedriftNavn());
            contentStream.newLine();
            contentStream.setFont(font, fontSize);
            contentStream.showText("BedriftsNr: " + avtale.getBedriftNr());
            contentStream.newLine();
            contentStream.showText("Kontakperson: " + avtale.getArbeidsgiverFornavn() + " " + avtale.getArbeidsgiverEtternavn());
            contentStream.newLine();
            contentStream.showText("Telefon: " + avtale.getArbeidsgiverTlf());
            contentStream.newLine();
            contentStream.showText("Nav-veileder ");
            contentStream.newLine();
            contentStream.setFont(font_Bold, fontSize);
            contentStream.showText(avtale.getVeilederFornavn() + " " + avtale.getVeilederEtternavn());
            contentStream.setFont(font, fontSize);
            contentStream.newLine();
            contentStream.showText("Telefon: " + avtale.getVeilederTlf());
            contentStream.newLine();
            contentStream.newLine();
            contentStream.setFont(font_Bold, fontSize);
            contentStream.showText("Mål: ");
            contentStream.setLeading(leadingSmaa);
            contentStream.newLine();
            contentStream.showText("_________________________________________________________________________________");
            contentStream.setLeading(leadingNormal);
            contentStream.newLine();

            for (Maal maal : avtale.getMaal()
            ) {
                aktulLinjerISiden++;
                contentStream.setFont(font_Bold, fontSize);
                contentStream.showText(maal.getKategori());
                contentStream.newLine();
                contentStream.setFont(font, fontSize);
                String maalBesk = maal.getBeskrivelse();
                contentStream = skrivTekst(maalBesk, contentStream, document);
            }
            contentStream.newLine();
            aktulLinjerISiden++;
            contentStream.setFont(font_Bold, fontSize);
            contentStream.showText("Arbeidsoppgaver: ");
            contentStream.setLeading(leadingSmaa);
            contentStream.newLine();
            aktulLinjerISiden++;
            contentStream.showText("_________________________________________________________________________________");
            contentStream.setLeading(leadingNormal);

            contentStream.newLine();
            for (Oppgave oppgave : avtale.getOppgaver()
            ) {
                contentStream.setFont(font_Bold, fontSize);
                contentStream.showText(oppgave.getTittel());
                contentStream.setFont(font, fontSize);
                contentStream.newLine();
                String oppgaveBesk = oppgave.getBeskrivelse();
                contentStream = skrivTekst(oppgaveBesk, contentStream, document);
                contentStream.showText("Opplæring i abeidsoppgave: ");
                contentStream.newLine();
                String opplaering = oppgave.getOpplaering();
                contentStream = skrivTekst(opplaering, contentStream, document);
            }
            contentStream.showText("Start Dato " + avtale.getStartDato().format(DateTimeFormatter.ofPattern(DATOFORMAT_NORGE)) + ", Varighet " + avtale.getArbeidstreningLengde() + " uker, " +
                    "Stillingsprosent " + avtale.getArbeidstreningStillingprosent() + "%");
            contentStream.newLine();
            contentStream.setFont(font_Bold, fontSize);
            contentStream.showText("Oppfølging ");
            contentStream.setLeading(leadingSmaa);
            contentStream.setFont(font, fontSize);
            contentStream.newLine();
            contentStream.showText("_________________________________________________________________________________");
            contentStream.setLeading(leadingNormal);
            contentStream.newLine();
            String oppfolging = avtale.getOppfolging();
            contentStream = skrivTekst(oppfolging, contentStream, document);
            contentStream.setFont(font_Bold, fontSize);
            contentStream.showText("Tilrettelegging ");
            contentStream.setLeading(leadingSmaa);
            contentStream.setFont(font,fontSize);
            contentStream.newLine();
            contentStream.showText("_________________________________________________________________________________");
            contentStream.setLeading(leadingNormal);
            contentStream.newLine();
            String tilrettelegging = avtale.getTilrettelegging();
            contentStream = skrivTekst(tilrettelegging, contentStream, document);
            //Vi trenger å sjekke at det er nok plass til Godkjenning i siden, upraktisk at godkjenning blir delt inn 2 sider
            if (aktulLinjerISiden > (maksLinjerPerSide - 10)) {
                contentStream = openNewPage(contentStream, document);
            }
            contentStream.newLine();
            contentStream.showText(" Godkjent av deltaker: " + avtale.getGodkjentAvDeltaker().format(DateTimeFormatter.ofPattern(DATOFORMAT_NORGE)));
            contentStream.newLine();
            contentStream.showText(" Godkjent av ArbeidsGiver: " + avtale.getGodkjentAvArbeidsgiver().format(DateTimeFormatter.ofPattern(DATOFORMAT_NORGE)));
            contentStream.newLine();
            contentStream.showText(" Godkjent av Nav veileder: " + avtale.getGodkjentAvVeileder().format(DateTimeFormatter.ofPattern(DATOFORMAT_NORGE)));
            try {
                if (avtale.isGodkjentPaVegneAv()) {
                    contentStream.newLine();
                    contentStream.showText("    NB: Avtalen er godkjent av veileder på vegne av deltaker fordi : ");
                    contentStream.newLine();
                    if (avtale.getGodkjentPaVegneGrunn().isIkkeBankId()) {
                        contentStream.showText("     Deltaker ikke har bankID");
                        contentStream.newLine();
                    }
                    if (avtale.getGodkjentPaVegneGrunn().isReservert()) {
                        contentStream.showText("     Deltaker har reservert seg mot digitale tjenester");
                        contentStream.newLine();
                    }
                    if (avtale.getGodkjentPaVegneGrunn().isDigitalKompetanse()) {
                        contentStream.showText("     Deltaker ikke har digital kompetanse");
                        contentStream.newLine();
                    }
                }
            } catch (Exception e) {
            }
            contentStream.endText();
            contentStream.close();
            String filNavn = "avtaleNr" + avtale.getId() + ".pdf";
            document.save(filNavn);
            document.close();
            return filNavn;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    static List<String> possibleWrapText(String text) {
        List<String> lineText = new ArrayList<>();
        while (text.length() > paragraphWidth) {
            lineText.addAll(List.of(text.substring(0, paragraphWidth).split("\n")));
            text = text.substring(paragraphWidth);
        }
        lineText.add(text.trim());
        return lineText;
    }

    public PDPageContentStream openNewPage(PDPageContentStream contentStream, PDDocument document) throws IOException {
        contentStream.endText();
        contentStream.close();
        PDPage new_Page = new PDPage(PDRectangle.A4);
        document.addPage(new_Page);
        contentStream = new PDPageContentStream(document, new_Page);
        contentStream.setFont(font, fontSize);
        contentStream.beginText();
        contentStream.newLineAtOffset(startSidenXY[0] / 2, startSidenXY[1]);
        contentStream.setLeading(leadingNormal);
        totalSider++;
        aktulLinjerISiden = 0;
        return contentStream;
    }

    public PDPageContentStream skrivFlereLinjer(String skrivTekst, PDPageContentStream contentStream, PDDocument document) throws IOException {
        for (String lineText : possibleWrapText(skrivTekst)
        ) {
            try {
                aktulLinjerISiden++;
                if (aktulLinjerISiden > maksLinjerPerSide) {
                    contentStream = openNewPage(contentStream, document);
                }
                contentStream.showText(lineText + "-");
                contentStream.newLine();
            } catch (Exception e) {
                System.out.println(lineText);
                e.printStackTrace();
            }
        }
        return contentStream;
    }

    /**
     * Skriver String som kan innholde lange tekster med flere linjer og oppretter ny side om det trengs
     *
     * @param skrivText     tekst til skriving
     * @param contentStream bruk samme contentStream for å roftsette med samme context
     * @param document      document til å skrive inn
     * @return Må bruke returnert contentStream for å fortsette i samme rekkefølge
     * @throws IOException
     */
    private PDPageContentStream skrivTekst(String skrivText, PDPageContentStream contentStream, PDDocument document) throws IOException {
        if (skrivText.length() < paragraphWidth) {
            contentStream.showText(skrivText);
            contentStream.newLine();
        } else {
            contentStream = skrivFlereLinjer(skrivText, contentStream, document);
        }
        return contentStream;
    }
}
