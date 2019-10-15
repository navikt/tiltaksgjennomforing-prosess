package no.nav.tag.tiltaksgjennomforingprosess;

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
    public void kallerIkkeJoarkMedEnAvtaleSomFeilet(){

        Avtale okAvtale = TestData.opprettAvtale();
        okAvtale.setId(okAvtale.getId());

        Journalpost okJournalpost = new Journalpost();
        okJournalpost.setEksternReferanseId(okAvtale.getId().toString());

        Avtale feiletAvt = TestData.opprettAvtale();

        Map<UUID, String> jorurnalpostIds = new HashMap<>(2);
        final String JOURNALPOST_ID = "1234";
        jorurnalpostIds.put(okAvtale.getId(), JOURNALPOST_ID);
        jorurnalpostIds.put(feiletAvt.getId(), MAPPING_FEIL);

        when(tiltaksgjennomfoeringApiService.finnAvtalerTilJournalfoering()).thenReturn(Arrays.asList(okAvtale, feiletAvt));

        when(journalpostFactory.konverterTilJournalpost(okAvtale)).thenReturn(okJournalpost);
        when(journalpostFactory.konverterTilJournalpost(feiletAvt)).thenThrow(RuntimeException.class);

        when(joarkService.sendJournalpost(eq(okJournalpost))).thenReturn(JOURNALPOST_ID);

        journalpostJobb.avtalerTilJournalfoering();
        verify(joarkService, times(1)).sendJournalpost(eq(okJournalpost));
        verify(tiltaksgjennomfoeringApiService, atLeastOnce()).settAvtalerTilJournalfoert(eq(jorurnalpostIds));
    }

    @Test
    public void kallerMedAlleAvtaler(){

        Avtale avtale1 = TestData.opprettAvtale();
        Avtale avtale2 = TestData.opprettAvtale();

        Journalpost journalpost1 = new Journalpost();
        journalpost1.setEksternReferanseId(avtale1.getId().toString());
        Journalpost journalpost2 = new Journalpost();
        journalpost2.setEksternReferanseId(avtale2.getId().toString());


        Map<UUID, String> jorurnalpostIds = new HashMap<>(2);
        jorurnalpostIds.put(avtale1.getId(), avtale1.getId().toString());
        jorurnalpostIds.put(avtale2.getId(), avtale2.getId().toString());

        when(tiltaksgjennomfoeringApiService.finnAvtalerTilJournalfoering()).thenReturn(Arrays.asList(avtale1, avtale2));

        when(journalpostFactory.konverterTilJournalpost(avtale1)).thenReturn(journalpost1);
        when(journalpostFactory.konverterTilJournalpost(avtale2)).thenReturn(journalpost2);

        when(joarkService.sendJournalpost(eq(journalpost1))).thenReturn(avtale1.getId().toString());
        when(joarkService.sendJournalpost(eq(journalpost2))).thenReturn(avtale2.getId().toString());

        journalpostJobb.avtalerTilJournalfoering();
        verify(joarkService, times(1)).sendJournalpost(eq(journalpost1));
        verify(joarkService, times(1)).sendJournalpost(eq(journalpost2));
        verify(tiltaksgjennomfoeringApiService, times(1)).settAvtalerTilJournalfoert(eq(jorurnalpostIds));
    }
}
