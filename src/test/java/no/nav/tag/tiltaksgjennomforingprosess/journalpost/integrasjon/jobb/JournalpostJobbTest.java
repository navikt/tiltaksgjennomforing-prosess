package no.nav.tag.tiltaksgjennomforingprosess.journalpost.integrasjon.jobb;

import no.nav.tag.tiltaksgjennomforingprosess.AvtaleRepository;
import no.nav.tag.tiltaksgjennomforingprosess.TestData;
import no.nav.tag.tiltaksgjennomforingprosess.domene.Avtale;
import no.nav.tag.tiltaksgjennomforingprosess.journalpost.integrasjon.JoarkService;
import no.nav.tag.tiltaksgjennomforingprosess.journalpost.jobb.JournalpostJobb;
import no.nav.tag.tiltaksgjennomforingprosess.sts.StsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JournalpostJobbTest {

    private final String dummyToken = "eyxXx";

    @Mock
    private JoarkService joarkService;

    @Mock
    private StsService stsService;

    @Mock
    private AvtaleRepository avtaleRepository;

    @InjectMocks
    private JournalpostJobb journalpostJobb;

    @Test
    public void oppretterJournalpost() throws InterruptedException {

        final String dummyJournalpostId = "12345";
        Avtale avtale = TestData.opprettAvtale();

        when(avtaleRepository.finnIkkeJournalfoerte()).thenReturn(Arrays.asList(avtale));
        when(stsService.hentToken()).thenReturn(dummyToken);
        when(joarkService.opprettOgSendJournalpost(dummyToken, avtale)).thenReturn(dummyJournalpostId);

        journalpostJobb.kjoerJobb();
        verify(avtaleRepository).save(eq(avtale));
        assertNotNull(avtale.getJournalpostId());
    }

}
