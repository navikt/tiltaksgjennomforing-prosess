package no.nav.tag.tiltaksgjennomforingprosess.journalpost.factory;

import no.nav.tag.tiltaksgjennomforingprosess.domene.Avtale;
import no.nav.tag.tiltaksgjennomforingprosess.domene.Maal;
import no.nav.tag.tiltaksgjennomforingprosess.domene.Oppgave;
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
            contentStream.setFont(PDType1Font.COURIER, 12);
            contentStream.showText("Avtale Nr:  " + avtale.getId().toString());
            contentStream.showText(" Version Nr: " + avtale.getVersjon().toString());
            contentStream.newLine();
            contentStream.newLineAtOffset(-50, -30);
            contentStream.showText("Deltaker FødeslsNummer er: ");
            contentStream.showText(avtale.getDeltakerFnr());
            contentStream.newLine();
            contentStream.showText("Deltaker Navn: " + avtale.getDeltakerFornavn() + " " + avtale.getDeltakerEtternavn());
            contentStream.newLine();
            contentStream.showText("Deltaker telefon nummer: " + avtale.getDeltakerTlf());
            contentStream.newLine();
            contentStream.showText("Bedrifts Navn:" + avtale.getBedriftNavn() + " BedriftsNr: " + avtale.getBedriftNr());
            contentStream.newLine();
            contentStream.showText("Arbeidsgiver navn/Kontakperson i bedrift: " + avtale.getArbeidsgiverFornavn() + " " + avtale.getArbeidsgiverEtternavn());
            contentStream.newLine();
            contentStream.showText("Arbeidsgiver Tlf/Kontakperson i bedrift: " + avtale.getArbeidsgiverTlf());
            contentStream.newLine();
            contentStream.showText("Nav veileder navn: " + avtale.getVeilederFornavn() + " " + avtale.getVeilederEtternavn());
            contentStream.newLine();
            contentStream.showText("Nav veileder Tlf: " + avtale.getVeilederTlf());
            contentStream.newLine();
            contentStream.showText("Oppfølgong: " + avtale.getOppfolging());
            contentStream.newLine();
            contentStream.showText("Tilrettelgging: " + avtale.getTilrettelegging());
            contentStream.newLine();
            contentStream.showText("Start Dato: " + avtale.getTilrettelegging() + ", ArbeidsTrening varighet: " + avtale.getArbeidstreningLengde() + " uker, " +
                    "Stilling prosent:" + avtale.getArbeidstreningStillingprosent());
            contentStream.newLine();
            contentStream.showText("Mål for arbeidstrening: ");
            contentStream.newLine();
            for (Maal maal : avtale.getMaal()
            ) {
                contentStream.showText(" Mål kategori: " + maal.getKategori() + ", Beskrivelse: " + maal.getBeskrivelse());
                contentStream.newLine();
            }
            contentStream.showText("Arbeidstrening oppgaver: ");
            contentStream.newLine();
            for (Oppgave oppgave : avtale.getOppgaver()
            ) {
                contentStream.showText(" Oppgave tittel: " + oppgave.getTittel() + ", Opplæring: " + oppgave.getOpplaering() + ", Beskrivelse: " + oppgave.getBeskrivelse());
                contentStream.newLine();
            }

            contentStream.newLine();
            contentStream.showText(" Avtalen er digtialt signert av deltaker den : " + avtale.getGodkjentAvDeltaker());
            if(avtale.isGodkjentPaVegneAv()){
                contentStream.showText(" NB: Avtalen er godkjent av veileder på vegne av deltaker : ");
            }
            contentStream.newLine();
            contentStream.showText(" Avtalen er digtialt signert av ArbeidsGiver den : " + avtale.getGodkjentAvArbeidsgiver());
            contentStream.newLine();
            contentStream.showText(" Avtalen er digtialt signert av Nav veileder den : " + avtale.getGodkjentAvVeileder());


            //contentStream.drawString(avtale.toString());
            contentStream.endText();
            contentStream.close();

            document.save("avtaleNr" + avtale.getId() + ".pdf");
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

}
