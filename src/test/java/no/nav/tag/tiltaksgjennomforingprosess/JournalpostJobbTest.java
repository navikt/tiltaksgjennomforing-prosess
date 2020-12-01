package no.nav.tag.tiltaksgjennomforingprosess;

import no.finn.unleash.Unleash;
import no.nav.tag.tiltaksgjennomforingprosess.domene.PdfGenException;
import no.nav.tag.tiltaksgjennomforingprosess.domene.avtale.Avtale;
import no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost.Journalpost;
import no.nav.tag.tiltaksgjennomforingprosess.factory.JournalpostFactory;
import no.nav.tag.tiltaksgjennomforingprosess.integrasjon.JoarkService;
import no.nav.tag.tiltaksgjennomforingprosess.integrasjon.TiltaksgjennomfoeringApiService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static no.nav.tag.tiltaksgjennomforingprosess.JournalpostJobb.MAPPING_FEIL;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class JournalpostJobbTest {

    private final String JOURNALPOST_ID = "1234";

    @Mock
    private Unleash unleash;

    @Mock
    private TiltaksgjennomfoeringApiService tiltaksgjennomfoeringApiService;

    @Mock
    private JournalpostFactory journalpostFactory;

    @Mock
    private JoarkService joarkService;

    @InjectMocks
    private JournalpostJobb journalpostJobb;

    @Before
    public void setUp() {
        JournalpostJobb.enabled = true;
    }

    @After
    public void tearDown() {
        JournalpostJobb.enabled = true;
    }

    @Test
    public void kallerIkkeJoarkMedEnAvtaleSomFeilet() {

        Avtale okAvtale = TestData.opprettArbeidstreningAvtale();
        Journalpost okJournalpost = new Journalpost();
        okJournalpost.setEksternReferanseId(okAvtale.getAvtaleId().toString());

        Avtale feiletAvt = TestData.opprettArbeidstreningAvtale();

        Map<UUID, String> jorurnalførteAvtaler = new HashMap<>(2);
        jorurnalførteAvtaler.put(okAvtale.getAvtaleVersjonId(), JOURNALPOST_ID);
        jorurnalførteAvtaler.put(feiletAvt.getAvtaleVersjonId(), MAPPING_FEIL);

        when(tiltaksgjennomfoeringApiService.finnAvtalerTilJournalfoering()).thenReturn(Arrays.asList(okAvtale, feiletAvt));

        when(journalpostFactory.konverterTilJournalpost(okAvtale)).thenReturn(okJournalpost);
        when(journalpostFactory.konverterTilJournalpost(feiletAvt)).thenThrow(RuntimeException.class);

        when(joarkService.sendJournalpost(eq(okJournalpost))).thenReturn(JOURNALPOST_ID);

        journalpostJobb.avtalerTilJournalfoering();
        verify(joarkService, times(1)).sendJournalpost(eq(okJournalpost));
        verify(tiltaksgjennomfoeringApiService, atLeastOnce()).settAvtalerTilJournalfoert(eq(jorurnalførteAvtaler));
    }

    @Test
    public void toAvtalerDerEttjoarkKallFeiler(){
        Avtale avtale1 = TestData.opprettArbeidstreningAvtale();
        Avtale avtale2 = TestData.opprettArbeidstreningAvtale();

        Journalpost journalpost1 = new Journalpost();
        journalpost1.setEksternReferanseId(avtale1.getAvtaleId().toString());
        Journalpost journalpost2 = new Journalpost();
        journalpost2.setEksternReferanseId(avtale2.getAvtaleId().toString());

        Map<UUID, String> jorurnalførteAvtaler = new HashMap<>(1);
        jorurnalførteAvtaler.put(avtale1.getAvtaleVersjonId(), JOURNALPOST_ID);

        when(tiltaksgjennomfoeringApiService.finnAvtalerTilJournalfoering()).thenReturn(Arrays.asList(avtale1, avtale2));

        when(journalpostFactory.konverterTilJournalpost(avtale1)).thenReturn(journalpost1);
        when(journalpostFactory.konverterTilJournalpost(avtale2)).thenReturn(journalpost2);

        when(joarkService.sendJournalpost(eq(journalpost1))).thenReturn(JOURNALPOST_ID);
        when(joarkService.sendJournalpost(eq(journalpost2))).thenThrow(RuntimeException.class);

        journalpostJobb.avtalerTilJournalfoering();
        verify(joarkService, times(1)).sendJournalpost(eq(journalpost1));
        verify(joarkService, times(1)).sendJournalpost(eq(journalpost2));
        verify(tiltaksgjennomfoeringApiService, times(1)).settAvtalerTilJournalfoert(eq(jorurnalførteAvtaler));
    }

    @Test
    public void kallerMedAlleAvtaler() {

        Avtale avtale1 = TestData.opprettArbeidstreningAvtale();
        Avtale avtale2 = TestData.opprettArbeidstreningAvtale();

        Journalpost journalpost1 = new Journalpost();
        journalpost1.setEksternReferanseId(avtale1.getAvtaleId().toString());
        Journalpost journalpost2 = new Journalpost();
        journalpost2.setEksternReferanseId(avtale2.getAvtaleId().toString());

        Map<UUID, String> jorurnalførteAvtaler = new HashMap<>(2);
        jorurnalførteAvtaler.put(avtale1.getAvtaleVersjonId(), JOURNALPOST_ID);
        jorurnalførteAvtaler.put(avtale2.getAvtaleVersjonId(), JOURNALPOST_ID);

        when(tiltaksgjennomfoeringApiService.finnAvtalerTilJournalfoering()).thenReturn(Arrays.asList(avtale1, avtale2));

        when(journalpostFactory.konverterTilJournalpost(avtale1)).thenReturn(journalpost1);
        when(journalpostFactory.konverterTilJournalpost(avtale2)).thenReturn(journalpost2);

        when(joarkService.sendJournalpost(eq(journalpost1))).thenReturn(JOURNALPOST_ID);
        when(joarkService.sendJournalpost(eq(journalpost2))).thenReturn(JOURNALPOST_ID);

        journalpostJobb.avtalerTilJournalfoering();
        verify(joarkService, times(1)).sendJournalpost(eq(journalpost1));
        verify(joarkService, times(1)).sendJournalpost(eq(journalpost2));
        verify(tiltaksgjennomfoeringApiService, times(1)).settAvtalerTilJournalfoert(eq(jorurnalførteAvtaler));
    }

    @Test
    public void pdfGenFeilerPåEnAvtale() {

        Avtale avtale1 = TestData.opprettArbeidstreningAvtale();
        Avtale avtale2 = TestData.opprettArbeidstreningAvtale();

        Journalpost journalpost1 = new Journalpost();
        journalpost1.setEksternReferanseId(avtale1.getAvtaleId().toString());
        Journalpost journalpost2 = new Journalpost();
        journalpost2.setEksternReferanseId(avtale2.getAvtaleId().toString());

        Map<UUID, String> jorurnalførteAvtaler = new HashMap<>(1);
        jorurnalførteAvtaler.put(avtale2.getAvtaleVersjonId(), JOURNALPOST_ID);

        when(tiltaksgjennomfoeringApiService.finnAvtalerTilJournalfoering()).thenReturn(Arrays.asList(avtale1, avtale2));

        when(journalpostFactory.konverterTilJournalpost(avtale1)).thenThrow(PdfGenException.class);
        when(journalpostFactory.konverterTilJournalpost(avtale2)).thenReturn(journalpost2);

        when(joarkService.sendJournalpost(eq(journalpost2))).thenReturn(JOURNALPOST_ID);

        journalpostJobb.avtalerTilJournalfoering();
        verify(joarkService, never()).sendJournalpost(eq(journalpost1));
        verify(joarkService, times(1)).sendJournalpost(eq(journalpost2));
        verify(tiltaksgjennomfoeringApiService, times(1)).settAvtalerTilJournalfoert(eq(jorurnalførteAvtaler));
    }
}
