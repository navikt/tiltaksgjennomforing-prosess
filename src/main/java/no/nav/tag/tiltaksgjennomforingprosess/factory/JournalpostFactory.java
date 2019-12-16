package no.nav.tag.tiltaksgjennomforingprosess.factory;

import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tiltaksgjennomforingprosess.domene.avtale.Avtale;
import no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

import static no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost.DokumentVariant.*;
import static no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost.Journalpost.JORURNALFØRENDE_ENHET;

@Slf4j
@Component
public class JournalpostFactory {

    private final static String EKSTREF_PREFIKS = "AVT";
    private final static String BEHANDLINGSTEMA = "ab0422";

    @Autowired
    private AvtaleTilXml avtaleTilXml;

    public Journalpost konverterTilJournalpost(Avtale avtale) {

        Bruker bruker = new Bruker();
        bruker.setId(avtale.getDeltakerFnr());
        Journalpost journalpost = new Journalpost();
        journalpost.setAvtaleId(avtale.getAvtaleId().toString());
        journalpost.setAvtaleVersjon(avtale.getVersjon());
        journalpost.setBruker(bruker);
        List<DokumentVariant> dokumentVarianter = new ArrayList<>(2);

        final byte[] dokumentPdfAsBytes = new AvtaleTilPdf().tilBytesAvPdf(avtale);
        dokumentVarianter.add(new DokumentVariant(FILTYPE_PDF, VARIANFORMAT_PDF, encodeToBase64(dokumentPdfAsBytes)));
        Dokument dokument = new Dokument();
        dokument.setDokumentVarianter(dokumentVarianter);
        journalfoerMedStatus(journalpost, avtale, dokument);
        journalpost.setDokumenter(Collections.singletonList(dokument));
        return journalpost;
    }

    private void journalfoerMedStatus(Journalpost journalpost, Avtale avtale, Dokument dokument) {
        if (journalpost.skalBehandlesIArena()) {
            journalfoerSomMidlertidig(journalpost, avtale, dokument.getDokumentVarianter());
            return;
        }
        journalfoerSomferdig(journalpost, avtale);
    }

    private void journalfoerSomMidlertidig(Journalpost journalpost, Avtale avtale, List<DokumentVariant> dokumentVarianter) {
        journalpost.setEksternReferanseId(EKSTREF_PREFIKS + avtale.getAvtaleId().toString());
        journalpost.setBehandlingsTema(BEHANDLINGSTEMA);
        final String dokumentXml = avtaleTilXml.genererXml(avtale);
        log.debug(dokumentXml);
        dokumentVarianter.add(new DokumentVariant(FILTYPE_XML, VARIANFORMAT_XML, encodeToBase64(dokumentXml.getBytes())));
        log.info("Versjon {} av avtale {} med versjonId {} skal sendes til Arena", avtale.getVersjon(), avtale.getAvtaleId(), avtale.getAvtaleVersjonId());
    }

    private void journalfoerSomferdig(Journalpost journalpost, Avtale avtale) {
        journalpost.setJournalfoerendeEnhet(JORURNALFØRENDE_ENHET);
        journalpost.setSak(new Sak());
        journalpost.setAvsenderMottaker(new Avsender(avtale.getBedriftNr(), avtale.getBedriftNavn()));
        log.info("Versjon {} av avtale {} med versjonId {} skal ikke sendes til Arena", avtale.getVersjon(), avtale.getAvtaleId(), avtale.getAvtaleVersjonId());
    }

    private String encodeToBase64(final byte[] dokumentBytes) {
        return Base64.getEncoder().encodeToString(dokumentBytes);
    }
}
