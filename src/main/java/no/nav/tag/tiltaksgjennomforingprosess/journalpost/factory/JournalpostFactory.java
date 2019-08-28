package no.nav.tag.tiltaksgjennomforingprosess.journalpost.factory;

import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tiltaksgjennomforingprosess.domene.Avtale;
import no.nav.tag.tiltaksgjennomforingprosess.journalpost.request.Bruker;
import no.nav.tag.tiltaksgjennomforingprosess.journalpost.request.Dokument;
import no.nav.tag.tiltaksgjennomforingprosess.journalpost.request.DokumentVariant;
import no.nav.tag.tiltaksgjennomforingprosess.journalpost.request.Journalpost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Base64;

@Slf4j
@Component
public class JournalpostFactory {

    @Autowired
    private AvtaleTilXml avtaleTilXml;

    @Autowired
    private AvtaleTilPdf avtaleTilPdf;

    public Journalpost konverterTilJournalpost(Avtale avtale) {

        Bruker bruker = new Bruker();
        bruker.setId(avtale.getDeltakerFnr());
        Journalpost journalpost = new Journalpost();
        journalpost.setBruker(bruker);

        String dokumentPdf;
        try {
            dokumentPdf = avtaleTilPdf.generererPdf(avtale);
        } catch (Exception e) {
            log.error("Feil ved generering av pdf fil: AvtaleId={}", avtale.getId(), e.getMessage());
            throw new RuntimeException(e);
        }

        final String dokumentXml = avtaleTilXml.genererXml(avtale);
        Dokument dokument = new Dokument();
        dokument.setDokumentVarianter(Arrays.asList(
                new DokumentVariant("XML", encodeToBase64(dokumentXml)),
                new DokumentVariant("PDF", encodeToBase64(dokumentPdf)))
        );
        journalpost.setDokumenter(Arrays.asList(dokument));
        return journalpost;
    }

    public String encodeToBase64(final String dokument) {
        return Base64.getEncoder().encodeToString(dokument.getBytes());
    }
}
