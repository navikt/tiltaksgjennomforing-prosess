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

    public Journalpost konverterTilJournalpost(Avtale avtale) {

        Bruker bruker = new Bruker();
        bruker.setId(avtale.getDeltakerFnr());

        Journalpost journalpost = new Journalpost();
        journalpost.setBruker(bruker);

        //TODO Sette i gang PDF konvertering av avtale her

        String dokumentXml = avtaleTilXml.genererXml(avtale);

        Dokument dokument = new Dokument();
        dokument.setDokumentVarianter(Arrays.asList(
                new DokumentVariant("PDF", encodeToBase64(dokumentXml, false)),
                new DokumentVariant("XML", encodeToBase64("dokument", true)))
        );
        journalpost.setDokumenter(Arrays.asList(dokument));
        return journalpost;
    }

    private String encodeToBase64(String dokument, boolean isPdf) {
        if(isPdf) {
            return testEncodeDummyFileToBase64();
        }
        return Base64.getEncoder().encodeToString(dokument.getBytes());
    }

    //TODO Denne er temporær for testing. Bytt ut med pdf generering
    private String testEncodeDummyFileToBase64() {
        byte[] bytes = new byte[0];
        try {
            Path fil = Paths.get(getClass().getClassLoader()
                    .getResource("dummy.pdf").toURI());
            bytes = Files.readAllBytes(fil);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Base64.getEncoder().encodeToString(bytes);
    }
}
