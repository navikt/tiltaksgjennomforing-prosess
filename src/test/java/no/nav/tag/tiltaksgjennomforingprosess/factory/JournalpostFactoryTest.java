package no.nav.tag.tiltaksgjennomforingprosess.factory;

import no.nav.tag.tiltaksgjennomforingprosess.TestData;
import no.nav.tag.tiltaksgjennomforingprosess.domene.avtale.Avtale;
import no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost.Dokument;
import no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost.DokumentVariant;
import no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost.Journalpost;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class JournalpostFactoryTest {

    @Mock
    private AvtaleTilXml avtaleTilXml;

    @InjectMocks
    private JournalpostFactory journalpostFactory;

    @Test
    public void journalpostSkalTilArena() throws Exception {
        Avtale avtale = TestData.opprettAvtale();

        when(avtaleTilXml.genererXml(avtale)).thenCallRealMethod();
        Journalpost journalpost = journalpostFactory.konverterTilJournalpost(avtale);

        assertEquals("ab0422", journalpost.getBehandlingsTema());
        assertEquals("INNGAAENDE", journalpost.getJournalposttype());
        assertEquals("NAV_NO", journalpost.getKanal());
        assertEquals("TIL", journalpost.getTema());
        assertEquals("Avtale om arbeidstrening", journalpost.getTittel());
        assertEquals("AVT" + avtale.getAvtaleId(), journalpost.getEksternReferanseId());
        assertEquals(avtale.getDeltakerFnr(), journalpost.getBruker().getId());
        assertEquals("FNR", journalpost.getBruker().getIdType());
        assertTrue(journalpost.getBehandlesIArena());
        assertEquals(1, journalpost.getDokumenter().size());

        journalpost.getDokumenter().get(0).getDokumentVarianter().forEach(dokumentVariant -> {
            if(dokumentVariant.getFiltype().equals("XML")){
                assertEquals("ORIGINAL", dokumentVariant.getVariantformat());
            } else if(dokumentVariant.getFiltype().equals("PDFA")){
                assertEquals("ARKIV", dokumentVariant.getVariantformat());
            } else{
                fail("DokumentType: " + dokumentVariant.getFiltype());
            }
            assertFalse(dokumentVariant.getFysiskDokument().isEmpty());
        });
    }

    @Test
    public void journalpostSkalIkkeTilArena(){
        Avtale avtale = TestData.opprettAvtale();
        avtale.setVersjon(2);

        Journalpost journalpost = journalpostFactory.konverterTilJournalpost(avtale);
        verify(avtaleTilXml, never()).genererXml(avtale);

        assertNull(journalpost.getBehandlingsTema());
        assertEquals("INNGAAENDE", journalpost.getJournalposttype());
        assertEquals("NAV_NO", journalpost.getKanal());
        assertEquals("TIL", journalpost.getTema());
        assertEquals("Avtale om arbeidstrening", journalpost.getTittel());
        assertFalse(journalpost.getBehandlesIArena());
        assertNull(journalpost.getEksternReferanseId());
        assertEquals(avtale.getDeltakerFnr(), journalpost.getBruker().getId());
        assertEquals("FNR", journalpost.getBruker().getIdType());
        assertEquals("9999", journalpost.getJournalfoerendeEnhet());
        assertEquals("GENERELL_SAK", journalpost.getSak().getSakstype());

        assertEquals(avtale.getBedriftNr(), journalpost.getAvsenderMottaker().getId());
        assertEquals("ORGNR", journalpost.getAvsenderMottaker().getIdType());
        assertEquals(avtale.getBedriftNavn(), journalpost.getAvsenderMottaker().getNavn());


        assertNotNull(journalpost.getTittel());
        assertEquals(1, journalpost.getDokumenter().size());
        assertEquals("Avtale om arbeidstrening", journalpost.getDokumenter().get(0).getTittel());
        assertEquals(1, journalpost.getDokumenter().get(0).getDokumentVarianter().size());

        DokumentVariant dokumentVariant = journalpost.getDokumenter().get(0).getDokumentVarianter().get(0);
        assertEquals("PDFA", dokumentVariant.getFiltype());
        assertEquals("ARKIV", dokumentVariant.getVariantformat());
    }

    @Test(expected = RuntimeException.class)
    public void avtaleTilXmlFeiler() throws Exception {
        Avtale avtale = TestData.opprettAvtale();
        when(avtaleTilXml.genererXml(avtale)).thenThrow(RuntimeException.class);
        journalpostFactory.konverterTilJournalpost(avtale);
    }
}
