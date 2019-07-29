package no.nav.tag.tiltaksgjennomforingprosess.journalpost.factory;

import no.nav.tag.tiltaksgjennomforingprosess.domene.Avtale;
import no.nav.tag.tiltaksgjennomforingprosess.journalpost.request.Bruker;
import no.nav.tag.tiltaksgjennomforingprosess.journalpost.request.Dokument;
import no.nav.tag.tiltaksgjennomforingprosess.journalpost.request.DokumentVariant;
import no.nav.tag.tiltaksgjennomforingprosess.journalpost.request.Journalpost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;

@Component
public class JournalpostFactory {

    @Autowired
    private AvtaleTilXml avtaleTilXml;
    private AvtaleTilPdf avtaleTilPdf;

    public Journalpost konverterTilJournalpost(Avtale avtale) {

        Bruker bruker = new Bruker();
        bruker.setId(avtale.getDeltakerFnr());

        Journalpost journalpost = new Journalpost();
        journalpost.setBruker(bruker);


        String dokumentPdf = avtaleTilPdf.generererPdf(avtale);
        String dokumentXml = avtaleTilXml.genererXml(avtale);
        Dokument dokument = new Dokument();
        dokument.setDokumentVarianter(Arrays.asList(
                new DokumentVariant("XML", encodeToBase64(dokumentXml, false)),
                new DokumentVariant("PDF", encodeToBase64(dokumentPdf, true)))
        );
        journalpost.setDokumenter(Arrays.asList(dokument));
        return journalpost;
    }

    private String encodeToBase64(String dokument, boolean isPdf) {
        if (isPdf) {
            return testEncodePdfFileToBase64(dokument);
        }
        return Base64.getEncoder().encodeToString(dokument.getBytes());
    }

    //TODO Denne er temporær for testing. Bytt ut med pdf generering
    private String testEncodePdfFileToBase64(String dokument) {
        byte[] bytes = new byte[0];
        try {
            Path fil = Paths.get(getClass().getClassLoader()
                    .getResource(dokument).toURI());
            bytes = Files.readAllBytes(fil);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Base64.getEncoder().encodeToString(bytes);
    }
}
