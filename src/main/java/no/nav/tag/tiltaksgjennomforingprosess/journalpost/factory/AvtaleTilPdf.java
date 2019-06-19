package no.nav.tag.tiltaksgjennomforingprosess.journalpost.factory;

import no.nav.tag.tiltaksgjennomforingprosess.domene.Avtale;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AvtaleTilPdf {
    public String generererPdf(Avtale avtale) {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);
        try {
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.setFont(PDType1Font.COURIER_BOLD, 12);
            contentStream.beginText();
            //contentStream.moveTextPositionByAmount(100, 70);
            contentStream.newLineAtOffset(100, 700);

            contentStream.setLeading(14.5f);
            contentStream.showText("Avtale for arbeidsTrening");
            contentStream.newLine();
            contentStream.newLineAtOffset(-50,-15);
            contentStream.showText("Deltaker FødeslsNummer er: ");
            contentStream.showText(avtale.getDeltakerFnr());
            contentStream.newLine();
            contentStream.showText(avtale.getBedriftNr());
            //contentStream.drawString(avtale.toString());
            contentStream.endText();
            contentStream.close();

            document.save("pdfBoxHelloWorld2.pdf");
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

}
