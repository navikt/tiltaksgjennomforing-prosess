package no.nav.tag.tiltaksgjennomforingprosess.factory;

import static no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost.DokumentVariant.FILTYPE_PDF;
import static no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost.DokumentVariant.FILTYPE_XML;
import static no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost.DokumentVariant.VARIANFORMAT_PDF;
import static no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost.DokumentVariant.VARIANFORMAT_XML;
import static no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost.Journalpost.EKSTREF_PREFIKS;

import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import no.nav.tag.tiltaksgjennomforingprosess.domene.avtale.Avtale;
import no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost.Bruker;
import no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost.Dokument;
import no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost.DokumentVariant;
import no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost.Journalpost;

@Component
public class JournalpostFactory {

    @Autowired
    private AvtaleTilXml avtaleTilXml;

    public Journalpost konverterTilJournalpost(Avtale avtale) {

        Bruker bruker = new Bruker();
        bruker.setId(avtale.getDeltakerFnr());
        Journalpost journalpost = new Journalpost();
        journalpost.setBruker(bruker);
        journalpost.setEksternReferanseId(EKSTREF_PREFIKS + avtale.getId().toString());

        final byte[] dokumentPdfAsBytes = new AvtaleTilPdf().tilBytesAvPdf(avtale);
        final String dokumentXml = avtaleTilXml.genererXml(avtale);

        Dokument dokument = new Dokument();
        dokument.setDokumentVarianter(Arrays.asList(
                new DokumentVariant(FILTYPE_XML, VARIANFORMAT_XML ,encodeToBase64(dokumentXml.getBytes())),
                new DokumentVariant(FILTYPE_PDF, VARIANFORMAT_PDF, encodeToBase64(dokumentPdfAsBytes)))
        );
        journalpost.setDokumenter(Collections.singletonList(dokument));
        return journalpost;
    }

    private String encodeToBase64(final byte[] dokumentBytes) {
        return Base64.getEncoder().encodeToString(dokumentBytes);
    }
}
