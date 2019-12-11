package no.nav.tag.tiltaksgjennomforingprosess.factory;

import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tiltaksgjennomforingprosess.domene.avtale.Avtale;
import no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost.Bruker;
import no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost.Dokument;
import no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost.DokumentVariant;
import no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost.Journalpost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

import static no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost.DokumentVariant.*;

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
        journalpost.setBruker(bruker);

        List<DokumentVariant> dokumentVarianter = new ArrayList<>(2);

        final byte[] dokumentPdfAsBytes = new AvtaleTilPdf().tilBytesAvPdf(avtale);
        dokumentVarianter.add(new DokumentVariant(FILTYPE_PDF, VARIANFORMAT_PDF, encodeToBase64(dokumentPdfAsBytes)));
        registrerJournalfoeringIArena(journalpost, avtale, dokumentVarianter);

        Dokument dokument = new Dokument();
        dokument.setDokumentVarianter(dokumentVarianter);
        journalpost.setDokumenter(Collections.singletonList(dokument));
        return journalpost;
    }

    private void registrerJournalfoeringIArena(Journalpost journalpost, Avtale avtale, List<DokumentVariant> dokumentVarianter) {
        if (avtale.registreresIArena()) {
            journalpost.setEksternReferanseId(EKSTREF_PREFIKS + avtale.getId().toString());
            journalpost.setBehandlingsTema(BEHANDLINGSTEMA);
            journalpost.setBehandlesIArena(true);
            final String dokumentXml = avtaleTilXml.genererXml(avtale);
            dokumentVarianter.add(new DokumentVariant(FILTYPE_XML, VARIANFORMAT_XML, encodeToBase64(dokumentXml.getBytes())));
            log.info("Avtale {} skal sendes til Arena", avtale.getId());
            return;
        }
        journalpost.setBehandlesIArena(false);
        log.info("Avtale {} skal ikke sendes til Arena", avtale.getId());
    }

    private String encodeToBase64(final byte[] dokumentBytes) {
        return Base64.getEncoder().encodeToString(dokumentBytes);
    }
}
