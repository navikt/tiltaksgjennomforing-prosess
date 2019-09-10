package no.nav.tag.tiltaksgjennomforingprosess.journalpost.factory;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpServerErrorException;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static no.nav.tag.tiltaksgjennomforingprosess.domene.Avtale.DATOFORMAT_NORGE;


@Data
@Slf4j
class AvtaleTilPdf {
    private final static String PA_VEGNE_GRUNN_TXT = "    NB: Avtalen er godkjent av veileder på vegne av deltaker fordi : ";
    private final static String IKKE_BANKID_TXT = "     Deltaker ikke har bankID";
    private final static String RESERVERT_TXT = "     Deltaker har reservert seg mot digitale tjenester";
    private final static String DIGITA_KOMPETANSE_TXT = "     Deltaker mangler digital kompetanse";
    private final static String LINJE = "_________________________________________________________________________________";

    private final static int paragraphWidth = 90;
    private final static PDFont font = PDType1Font.TIMES_ROMAN;
    private final static PDFont font_Bold = PDType1Font.TIMES_BOLD;
    private final static int fontSizeSmaa = 10;
    private final static int fontSize = 12;
    private final static int fontSizeMellomStor = 14;
    private final static int fontSizeStor = 18;
    private static final int maksLinjerPerSide = 45;
    private static final int[] startSidenXY = new int[]{50, 700};
    private static final float leadingNormal = 14f;
    private static final float leadingSmaa = 1f;
    private static final float[] logoposition = new float[]{50, 750};
    private static final float[] logoStorrelse = new float[]{60, 38};
    private static final String ikonfil = "navikon.png";

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATOFORMAT_NORGE);

    private int aktulLinjerISiden = 10;
    private int totalSider = 1;

    byte[] tilBytesAvPdf(Avtale avtale) {
        PDDocument dokument = generererPdf(avtale);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            dokument.save(baos);
            dokument.close();
        } catch (IOException e) {
            log.error("Feil oppsto ved generering av avtale " + avtale.getId(), e);
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Feil ved generering av PDF fil: " + e.getMessage());
        }
        return baos.toByteArray();
    }

    private PDDocument generererPdf(Avtale avtale) {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        PDImageXObject pdImage = null;
        try {
            byte[] iconBytes = getClass().getClassLoader().getResourceAsStream(ikonfil).readAllBytes();
            pdImage = PDImageXObject.createFromByteArray(document, iconBytes, ikonfil);
        } catch (Exception e) {
            log.error("Feil ved generering av PDF fil format, logo blir ikke laget: {}", e.getMessage());
        }

        try {
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            if (pdImage != null) {
                contentStream.drawImage(pdImage, logoposition[0], logoposition[1], logoStorrelse[0], logoStorrelse[1]);
            }

            contentStream.beginText();
            contentStream.newLineAtOffset(startSidenXY[0], startSidenXY[1]);
            contentStream.setLeading(leadingNormal);
            contentStream = skrivTekst("Avtale for arbeidstrening", contentStream, document, font_Bold, fontSizeStor);
            contentStream.setFont(font, fontSize);
            List<Object> text = new ArrayList<>();
            float moveToPositionX = -32000;
            text.add(moveToPositionX);
            text.add("Startdato: " + avtale.getStartDato().format(DATE_TIME_FORMATTER));
            contentStream.showTextWithPositioning(text.toArray());
            contentStream.newLine();
            text = new ArrayList<>();
            text.add(moveToPositionX);
            text.add("Varighet: " + avtale.getArbeidstreningLengde() + " uker ");
            contentStream.showTextWithPositioning(text.toArray());
            contentStream.newLine();
            contentStream.newLineAtOffset(-0, -30);
            startNyttAvsnitt("Avtalens parter", contentStream);
            contentStream = skrivTekst("Deltaker  ", contentStream, document, font, fontSize);
            contentStream = skrivTekst(avtale.getDeltakerFornavn() + " " + avtale.getDeltakerEtternavn(), contentStream, document, font_Bold, fontSize);
            contentStream = skrivTekst("Fødselsnummer: " + avtale.getDeltakerFnr(), contentStream, document, font, fontSize);
            contentStream = skrivTekst("Telefon: " +  blankForNull(avtale.getDeltakerTlf()), contentStream, document, font, fontSize);
            contentStream.newLine();
            contentStream = skrivTekst("Arbeidsgiver ", contentStream, document, font, fontSize);
            contentStream = skrivTekst(avtale.getBedriftNavn(), contentStream, document, font_Bold, fontSize);
            contentStream = skrivTekst("BedriftsNr: " + avtale.getBedriftNr(), contentStream, document, font, fontSize);
            contentStream = skrivTekst("Kontakperson: " + avtale.getArbeidsgiverFornavn() + " " + avtale.getArbeidsgiverEtternavn(), contentStream, document, font, fontSize);
            contentStream = skrivTekst("Telefon: " + blankForNull(avtale.getArbeidsgiverTlf()), contentStream, document, font, fontSize);
            contentStream.newLine();
            contentStream = skrivTekst("NAV-veileder ", contentStream, document, font, fontSize);
            contentStream = skrivTekst(avtale.getVeilederFornavn() + " " + avtale.getVeilederEtternavn(), contentStream, document, font_Bold, fontSize);
            contentStream = skrivTekst("Telefon: " + blankForNull(avtale.getVeilederTlf()), contentStream, document, font, fontSize);
            contentStream.newLine();
            startNyttAvsnitt("Mål  ", contentStream);

            for (Maal maal : avtale.getMaal()
            ) {
                aktulLinjerISiden++;
                contentStream = skrivTekst("Kategori", contentStream, document, font_Bold, fontSize);
                contentStream = skrivTekst(maal.getKategori(), contentStream, document, font, fontSize);
                contentStream = skrivTekst("Beskrivelse", contentStream, document, font_Bold, fontSize);
                contentStream = skrivTekst(maal.getBeskrivelse(), contentStream, document, font, fontSize);
                contentStream.newLine();
            }
            startNyttAvsnitt("Arbeidsoppgaver ", contentStream);
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
            contentStream = skrivTekst("Varighet: " + avtale.getArbeidstreningLengde() + " uker ", contentStream, document, font, fontSize);
            contentStream = skrivTekst("Stillingsprosent: " + avtale.getArbeidstreningStillingprosent() + "%", contentStream, document, font, fontSize);
            contentStream.newLine();
            startNyttAvsnitt("Oppfølging ", contentStream);
            String oppfolging = avtale.getOppfolging();
            contentStream = skrivTekst(oppfolging, contentStream, document, font, fontSize);
            contentStream.newLine();
            aktulLinjerISiden += 2;
            startNyttAvsnitt("Tilrettelegging ", contentStream);
            String tilrettelegging = avtale.getTilrettelegging();
            contentStream = skrivTekst(tilrettelegging, contentStream, document, font, fontSize);
            //Vi trenger å sjekke at det er nok plass til Godkjenning i siden, upraktisk at godkjenning blir delt inn 2 sider
            if (aktulLinjerISiden > (maksLinjerPerSide - 10)) {
                contentStream = openNewPage(contentStream, document);
            }
            contentStream.newLine();
            startNyttAvsnitt("Godkjenning ", contentStream);
            contentStream = skrivTekst(" Godkjent av deltaker: " + avtale.getGodkjentAvDeltaker(), contentStream, document, font, fontSize);
            contentStream = skrivTekst(" Godkjent av Arbeidsgiver: " + avtale.getGodkjentAvArbeidsgiver(), contentStream, document, font, fontSize);
            contentStream.showText(" Godkjent av NAV-veileder: " + avtale.getGodkjentAvVeileder());
            try {
                if (avtale.isGodkjentPaVegneAv()) {
                    contentStream.newLine();
                    aktulLinjerISiden++;
                    contentStream = skrivTekst(PA_VEGNE_GRUNN_TXT, contentStream, document, font, fontSize);
                    if (avtale.getGodkjentPaVegneGrunn().isIkkeBankId()) {
                        contentStream = skrivTekst(IKKE_BANKID_TXT, contentStream, document, font, fontSize);
                    }
                    if (avtale.getGodkjentPaVegneGrunn().isReservert()) {
                        contentStream = skrivTekst(RESERVERT_TXT, contentStream, document, font, fontSize);
                    }
                    if (avtale.getGodkjentPaVegneGrunn().isDigitalKompetanse()) {
                        contentStream = skrivTekst(DIGITA_KOMPETANSE_TXT, contentStream, document, font, fontSize);
                    }
                }
            } catch (Exception e) {
                log.error("Kan være avtale fra gamle versjon som mangler mulighet for godkjenning på vegne av " + e.getMessage());
            }
            skrivFooter("Referanse:  " + avtale.getId().toString(), contentStream);
            contentStream.endText();
            contentStream.close();

        } catch (Exception e) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Feil ved generering av PDF fil: " + e.getMessage());
        }
        return document;
    }

    List<String> possibleWrapText(String skrivText, PDPage page) throws IOException {

        PDRectangle mediabox = page.getMediaBox();
        float margin = 72;
        float width = mediabox.getWidth() - 2 * margin;

        List<String> lines = new ArrayList<>();
        int lastSpace = -1;
        while (skrivText.length() > 0) {
            int spaceIndex = skrivText.indexOf(' ', lastSpace + 1);
            if (spaceIndex < 0)
                spaceIndex = skrivText.length();
            String subString = skrivText.substring(0, spaceIndex);
            float size = fontSize * font.getStringWidth(subString) / 1000;
            if (size > width) {
                if (lastSpace < 0)
                    lastSpace = spaceIndex;
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

    private PDPageContentStream openNewPage(PDPageContentStream contentStream, PDDocument document) throws IOException {
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

    private PDPageContentStream skrivTekst(String skrivTekst, PDPageContentStream contentStream, PDDocument document, PDFont fontIBruk, int fontSizeIBruk) throws Exception {
        for (String lineText : possibleWrapText(skrivTekst, new PDPage(PDRectangle.A4))) {
            aktulLinjerISiden = Integer.sum(aktulLinjerISiden, 1);
            if (aktulLinjerISiden > maksLinjerPerSide) {
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
        contentStream.setFont(font_Bold, fontSizeMellomStor);
        contentStream.showText(avsnitt);
        contentStream.setLeading(leadingSmaa);
        contentStream.newLine();
        contentStream.showText(LINJE);
        contentStream.setLeading(leadingNormal);
        contentStream.newLine();
        contentStream.newLine();
        aktulLinjerISiden += 3;
    }

    private void skrivFooter(String footer, PDPageContentStream contentStream) throws IOException {
        while (aktulLinjerISiden < 45) {
            contentStream.newLine();
            aktulLinjerISiden++;
        }
        contentStream.showText(LINJE);
        contentStream.newLine();
        Color fontColor = Color.gray;
        contentStream.setFont(font, fontSizeSmaa);
        contentStream.setNonStrokingColor(fontColor);
        contentStream.showText(footer);
    }

    private static String blankForNull(String muligNullVerdi) {
        return muligNullVerdi == null ? "" : muligNullVerdi;
    }
}
