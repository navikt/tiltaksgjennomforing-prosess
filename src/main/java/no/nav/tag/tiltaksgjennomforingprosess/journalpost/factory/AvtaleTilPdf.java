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
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Component
@Data
public class AvtaleTilPdf {
    public final static String DATOFORMAT_NORGE = "dd.MM.YYYY";
    private static int paragraphWidth = 90;
    private static PDFont font = PDType1Font.TIMES_ROMAN;
    private static PDFont font_Bold = PDType1Font.TIMES_BOLD;
    private static int fontSizeSmaa = 10;
    private static int fontSize = 12;
    private static int fontSizeMellomStor = 14;
    private static int fontSizeStor = 18;
    private static int totalSider = 1;
    private static int aktulLinjerISiden = 10;
    private final static int maksLinjerPerSide = 45;
    static int[] startSidenXY = new int[]{50, 700};
    static float leadingNormal = 14f;
    static float leadingSmaa = 1f;
    static float[] logoposition = new float[]{50, 750};
    static float[] logoStorrelse = new float[]{60, 38};
    private static String ikonfil = "navikon.png";

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

            PDImageXObject pdImage = null;
            try {
                pdImage = PDImageXObject.createFromFile(ikonfil, document);
            } catch (NullPointerException e) {
                System.out.println(e.getMessage());
                System.out.println("Fil ikke eksist, logo blir ikke laget");
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                System.out.println("Feil fil format, logo blir ikke laget");
                e.printStackTrace();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Ukjent feil, logo blir ikke laget");
                e.printStackTrace();
            }

            contentStream.drawImage(pdImage, logoposition[0], logoposition[1], logoStorrelse[0], logoStorrelse[1]);
            contentStream.beginText();
            contentStream.newLineAtOffset(startSidenXY[0], startSidenXY[1]);
            contentStream.setLeading(leadingNormal);
            contentStream = skrivTekst("Avtale for arbeidstrening", contentStream, document, font_Bold, fontSizeStor);
            contentStream.setFont(font, fontSize);
            List<Object> text = new ArrayList<>();
            float moveToPositionX = -32000;
            text.add(moveToPositionX);
            text.add("Startdato: " + avtale.getStartDato().format(DateTimeFormatter.ofPattern(DATOFORMAT_NORGE)));
            contentStream.showTextWithPositioning(text.toArray());
            contentStream.newLine();
            text = new ArrayList<>();
            text.add(moveToPositionX);
            text.add("Varighet: " + avtale.getArbeidstreningLengde() + " uker ");
            contentStream.showTextWithPositioning(text.toArray());
            contentStream.newLine();
            contentStream.newLineAtOffset(-0, -30);
            contentStream = startNyttAvsnitt("Avtalens parter", contentStream);
            contentStream = skrivTekst("Deltaker  ", contentStream, document, font, fontSize);
            contentStream = skrivTekst(avtale.getDeltakerFornavn() + " " + avtale.getDeltakerEtternavn(), contentStream, document, font_Bold, fontSize);
            contentStream = skrivTekst("Fødselsnummer: " + avtale.getDeltakerFnr(), contentStream, document, font, fontSize);
            contentStream = skrivTekst("Telefon: " + avtale.getDeltakerTlf(), contentStream, document, font, fontSize);
            contentStream.newLine();
            contentStream = skrivTekst("Arbeidsgiver ", contentStream, document, font, fontSize);
            contentStream = skrivTekst(avtale.getBedriftNavn(), contentStream, document, font_Bold, fontSize);
            contentStream = skrivTekst("BedriftsNr: " + avtale.getBedriftNr(), contentStream, document, font, fontSize);
            contentStream = skrivTekst("Kontakperson: " + avtale.getArbeidsgiverFornavn() + " " + avtale.getArbeidsgiverEtternavn(), contentStream, document, font, fontSize);
            contentStream = skrivTekst("Telefon: " + avtale.getArbeidsgiverTlf(), contentStream, document, font, fontSize);
            contentStream.newLine();
            contentStream = skrivTekst("Nav-veileder ", contentStream, document, font, fontSize);
            contentStream = skrivTekst(avtale.getVeilederFornavn() + " " + avtale.getVeilederEtternavn(), contentStream, document, font_Bold, fontSize);
            contentStream = skrivTekst("Telefon: " + avtale.getVeilederTlf(), contentStream, document, font, fontSize);
            contentStream.newLine();
            contentStream = startNyttAvsnitt("Mål  ", contentStream);

            for (Maal maal : avtale.getMaal()
            ) {
                aktulLinjerISiden++;
                contentStream = skrivTekst(maal.getKategori(), contentStream, document, font_Bold, fontSize);
                String maalBesk = maal.getBeskrivelse();
                contentStream = skrivTekst(maalBesk, contentStream, document, font, fontSize);
                contentStream.newLine();
            }
            contentStream = startNyttAvsnitt("Arbeidsoppgaver ", contentStream);
            for (Oppgave oppgave : avtale.getOppgaver()
            ) {
                contentStream = skrivTekst(oppgave.getTittel(), contentStream, document, font_Bold, fontSize);
                String oppgaveBesk = oppgave.getBeskrivelse();
                contentStream = skrivTekst(oppgaveBesk, contentStream, document, font, fontSize);
                contentStream.newLine();
                contentStream = skrivTekst("Opplæring: ", contentStream, document, font_Bold, fontSize);
                String opplaering = oppgave.getOpplaering();
                contentStream = skrivTekst(opplaering, contentStream, document, font, fontSize);
                contentStream.newLine();
                aktulLinjerISiden += 2;
            }
            contentStream.newLine();
            contentStream = skrivTekst("Startdato: " + avtale.getStartDato().format(DateTimeFormatter.ofPattern(DATOFORMAT_NORGE)), contentStream, document, font, fontSize);
            contentStream = skrivTekst("Varighet: " + avtale.getArbeidstreningLengde() + " uker ", contentStream, document, font, fontSize);
            contentStream = skrivTekst("Stillingsprosent: " + avtale.getArbeidstreningStillingprosent() + "%", contentStream, document, font, fontSize);
            contentStream.newLine();
            contentStream = startNyttAvsnitt("Oppfølging ", contentStream);
            String oppfolging = avtale.getOppfolging();
            contentStream = skrivTekst(oppfolging, contentStream, document, font, fontSize);
            contentStream.newLine();
            aktulLinjerISiden += 2;
            contentStream = startNyttAvsnitt("Tilrettelegging ", contentStream);
            String tilrettelegging = avtale.getTilrettelegging();
            contentStream = skrivTekst(tilrettelegging, contentStream, document, font, fontSize);
            //Vi trenger å sjekke at det er nok plass til Godkjenning i siden, upraktisk at godkjenning blir delt inn 2 sider
            if (aktulLinjerISiden > (maksLinjerPerSide - 10)) {
                contentStream = openNewPage(contentStream, document);
            }
            contentStream.newLine();
            contentStream = startNyttAvsnitt("Godkjenning ", contentStream);
            contentStream = skrivTekst(" Godkjent av deltaker: " + avtale.getGodkjentAvDeltaker().format(DateTimeFormatter.ofPattern(DATOFORMAT_NORGE)), contentStream, document, font, fontSize);
            contentStream = skrivTekst(" Godkjent av ArbeidsGiver: " + avtale.getGodkjentAvArbeidsgiver().format(DateTimeFormatter.ofPattern(DATOFORMAT_NORGE)), contentStream, document, font, fontSize);
            contentStream.showText(" Godkjent av Nav veileder: " + avtale.getGodkjentAvVeileder().format(DateTimeFormatter.ofPattern(DATOFORMAT_NORGE)));
            try {
                if (avtale.isGodkjentPaVegneAv()) {
                    contentStream.newLine();
                    aktulLinjerISiden++;
                    contentStream = skrivTekst("    NB: Avtalen er godkjent av veileder på vegne av deltaker fordi : ", contentStream, document, font, fontSize);
                    if (avtale.getGodkjentPaVegneGrunn().isIkkeBankId()) {
                        contentStream = skrivTekst("     Deltaker ikke har bankID", contentStream, document, font, fontSize);
                    }
                    if (avtale.getGodkjentPaVegneGrunn().isReservert()) {
                        contentStream = skrivTekst("     Deltaker har reservert seg mot digitale tjenester", contentStream, document, font, fontSize);
                    }
                    if (avtale.getGodkjentPaVegneGrunn().isDigitalKompetanse()) {
                        contentStream = skrivTekst("     Deltaker mangler digital kompetanse", contentStream, document, font, fontSize);
                    }
                }
            } catch (Exception e) {
            }
            contentStream = skrivFooter("Referanse:  " + avtale.getId().toString(), contentStream);
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

    List<String> possibleWrapText(String skrivText, PDPage page) throws IOException {


        PDRectangle mediabox = page.getMediaBox();
        float margin = 72;
        float width = mediabox.getWidth() - 2 * margin;

        List<String> lines = new ArrayList<String>();
        int lastSpace = -1;
        while (skrivText.length() > 0) {
            int spaceIndex = skrivText.indexOf(' ', lastSpace + 1);
            if (spaceIndex < 0)
                spaceIndex = skrivText.length();
            String subString = skrivText.substring(0, spaceIndex);
            float size = fontSize * font.getStringWidth(subString) / 1000;
            System.out.printf("'%s' - %f of %f\n", subString, size, width);
            if (size > width) {
                if (lastSpace < 0)
                    lastSpace = spaceIndex;
                subString = skrivText.substring(0, lastSpace);
                lines.add(subString);
                skrivText = skrivText.substring(lastSpace).trim();
                System.out.printf("'%s' is line\n", subString);
                lastSpace = -1;
            } else if (spaceIndex == skrivText.length()) {
                lines.add(skrivText);
                System.out.printf("'%s' is line\n", skrivText);
                skrivText = "";
            } else {
                lastSpace = spaceIndex;
            }
        }

        return lines;
    }

    public PDPageContentStream openNewPage(PDPageContentStream contentStream, PDDocument document) throws IOException {
        contentStream.endText();
        contentStream.close();
        PDPage new_Page = new PDPage(PDRectangle.A4);
        document.addPage(new_Page);
        contentStream = new PDPageContentStream(document, new_Page);
        contentStream.setFont(font, fontSize);
        contentStream.beginText();
        contentStream.newLineAtOffset(startSidenXY[0], logoposition[1]);
        contentStream.setLeading(leadingNormal);
        totalSider++;
        aktulLinjerISiden = 0;
        return contentStream;
    }


    /**
     * Skriver korte og lange String som kan innholde lange tekster med flere linjer og oppretter ny side om det trengs
     * Lager nylinje og øke aktuellLinjerISiden
     *
     * @param skrivTekst    tekst til skriving
     * @param contentStream bruk samme contentStream for å roftsette med samme context
     * @param document      document til å skrive inn
     * @return Må bruke returnert contentStream for å fortsette i samme rekkefølge
     * @throws IOException
     */
    private PDPageContentStream skrivTekst(String skrivTekst, PDPageContentStream contentStream, PDDocument document, PDFont fontIBruk, int fontSizeIBruk) throws IOException {
        for (String lineText : possibleWrapText(skrivTekst, new PDPage(PDRectangle.A4))
        ) {
            try {
                aktulLinjerISiden++;
                if (aktulLinjerISiden > maksLinjerPerSide) {
                    contentStream = openNewPage(contentStream, document);
                }
                contentStream.setFont(fontIBruk, fontSizeIBruk);
                contentStream.showText(lineText + "");
                contentStream.newLine();
            } catch (Exception e) {
                System.out.println(lineText);
                e.printStackTrace();
            }
        }
        return contentStream;
    }

    private PDPageContentStream startNyttAvsnitt(String avsnitt, PDPageContentStream contentStream) throws IOException {
        contentStream.newLine();
        contentStream.setFont(font_Bold, fontSizeMellomStor);
        contentStream.showText(avsnitt);
        contentStream.setLeading(leadingSmaa);
        contentStream.newLine();
        contentStream.showText("_________________________________________________________________________________");
        contentStream.setLeading(leadingNormal);
        contentStream.newLine();
        contentStream.newLine();
        aktulLinjerISiden += 3;
        return contentStream;
    }

    private PDPageContentStream skrivFooter(String footer, PDPageContentStream contentStream) throws IOException {
        while (aktulLinjerISiden < 45) {
            contentStream.newLine();
            aktulLinjerISiden++;
        }
        contentStream.showText("_________________________________________________________________________________");
        contentStream.newLine();
        Color fontColor = Color.gray;
        contentStream.setFont(font, fontSizeSmaa);
        contentStream.setNonStrokingColor(fontColor);
        contentStream.showText(footer);

        return contentStream;
    }
}
