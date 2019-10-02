package no.nav.tag.tiltaksgjennomforingprosess;

import no.nav.tag.tiltaksgjennomforingprosess.domene.avtale.Avtale;
import no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost.Journalpost;
import no.nav.tag.tiltaksgjennomforingprosess.factory.JournalpostFactory;
import no.nav.tag.tiltaksgjennomforingprosess.integrasjon.JoarkService;
import no.nav.tag.tiltaksgjennomforingprosess.integrasjon.StsService;
import no.nav.tag.tiltaksgjennomforingprosess.integrasjon.TiltaksgjennomfoeringApiService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.HttpServerErrorException;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class JournalpostJobbTest {

    @Mock
    private TiltaksgjennomfoeringApiService tiltaksgjennomfoeringApiService;

    @Mock
    private JournalpostFactory journalpostFactory;

    @Mock
    private JoarkService joarkService;

    @Mock
    private StsService stsService;

    @InjectMocks
    private JournalpostJobb journalpostJobb;

    @Test(expected = HttpServerErrorException.class)
    public void kallerIkkeJoarkHvisfeilet(){
        when(stsService.hentToken()).thenReturn("");
        when(tiltaksgjennomfoeringApiService.finnAvtalerTilJournalfoering(anyString())).thenReturn(Arrays.asList(TestData.opprettAvtale()));
        when(journalpostFactory.konverterTilJournalpost(any(Avtale.class))).thenThrow(HttpServerErrorException.class);

        journalpostJobb.avtalerTilJournalfoering();
        verify(joarkService, never()).sendJournalpost(anyString(), any(Journalpost.class));
        verify(tiltaksgjennomfoeringApiService, never()).settAvtalerTilJournalfoert(anyString(), anyMap());
    }

}
