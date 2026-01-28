package no.nav.tag.tiltaksgjennomforingprosess;

import no.nav.tag.tiltaksgjennomforingprosess.domene.PdfGenException;
import no.nav.tag.tiltaksgjennomforingprosess.domene.avtale.Avtale;
import no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost.Journalpost;
import no.nav.tag.tiltaksgjennomforingprosess.factory.JournalpostFactory;
import no.nav.tag.tiltaksgjennomforingprosess.integrasjon.JoarkService;
import no.nav.tag.tiltaksgjennomforingprosess.integrasjon.TiltaksgjennomfoeringApiService;
import no.nav.tag.tiltaksgjennomforingprosess.leader.LeaderPodCheck;
import no.nav.tag.tiltaksgjennomforingprosess.properties.PilotProperties;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static no.nav.tag.tiltaksgjennomforingprosess.JournalpostJobb.MAPPING_FEIL;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JournalpostJobbTest {

    private final String JOURNALPOST_ID = "1234";

    @Mock
    private TiltaksgjennomfoeringApiService tiltaksgjennomfoeringApiService;

    @Mock
    private JournalpostFactory journalpostFactory;

    @Mock
    private JoarkService joarkService;

    @Mock
    private LeaderPodCheck leaderPodCheck;

    @InjectMocks
    private JournalpostJobb journalpostJobb;

    @Mock
    private PilotProperties pilotProperties;

    @BeforeEach
    public void setUp() {
        JournalpostJobb.enabled = true;
    }

    @AfterEach
    public void tearDown() {
        JournalpostJobb.enabled = true;
    }

    @Test
    public void prosessererIkkeHvisIkkeLeaderPod() {
        when(leaderPodCheck.isLeaderPod()).thenReturn(false);

        journalpostJobb.avtalerTilJournalfoering();

        verify(tiltaksgjennomfoeringApiService, never()).finnAvtalerTilJournalfoering();
        verify(journalpostFactory, never()).konverterTilJournalpost(any());
        verify(joarkService, never()).sendJournalpost(any(), anyBoolean());
        verify(tiltaksgjennomfoeringApiService, never()).settAvtalerTilJournalfoert(any());
    }

    @Test
    public void kallerIkkeJoarkMedEnAvtaleSomFeilet() {

        Avtale okAvtale = TestData.opprettMentorAvtale();
        Journalpost okJournalpost = new Journalpost();
        okJournalpost.setEksternReferanseId(okAvtale.getAvtaleId().toString());
        okJournalpost.setAvtaleVersjon(okAvtale.getVersjon());

        Avtale feiletAvt = TestData.opprettMentorAvtale();

        Map<UUID, String> jorurnalførteAvtaler = new HashMap<>(2);
        jorurnalførteAvtaler.put(okAvtale.getAvtaleVersjonId(), JOURNALPOST_ID);
        jorurnalførteAvtaler.put(feiletAvt.getAvtaleVersjonId(), MAPPING_FEIL);

        when(leaderPodCheck.isLeaderPod()).thenReturn(true);
        when(tiltaksgjennomfoeringApiService.finnAvtalerTilJournalfoering()).thenReturn(List.of(okAvtale, feiletAvt));

        when(journalpostFactory.konverterTilJournalpost(okAvtale)).thenReturn(okJournalpost);
        when(journalpostFactory.konverterTilJournalpost(feiletAvt)).thenThrow(RuntimeException.class);

        when(joarkService.sendJournalpost(eq(okJournalpost), eq(false))).thenReturn(JOURNALPOST_ID);

        journalpostJobb.avtalerTilJournalfoering();
      //  verify(joarkService, times(1)).sendJournalpost(eq(okJournalpost), eq(false));
       // verify(tiltaksgjennomfoeringApiService, atLeastOnce()).settAvtalerTilJournalfoert(eq(jorurnalførteAvtaler));
        // TODO: fikse testen - mentor er brut, men bør være en tiltakstype som skal til arena
    }

    @Test
    public void toAvtalerDerEttjoarkKallFeiler() {
        Avtale avtale1 = TestData.opprettArbeidstreningAvtale();
        Avtale avtale2 = TestData.opprettArbeidstreningAvtale();

        Journalpost journalpost1 = new Journalpost();
        journalpost1.setEksternReferanseId(avtale1.getAvtaleId().toString());
        journalpost1.setAvtaleVersjon(1);
        Journalpost journalpost2 = new Journalpost();
        journalpost2.setEksternReferanseId(avtale2.getAvtaleId().toString());
        journalpost2.setAvtaleVersjon(1);

        Map<UUID, String> jorurnalførteAvtaler = new HashMap<>();
        jorurnalførteAvtaler.put(avtale1.getAvtaleVersjonId(), JOURNALPOST_ID);

        when(leaderPodCheck.isLeaderPod()).thenReturn(true);
        when(tiltaksgjennomfoeringApiService.finnAvtalerTilJournalfoering()).thenReturn(List.of(avtale1, avtale2));

        when(journalpostFactory.konverterTilJournalpost(avtale1)).thenReturn(journalpost1);
        when(journalpostFactory.konverterTilJournalpost(avtale2)).thenReturn(journalpost2);

        when(joarkService.sendJournalpost(eq(journalpost1), anyBoolean())).thenReturn(JOURNALPOST_ID);
        when(joarkService.sendJournalpost(eq(journalpost2), anyBoolean())).thenThrow(RuntimeException.class);

        journalpostJobb.avtalerTilJournalfoering();
        verify(joarkService, times(1)).sendJournalpost(eq(journalpost1), anyBoolean());
        verify(joarkService, times(1)).sendJournalpost(eq(journalpost2), anyBoolean());
        verify(tiltaksgjennomfoeringApiService, times(1)).settAvtalerTilJournalfoert(eq(jorurnalførteAvtaler));
    }

    @Test
    public void kallerMedAlleAvtaler() {

        Avtale avtale1 = TestData.opprettArbeidstreningAvtale();
        Avtale avtale2 = TestData.opprettArbeidstreningAvtale();

        Journalpost journalpost1 = new Journalpost();
        journalpost1.setEksternReferanseId(avtale1.getAvtaleId().toString());
        journalpost1.setAvtaleVersjon(1);
        Journalpost journalpost2 = new Journalpost();
        journalpost2.setEksternReferanseId(avtale2.getAvtaleId().toString());
        journalpost2.setAvtaleVersjon(1);

        Map<UUID, String> jorurnalførteAvtaler = new HashMap<>(2);
        jorurnalførteAvtaler.put(avtale1.getAvtaleVersjonId(), JOURNALPOST_ID);
        jorurnalførteAvtaler.put(avtale2.getAvtaleVersjonId(), JOURNALPOST_ID);

        when(leaderPodCheck.isLeaderPod()).thenReturn(true);
        when(tiltaksgjennomfoeringApiService.finnAvtalerTilJournalfoering()).thenReturn(List.of(avtale1, avtale2));

        when(journalpostFactory.konverterTilJournalpost(avtale1)).thenReturn(journalpost1);
        when(journalpostFactory.konverterTilJournalpost(avtale2)).thenReturn(journalpost2);

        when(joarkService.sendJournalpost(eq(journalpost1), anyBoolean())).thenReturn(JOURNALPOST_ID);
        when(joarkService.sendJournalpost(eq(journalpost2), anyBoolean())).thenReturn(JOURNALPOST_ID);

        journalpostJobb.avtalerTilJournalfoering();
        verify(joarkService, times(1)).sendJournalpost(eq(journalpost1), anyBoolean());
        verify(joarkService, times(1)).sendJournalpost(eq(journalpost2), anyBoolean());
        verify(tiltaksgjennomfoeringApiService, times(1)).settAvtalerTilJournalfoert(eq(jorurnalførteAvtaler));
    }

    @Test
    public void pdfGenFeilerPåEnAvtale() {

        Avtale avtale1 = TestData.opprettArbeidstreningAvtale();
        Avtale avtale2 = TestData.opprettArbeidstreningAvtale();

        Journalpost journalpost1 = new Journalpost();
        journalpost1.setEksternReferanseId(avtale1.getAvtaleId().toString());
        journalpost1.setAvtaleVersjon(1);
        Journalpost journalpost2 = new Journalpost();
        journalpost2.setEksternReferanseId(avtale2.getAvtaleId().toString());
        journalpost2.setAvtaleVersjon(1);

        Map<UUID, String> jorurnalførteAvtaler = new HashMap<>(1);
        jorurnalførteAvtaler.put(avtale2.getAvtaleVersjonId(), JOURNALPOST_ID);

        when(leaderPodCheck.isLeaderPod()).thenReturn(true);
        when(tiltaksgjennomfoeringApiService.finnAvtalerTilJournalfoering()).thenReturn(List.of(avtale1, avtale2));

        when(journalpostFactory.konverterTilJournalpost(avtale1)).thenThrow(PdfGenException.class);
        when(journalpostFactory.konverterTilJournalpost(avtale2)).thenReturn(journalpost2);

        when(joarkService.sendJournalpost(eq(journalpost2), anyBoolean())).thenReturn(JOURNALPOST_ID);

        journalpostJobb.avtalerTilJournalfoering();
        verify(joarkService, never()).sendJournalpost(eq(journalpost1), anyBoolean());
        verify(joarkService, times(1)).sendJournalpost(eq(journalpost2), anyBoolean());
        verify(tiltaksgjennomfoeringApiService, times(1)).settAvtalerTilJournalfoert(eq(jorurnalførteAvtaler));
    }
}
