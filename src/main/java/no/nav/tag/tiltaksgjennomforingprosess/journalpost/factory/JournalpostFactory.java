package no.nav.tag.tiltaksgjennomforingprosess.journalpost.factory;

import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tiltaksgjennomforingprosess.domene.Avtale;
import no.nav.tag.tiltaksgjennomforingprosess.journalpost.request.Bruker;
import no.nav.tag.tiltaksgjennomforingprosess.journalpost.request.Dokument;
import no.nav.tag.tiltaksgjennomforingprosess.journalpost.request.DokumentVariant;
import no.nav.tag.tiltaksgjennomforingprosess.journalpost.request.Journalpost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;

import java.util.Arrays;
import java.util.Base64;

@Slf4j
@Component
public class JournalpostFactory {

    @Autowired
    private AvtaleTilXml avtaleTilXml;

    public Journalpost konverterTilJournalpost(Avtale avtale) {

        Bruker bruker = new Bruker();
        bruker.setId(avtale.getDeltakerFnr());
        Journalpost journalpost = new Journalpost();
        journalpost.setBruker(bruker);

        final byte[] dokumentPdfAsBytes = avtaleTilPdfBytes(avtale);
        final String dokumentXml = avtaleTilXml.genererXml(avtale);

        Dokument dokument = new Dokument();
        dokument.setDokumentVarianter(Arrays.asList(
                new DokumentVariant("XML", encodeToBase64(dokumentXml.getBytes())),
                new DokumentVariant("PDF", encodeToBase64(dokumentPdfAsBytes)))
        );
        journalpost.setDokumenter(Arrays.asList(dokument));
        return journalpost;
    }

    private byte[] avtaleTilPdfBytes(Avtale avtale) {
        try {
            return new AvtaleTilPdf().tilBytesAvPdf(avtale);
        } catch (Exception e) {
            log.error("Feil ved generering til pdf fil: AvtaleId={}", avtale.getId(), e);
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String encodeToBase64(final byte[] dokumentBytes) {
        return Base64.getEncoder().encodeToString(dokumentBytes);
    }
}
