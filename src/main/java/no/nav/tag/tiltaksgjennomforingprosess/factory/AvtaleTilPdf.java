package no.nav.tag.tiltaksgjennomforingprosess.factory;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tiltaksgjennomforingprosess.domene.avtale.Avtale;
import no.nav.tag.tiltaksgjennomforingprosess.domene.avtale.Maal;
import no.nav.tag.tiltaksgjennomforingprosess.domene.avtale.Oppgave;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Data
@Slf4j
class AvtaleTilPdf {

    private static final String PA_VEGNE_GRUNN_TXT = "    NB: Avtalen er godkjent av veileder på vegne av deltaker fordi : ";
    private static final String IKKE_BANKID_TXT = "     Deltaker ikke har BankID";
    private static final String RESERVERT_TXT = "     Deltaker har reservert seg mot digitale tjenester";
    private static final String DIGITAL_KOMPETANSE_TXT = "     Deltaker mangler digital kompetanse";
    private static final String LINJE = "_________________________________________________________________________________";
    private static final String REGEX_NY_LINJE = "\n";
    private static final String REGEX_TAB = "\t";
    private static final int FONT_SIZE_SMAA = 10;
    private static final int FONT_SIZE = 12;
    private static final int FONT_SIZE_MELLOM_STOR = 14;
    private static final int FONT_SIZE_STOR = 18;
    private static final int MAKS_LINJER_PER_SIDE = 45;
    private static final int[] START_SIDEN_XY = new int[]{50, 700};
    private static final float LEADING_NORMAL = 14f;
    private static final float LEADING_LITEN = 1f;
    private static final float[] LOGO_POSITION = new float[]{50, 750};
    private static final float[] LOGO_STORRELSE = new float[]{60, 38};
    private static final String IKONFIL = "pdf/navikon.png";

    static final String DATOFORMAT_NORGE = "dd.MM.YYYY";
    private static final DateTimeFormatter NORSK_DATO_FORMATTER = DateTimeFormatter.ofPattern(DATOFORMAT_NORGE);

    private static PDFont font;
    private static PDFont fontBold;

    private int aktuelleLinjerPåSiden = 10;
    private int totalSider = 1;
    private PDDocument document;

    private final InputStream fontIS = AvtaleTilPdf.class.getResourceAsStream("/pdf/times_new_roman.ttf");
    private final InputStream fontBoldIS = AvtaleTilPdf.class.getResourceAsStream("/pdf/times_new_roman_bold.ttf");


    AvtaleTilPdf() {
        document = new PDDocument();
        try {
            font = PDType0Font.load(document, fontIS, true);
            fontBold = PDType0Font.load(document, fontBoldIS, true);
        } catch (IOException e) {
            throw new RuntimeException("Feil ved generering av PDF-fil: " + e.getMessage());
        }
    }

    byte[] tilBytesAvPdf(Avtale avtale) {
        PDDocument dokument = generererPdf(avtale);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            dokument.save(baos);
        } catch (IOException e) {
            log.error("Feil oppstod ved generering av avtale " + avtale.getAvtaleId(), e);
            throw new RuntimeException("Feil ved generering av PDF-fil", e);
        } finally {
            try {
                dokument.close();
            } catch (IOException e) {
            }
        }
        return baos.toByteArray();
    }

    @SuppressWarnings("resource")
    // En smule hæckete håndtering av stream i denne klassen, men ressurser blir som regel lukket
    private PDDocument generererPdf(Avtale avtale) {
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        PDImageXObject pdImage = null;
        try {
            byte[] iconBytes = getClass().getClassLoader().getResourceAsStream(IKONFIL).readAllBytes();
            pdImage = PDImageXObject.createFromByteArray(document, iconBytes, IKONFIL);
        } catch (Exception e) {
            log.error("Feil ved generering av PDF-fil format, logo blir ikke laget: {}", e.getMessage());
        }

        try {
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            if (pdImage != null) {
                contentStream.drawImage(pdImage, LOGO_POSITION[0], LOGO_POSITION[1], LOGO_STORRELSE[0], LOGO_STORRELSE[1]);
            }

            contentStream.beginText();
            contentStream.newLineAtOffset(START_SIDEN_XY[0], START_SIDEN_XY[1]);
            contentStream.setLeading(LEADING_NORMAL);
            contentStream = skrivTekst(avtale.erNyVersjon() ? "Avtale om arbeidstrening (versjon " + avtale.getVersjon() + ")" : "Avtale om arbeidstrening", contentStream, document, fontBold, FONT_SIZE_STOR);
            contentStream.setFont(font, FONT_SIZE);
            List<Object> text = new ArrayList<>();
            float moveToPositionX = -32000;
            text.add(moveToPositionX);
            text.add("Startdato: " + avtale.getStartDato().format(NORSK_DATO_FORMATTER));
            contentStream.showTextWithPositioning(text.toArray());
            contentStream.newLine();
            text = new ArrayList<>();
            text.add(moveToPositionX);
            text.add("Sluttdato: " + avtale.getSluttDato().format(NORSK_DATO_FORMATTER));
            contentStream.showTextWithPositioning(text.toArray());
            contentStream.newLine();
            contentStream.newLineAtOffset(-0, -30);
            startNyttAvsnitt("Avtalens parter", contentStream);
            contentStream = skrivTekst("Deltaker  ", contentStream, document, font, FONT_SIZE);
            contentStream = skrivTekst(avtale.getDeltakerFornavn() + " " + avtale.getDeltakerEtternavn(), contentStream, document, fontBold, FONT_SIZE);
            contentStream = skrivTekst("Fødselsnummer: " + avtale.getDeltakerFnr(), contentStream, document, font, FONT_SIZE);
            contentStream = skrivTekst("Telefon: " + blankForNull(avtale.getDeltakerTlf()), contentStream, document, font, FONT_SIZE);
            contentStream.newLine();
            contentStream = skrivTekst("Arbeidsgiver ", contentStream, document, font, FONT_SIZE);
            contentStream = skrivTekst(avtale.getBedriftNavn(), contentStream, document, fontBold, FONT_SIZE);
            contentStream = skrivTekst("Bedriftnr.: " + avtale.getBedriftNr(), contentStream, document, font, FONT_SIZE);
            contentStream = skrivTekst("Kontakperson: " + avtale.getArbeidsgiverFornavn() + " " + avtale.getArbeidsgiverEtternavn(), contentStream, document, font, FONT_SIZE);
            contentStream = skrivTekst("Telefon: " + blankForNull(avtale.getArbeidsgiverTlf()), contentStream, document, font, FONT_SIZE);
            contentStream.newLine();
            contentStream = skrivTekst("NAV-veileder ", contentStream, document, font, FONT_SIZE);
            contentStream = skrivTekst(avtale.getVeilederFornavn() + " " + avtale.getVeilederEtternavn(), contentStream, document, fontBold, FONT_SIZE);
            contentStream = skrivTekst("Telefon: " + blankForNull(avtale.getVeilederTlf()), contentStream, document, font, FONT_SIZE);
            contentStream.newLine();
            startNyttAvsnitt("Mål  ", contentStream);

            for (Maal maal : avtale.getMaal()
            ) {
                aktuelleLinjerPåSiden++;
                contentStream = skrivTekst("Kategori", contentStream, document, fontBold, FONT_SIZE);
                contentStream = skrivTekst(maal.getKategori(), contentStream, document, font, FONT_SIZE);
                contentStream = skrivTekst("Beskrivelse", contentStream, document, fontBold, FONT_SIZE);
                contentStream = skrivFritekstTilPdf(contentStream, maal.getBeskrivelse());
            }
            startNyttAvsnitt("Arbeidsoppgaver ", contentStream);
            for (Oppgave oppgave : avtale.getOppgaver()
            ) {
                contentStream = skrivTekst("Tittel", contentStream, document, fontBold, FONT_SIZE);
                contentStream = skrivFritekstTilPdf(contentStream, oppgave.getTittel());

                contentStream.newLine();
                contentStream = skrivTekst("Beskrivelse: ", contentStream, document, fontBold, FONT_SIZE);
                contentStream = skrivFritekstTilPdf(contentStream, oppgave.getBeskrivelse());

                contentStream.newLine();
                contentStream = skrivTekst("Opplæring: ", contentStream, document, fontBold, FONT_SIZE);
                contentStream = skrivFritekstTilPdf(contentStream, blankForNull(oppgave.getOpplaering()));
                contentStream.newLine();
                aktuelleLinjerPåSiden += 2;
            }
            contentStream.newLine();
            contentStream = skrivTekst("Sluttdato: " + avtale.getSluttDato().format(NORSK_DATO_FORMATTER), contentStream, document, font, FONT_SIZE);
            contentStream = skrivTekst("Stillingsprosent: " + avtale.getStillingprosent() + "%", contentStream, document, font, FONT_SIZE);
            contentStream.newLine();
            startNyttAvsnitt("Oppfølging ", contentStream);
            contentStream = skrivFritekstTilPdf(contentStream, avtale.getOppfolging());
            aktuelleLinjerPåSiden += 2;
            startNyttAvsnitt("Tilrettelegging ", contentStream);
            contentStream = skrivFritekstTilPdf(contentStream, avtale.getTilrettelegging());
            //Vi trenger å sjekke at det er nok plass til Godkjenning i siden, upraktisk at godkjenning blir delt inn 2 sider
            if (aktuelleLinjerPåSiden > (MAKS_LINJER_PER_SIDE - 10)) {
                contentStream = openNewPage(contentStream, document);
            }
            contentStream.newLine();
            startNyttAvsnitt("Godkjenning ", contentStream);
            contentStream = skrivTekst(" Godkjent av deltaker: " + avtale.getGodkjentAvDeltaker().format(NORSK_DATO_FORMATTER), contentStream, document, font, FONT_SIZE);
            contentStream = skrivTekst(" Godkjent av arbeidsgiver: " + avtale.getGodkjentAvArbeidsgiver().format(NORSK_DATO_FORMATTER), contentStream, document, font, FONT_SIZE);
            contentStream.showText(" Godkjent av NAV-veileder: " + avtale.getGodkjentAvVeileder().format(NORSK_DATO_FORMATTER));
            try {
                if (avtale.isGodkjentPaVegneAv()) {
                    contentStream.newLine();
                    aktuelleLinjerPåSiden++;
                    contentStream = skrivTekst(PA_VEGNE_GRUNN_TXT, contentStream, document, font, FONT_SIZE);
                    if (avtale.getGodkjentPaVegneGrunn().isIkkeBankId()) {
                        contentStream = skrivTekst(IKKE_BANKID_TXT, contentStream, document, font, FONT_SIZE);
                    }
                    if (avtale.getGodkjentPaVegneGrunn().isReservert()) {
                        contentStream = skrivTekst(RESERVERT_TXT, contentStream, document, font, FONT_SIZE);
                    }
                    if (avtale.getGodkjentPaVegneGrunn().isDigitalKompetanse()) {
                        contentStream = skrivTekst(DIGITAL_KOMPETANSE_TXT, contentStream, document, font, FONT_SIZE);
                    }
                }
            } catch (Exception e) {
                log.error("Kan være avtale fra gamle versjon som mangler mulighet for godkjenning på vegne av " + e.getMessage());
            }
            skrivFooter("Referanse:  " + avtale.getAvtaleId().toString(), contentStream);
            contentStream.endText();
            contentStream.close();

        } catch (Exception e) {
            throw new RuntimeException("Feil ved generering av PDF-fil", e);
        }
        return document;
    }

    List<String> possibleWrapText(String skrivText, PDPage page) throws IOException {

        PDRectangle mediabox = page.getMediaBox();
        final float margin = 72;
        final float width = mediabox.getWidth() - 2 * margin;

        List<String> lines = new ArrayList<>();
        int lastSpace = -1;
        while (skrivText.length() > 0) {
            int spaceIndex = skrivText.indexOf(' ', lastSpace + 1);
            if (spaceIndex < 0) {
                spaceIndex = skrivText.length();
            }
            String subString = skrivText.substring(0, spaceIndex);
            float size = FONT_SIZE * font.getStringWidth(subString) / 1000;
            if (size > width) {
                if (lastSpace < 0) {
                    lastSpace = spaceIndex;
                }
                subString = skrivText.substring(0, lastSpace);
                lines.add(subString);
                skrivText = skrivText.substring(lastSpace).trim();
                lastSpace = -1;
            } else if (spaceIndex == skrivText.length()) {
                lines.add(skrivText);
                skrivText = "";
            } else {
                lastSpace = spaceIndex;
            }
        }
        return lines;
    }

    private PDPageContentStream skrivFritekstTilPdf(PDPageContentStream contentStream, String fritekst) throws Exception {
        String[] linjer = fritekst.replace(REGEX_TAB, "  ").split(REGEX_NY_LINJE);
        for (String linje : linjer) {
            contentStream = skrivTekst(linje.trim(), contentStream, document, font, FONT_SIZE);
        }
        return contentStream;
    }

    private PDPageContentStream openNewPage(PDPageContentStream contentStream, PDDocument document) throws IOException {
        contentStream.endText();
        contentStream.close();
        PDPage new_Page = new PDPage(PDRectangle.A4);
        document.addPage(new_Page);
        contentStream = new PDPageContentStream(document, new_Page);
        contentStream.setFont(font, FONT_SIZE);
        contentStream.beginText();
        contentStream.newLineAtOffset(START_SIDEN_XY[0], LOGO_POSITION[1]);
        contentStream.setLeading(LEADING_NORMAL);
        totalSider++;
        aktuelleLinjerPåSiden = 0;
        return contentStream;
    }

    private PDPageContentStream skrivTekst(String skrivTekst, PDPageContentStream contentStream, PDDocument document, PDFont fontIBruk, int fontSizeIBruk) throws Exception {
        for (String lineText : possibleWrapText(skrivTekst, new PDPage(PDRectangle.A4))) {
            aktuelleLinjerPåSiden = Integer.sum(aktuelleLinjerPåSiden, 1);
            if (aktuelleLinjerPåSiden > MAKS_LINJER_PER_SIDE) {
                contentStream = openNewPage(contentStream, document);
            }
            contentStream.setFont(fontIBruk, fontSizeIBruk);
            contentStream.showText(lineText + "");
            contentStream.newLine();
        }
        return contentStream;
    }

    private void startNyttAvsnitt(String avsnitt, PDPageContentStream contentStream) throws IOException {
        contentStream.newLine();
        contentStream.setFont(fontBold, FONT_SIZE_MELLOM_STOR);
        contentStream.showText(avsnitt);
        contentStream.setLeading(LEADING_LITEN);
        contentStream.newLine();
        contentStream.showText(LINJE);
        contentStream.setLeading(LEADING_NORMAL);
        contentStream.newLine();
        contentStream.newLine();
        aktuelleLinjerPåSiden += 3;
    }

    private void skrivFooter(String footer, PDPageContentStream contentStream) throws IOException {
        while (aktuelleLinjerPåSiden < 45) {
            contentStream.newLine();
            aktuelleLinjerPåSiden++;
        }
        contentStream.showText(LINJE);
        contentStream.newLine();
        Color fontColor = Color.gray;
        contentStream.setFont(font, FONT_SIZE_SMAA);
        contentStream.setNonStrokingColor(fontColor);
        contentStream.showText(footer);
    }

    private static String blankForNull(String muligNullVerdi) {
        return muligNullVerdi == null ? "" : muligNullVerdi;
    }
}
