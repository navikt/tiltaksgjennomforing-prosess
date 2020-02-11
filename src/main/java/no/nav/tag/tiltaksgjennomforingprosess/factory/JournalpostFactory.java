package no.nav.tag.tiltaksgjennomforingprosess.factory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.finn.unleash.Unleash;
import no.nav.tag.tiltaksgjennomforingprosess.domene.avtale.Avtale;
import no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost.*;
import no.nav.tag.tiltaksgjennomforingprosess.integrasjon.DokgenAdapter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

import static no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost.DokumentVariant.*;
import static no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost.Journalpost.JORURNALFØRENDE_ENHET;

@Slf4j
@Component
@RequiredArgsConstructor
public class JournalpostFactory {

    private final static String EKSTREF_PREFIKS = "AVT";

    private final AvtaleTilXml avtaleTilXml;
    private final DokgenAdapter dokgenAdapter;
    private final Unleash unleash;

    public Journalpost konverterTilJournalpost(Avtale avtale) {

        Bruker bruker = new Bruker();
        bruker.setId(avtale.getBedriftNr());
        Journalpost journalpost = new Journalpost();
        journalpost.setAvtaleId(avtale.getAvtaleId().toString());
        journalpost.setAvtaleVersjon(avtale.getVersjon());
        journalpost.setAvtaleVersjonId(avtale.getAvtaleVersjonId().toString());
        journalpost.setTittel(avtale.getTiltakstype().getTittel());
        journalpost.setBruker(bruker);
        journalpost.setAvsenderMottaker(new Avsender(avtale.getBedriftNr(), avtale.getBedriftNavn()));
        List<DokumentVariant> dokumentVarianter = new ArrayList<>(2);

        final byte[] dokumentPdfAsBytes = unleash.isEnabled("tag.tiltak.prosess.dokgen") ?
                dokgenAdapter.genererPdf(avtale) :
                new AvtaleTilPdf().tilBytesAvPdf(avtale);

        dokumentVarianter.add(new DokumentVariant(FILTYPE_PDF, VARIANFORMAT_PDF, encodeToBase64(dokumentPdfAsBytes)));
        Dokument dokument = new Dokument();
        dokument.setTittel(avtale.getTiltakstype().getTittel());
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
        journalpost.setBehandlingsTema(avtale.getTiltakstype().getBehandlingstema());
        journalpost.setEksternReferanseId(EKSTREF_PREFIKS + avtale.getAvtaleId().toString());
        final String dokumentXml = avtaleTilXml.genererXml(avtale);
        log.debug(dokumentXml);
        dokumentVarianter.add(new DokumentVariant(FILTYPE_XML, VARIANFORMAT_XML, encodeToBase64(dokumentXml.getBytes())));
    }

    private void journalfoerSomferdig(Journalpost journalpost, Avtale avtale) {
        journalpost.setJournalfoerendeEnhet(JORURNALFØRENDE_ENHET);
        journalpost.setSak(new Sak());
    }

    private String encodeToBase64(final byte[] dokumentBytes) {
        return Base64.getEncoder().encodeToString(dokumentBytes);
    }
}
