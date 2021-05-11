package no.nav.tag.tiltaksgjennomforingprosess.factory;

import no.finn.unleash.Unleash;
import no.nav.tag.tiltaksgjennomforingprosess.TestData;
import no.nav.tag.tiltaksgjennomforingprosess.domene.avtale.Avtale;
import no.nav.tag.tiltaksgjennomforingprosess.domene.avtale.Tiltakstype;
import no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost.Dokument;
import no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost.Journalpost;
import no.nav.tag.tiltaksgjennomforingprosess.integrasjon.DokgenAdapter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.HttpClientErrorException;

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

    @Before
    public void before() {
        when(dokgenAdapter.genererPdf(any(Avtale.class))).thenReturn(new byte[1]);
    }

    @Test
    public void journalpostArbeidstreningSkalTilArena() {
        Avtale avtale = TestData.opprettArbeidstreningAvtale();

        when(avtaleTilXml.genererXml(avtale)).thenCallRealMethod();
        Journalpost journalpost = journalpostFactory.konverterTilJournalpost(avtale);
        verify(avtaleTilXml, times(1)).genererXml(avtale);

        assertGenereltInnhold(journalpost, avtale);
        assertEquals("ab0422", journalpost.getBehandlingsTema());
        assertEquals("Avtale om arbeidstrening", journalpost.getTittel());
        assertEquals(2, journalpost.getDokumenter().get(0).getDokumentVarianter().size());
    }

    @Test
    public void journalpostArbeidstreningSkalIkkeTilArena() {
        Avtale avtale = TestData.opprettArbeidstreningAvtale();
        avtale.setVersjon(2);

        Journalpost journalpost = journalpostFactory.konverterTilJournalpost(avtale);
        verify(avtaleTilXml, never()).genererXml(avtale);

        assertGenereltInnhold(journalpost, avtale);
        assertEquals(1, journalpost.getDokumenter().size());
        assertEquals("9999", journalpost.getJournalfoerendeEnhet());
        assertEquals("GENERELL_SAK", journalpost.getSak().getSakstype());
        assertEquals("Avtale om arbeidstrening", journalpost.getTittel());
        assertNull(journalpost.getBehandlingsTema());
        assertEquals(1, journalpost.getDokumenter().get(0).getDokumentVarianter().size());
    }

    @Test
    public void journalpostLonnstilskuddTilArena() {
        Avtale avtale = TestData.opprettLonnstilskuddsAvtale();
        when(avtaleTilXml.genererXml(avtale)).thenCallRealMethod();

        Journalpost journalpost = journalpostFactory.konverterTilJournalpost(avtale);
        verify(avtaleTilXml, times(1)).genererXml(avtale);

        assertGenereltInnhold(journalpost, avtale);
        assertEquals("Avtale om midlertidig lønnstilskudd", journalpost.getTittel());
        assertEquals("ab0336", journalpost.getBehandlingsTema());
        assertEquals(2, journalpost.getDokumenter().get(0).getDokumentVarianter().size());
    }

    @Test
    public void journalpostLonnstilskuddSkalIkkeTilArena() {
        Avtale avtale = TestData.opprettLonnstilskuddsAvtale();
        avtale.setVersjon(2);

        Journalpost journalpost = journalpostFactory.konverterTilJournalpost(avtale);

        assertGenereltInnhold(journalpost, avtale);
        assertEquals(avtale.getTiltakstype().getTittel(), journalpost.getTittel());
        assertNull(journalpost.getBehandlingsTema());
        assertEquals(1, journalpost.getDokumenter().get(0).getDokumentVarianter().size());
    }

    @Test
    public void journalpostVarigLonnstilskuddTilArena() {
        Avtale avtale = TestData.opprettLonnstilskuddsAvtale();
        avtale.setTiltakstype(Tiltakstype.VARIG_LONNSTILSKUDD);

        when(avtaleTilXml.genererXml(avtale)).thenCallRealMethod();

        Journalpost journalpost = journalpostFactory.konverterTilJournalpost(avtale);
        verify(avtaleTilXml, times(1)).genererXml(avtale);

        assertGenereltInnhold(journalpost, avtale);
        assertEquals("Avtale om varig lønnstilskudd", journalpost.getTittel());
        assertEquals("ab0337", journalpost.getBehandlingsTema());
        assertEquals(2, journalpost.getDokumenter().get(0).getDokumentVarianter().size());
    }

    @Test
    public void journalpostMentorSkalIkkeTilArena() {
        Avtale avtale = TestData.opprettMentorAvtale();
        avtale.setVersjon(2);

        Journalpost journalpost = journalpostFactory.konverterTilJournalpost(avtale);

        assertGenereltInnhold(journalpost, avtale);
        assertEquals("Avtale om tilskudd til mentor", journalpost.getTittel());
        assertNull(journalpost.getBehandlingsTema());
        assertEquals(1, journalpost.getDokumenter().get(0).getDokumentVarianter().size());
    }

    @Test
    public void journalpostMentorTilArena() {
        Avtale avtale = TestData.opprettMentorAvtale();

        when(avtaleTilXml.genererXml(avtale)).thenCallRealMethod();

        Journalpost journalpost = journalpostFactory.konverterTilJournalpost(avtale);
        verify(avtaleTilXml, times(1)).genererXml(avtale);

        assertGenereltInnhold(journalpost, avtale);
        assertEquals("Avtale om tilskudd til mentor", journalpost.getTittel());
        assertEquals("ab0416", journalpost.getBehandlingsTema());
        assertEquals(2, journalpost.getDokumenter().get(0).getDokumentVarianter().size());
    }

    @Test(expected = RuntimeException.class)
    public void pdfDocGenFeiler() throws Exception {
        Avtale avtale = TestData.opprettArbeidstreningAvtale();

        when(dokgenAdapter.genererPdf(avtale)).thenThrow(HttpClientErrorException.class);
        journalpostFactory.konverterTilJournalpost(avtale);
        verify(dokgenAdapter, atLeastOnce()).genererPdf(avtale);
        verify(avtaleTilXml, never()).genererXml(avtale);
    }

    @Test(expected = RuntimeException.class)
    public void avtaleTilXmlFeiler() throws Exception {
        Avtale avtale = TestData.opprettArbeidstreningAvtale();
        when(avtaleTilXml.genererXml(avtale)).thenThrow(RuntimeException.class);
        journalpostFactory.konverterTilJournalpost(avtale);
    }

    private void assertGenereltInnhold(Journalpost journalpost, Avtale avtale) {
        assertEquals("INNGAAENDE", journalpost.getJournalposttype());
        assertEquals("NAV_NO", journalpost.getKanal());
        assertEquals("TIL", journalpost.getTema());

        assertEquals(avtale.getBedriftNr(), journalpost.getAvsenderMottaker().getId());
        assertEquals("ORGNR", journalpost.getBruker().getIdType());
        assertEquals(avtale.getBedriftNr(), journalpost.getAvsenderMottaker().getId());
        assertEquals("ORGNR", journalpost.getAvsenderMottaker().getIdType());
        assertEquals(avtale.getBedriftNavn(), journalpost.getAvsenderMottaker().getNavn());
        assertEquals("AVT-" + avtale.getAvtaleVersjonId(), journalpost.getEksternReferanseId());
        assertNotNull(journalpost.getTittel());
        assertNotNull(journalpost.getEksternReferanseId());

        assertEquals(1, journalpost.getDokumenter().size());

        Dokument dokument = journalpost.getDokumenter().get(0);
        assertEquals(avtale.getTiltakstype().getTittel(), journalpost.getDokumenter().get(0).getTittel());
        assertEquals(avtale.getTiltakstype().getBrevkode(), journalpost.getDokumenter().get(0).getBrevkode());

        dokument.getDokumentVarianter().forEach(dokumentVariant -> {
            if (dokumentVariant.getFiltype().equals("XML")) {
                assertEquals("ORIGINAL", dokumentVariant.getVariantformat());
            } else if (dokumentVariant.getFiltype().equals("PDFA")) {
                assertEquals("ARKIV", dokumentVariant.getVariantformat());
            } else {
                fail("DokumentType: " + dokumentVariant.getFiltype());
            }
            assertFalse(dokumentVariant.getFysiskDokument().isEmpty());
        });
    }
}
