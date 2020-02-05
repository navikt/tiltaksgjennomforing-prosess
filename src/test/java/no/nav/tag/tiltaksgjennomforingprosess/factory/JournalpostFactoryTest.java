package no.nav.tag.tiltaksgjennomforingprosess.factory;

import no.finn.unleash.Unleash;
import no.nav.tag.tiltaksgjennomforingprosess.TestData;
import no.nav.tag.tiltaksgjennomforingprosess.domene.avtale.Avtale;
import no.nav.tag.tiltaksgjennomforingprosess.domene.avtale.Tiltakstype;
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

    @Mock
    private DokgenAdapter dokgenAdapter;

    @Mock
    private Unleash unleash;

    @InjectMocks
    private JournalpostFactory journalpostFactory;

    @Test
    public void journalpostArbeidstreningSkalTilArena() {
        Avtale avtale = TestData.opprettArbeidstreningAvtale();

        when(avtaleTilXml.genererXml(avtale)).thenCallRealMethod();
        Journalpost journalpost = journalpostFactory.konverterTilJournalpost(avtale);
        verify(avtaleTilXml, times(1)).genererXml(avtale);

        assertGenereltInnhold(journalpost, avtale);
        assertEquals("ab0422", journalpost.getBehandlingsTema());
        assertEquals("Avtale om arbeidstrening", journalpost.getTittel());
        assertTrue(journalpost.skalBehandlesIArena());
        assertEquals(2, journalpost.getDokumenter().get(0).getDokumentVarianter().size());
    }

    @Test
    public void journalpostArbeidstreningSkalIkkeTilArena() {
        Avtale avtale = TestData.opprettArbeidstreningAvtale();
        avtale.setVersjon(2);

        Journalpost journalpost = journalpostFactory.konverterTilJournalpost(avtale);
        verify(avtaleTilXml, never()).genererXml(avtale);

        assertGenereltInnhold(journalpost, avtale);
        assertFalse(journalpost.skalBehandlesIArena());
        assertEquals(1, journalpost.getDokumenter().size());
        assertEquals("9999", journalpost.getJournalfoerendeEnhet());
        assertEquals("GENERELL_SAK", journalpost.getSak().getSakstype());
        assertEquals("Avtale om arbeidstrening", journalpost.getTittel());
        assertNull(journalpost.getBehandlingsTema());
        assertEquals(1, journalpost.getDokumenter().get(0).getDokumentVarianter().size());
        assertEquals(Tiltakstype.ARBEIDSTRENING.getTittel(), journalpost.getDokumenter().get(0).getTittel());
    }

    @Test
    public void journalpostMentorTilArena(){
        Avtale avtale = TestData.opprettMentorAvtale();
        when(avtaleTilXml.genererXml(avtale)).thenCallRealMethod();

        Journalpost journalpost = journalpostFactory.konverterTilJournalpost(avtale);
        verify(avtaleTilXml, times(1)).genererXml(avtale);

        assertGenereltInnhold(journalpost, avtale);
        assertEquals("Avtale om mentortilskudd", journalpost.getTittel());
        assertEquals("ab0416", journalpost.getBehandlingsTema());
        assertTrue(journalpost.skalBehandlesIArena());
        assertEquals(2, journalpost.getDokumenter().get(0).getDokumentVarianter().size());
    }

    @Test
    public void journalpostMentorSkalIkkeTilArena(){
        Avtale avtale = TestData.opprettMentorAvtale();
        avtale.setVersjon(2);

        Journalpost journalpost = journalpostFactory.konverterTilJournalpost(avtale);

        assertGenereltInnhold(journalpost, avtale);
        assertEquals("Avtale om mentortilskudd", journalpost.getTittel());
        assertNull(journalpost.getBehandlingsTema());
        assertFalse(journalpost.skalBehandlesIArena());
        assertEquals(1, journalpost.getDokumenter().get(0).getDokumentVarianter().size());
    }


    @Test(expected = RuntimeException.class)
    public void avtaleTilXmlFeiler() throws Exception {
        Avtale avtale = TestData.opprettArbeidstreningAvtale();
        when(avtaleTilXml.genererXml(avtale)).thenThrow(RuntimeException.class);
        journalpostFactory.konverterTilJournalpost(avtale);
    }

    private void assertGenereltInnhold(Journalpost journalpost, Avtale avtale){
        assertEquals("INNGAAENDE", journalpost.getJournalposttype());
        assertEquals("NAV_NO", journalpost.getKanal());
        assertEquals("TIL", journalpost.getTema());

        assertEquals(avtale.getBedriftNr(), journalpost.getAvsenderMottaker().getId());
        assertEquals("ORGNR", journalpost.getBruker().getIdType());
        assertEquals(avtale.getBedriftNr(), journalpost.getAvsenderMottaker().getId());
        assertEquals("ORGNR", journalpost.getAvsenderMottaker().getIdType());
        assertEquals(avtale.getBedriftNavn(), journalpost.getAvsenderMottaker().getNavn());
        assertNotNull(journalpost.getTittel());

        assertEquals(1, journalpost.getDokumenter().size());

        journalpost.getDokumenter().get(0).getDokumentVarianter().forEach(dokumentVariant -> {
            if (dokumentVariant.getFiltype().equals("XML")) {
                assertEquals("ORIGINAL", dokumentVariant.getVariantformat());
            } else if (dokumentVariant.getFiltype().equals("PDFA")) {
                assertEquals("ARKIV", dokumentVariant.getVariantformat());
            } else {
                fail("DokumentType: " + dokumentVariant.getFiltype());
            }
            assertFalse(dokumentVariant.getFysiskDokument().isEmpty());
        });

//        DokumentVariant dokumentVariant = journalpost.getDokumenter().get(0).getDokumentVarianter().get(0);
//        assertEquals("PDFA", dokumentVariant.getFiltype());
//        assertEquals("ARKIV", dokumentVariant.getVariantformat());
    }
}
