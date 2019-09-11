package no.nav.tag.tiltaksgjennomforingprosess.journalpost.integrasjon.jobb;


import no.nav.tag.tiltaksgjennomforingprosess.journalpost.integrasjon.JoarkService;
import no.nav.tag.tiltaksgjennomforingprosess.journalpost.jobb.JournalpostJobb;
import no.nav.tag.tiltaksgjennomforingprosess.journalpost.request.Journalpost;
import no.nav.tag.tiltaksgjennomforingprosess.sts.StsService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JournalpostJobbTest {

    private final String dummyToken = "eyxXx";

    @Mock
    private JoarkService joarkService;

    @Mock
    private StsService stsService;

    @InjectMocks
    private JournalpostJobb journalpostJobb;

    @Test
    @Ignore
    public void oppretterJournalpost() throws InterruptedException {

        final String dummyJournalpostId = "12345";
        Journalpost journalpost = new Journalpost();

        //when(avtaleRepository.finnIkkeJournalfoerte()).thenReturn(Arrays.asList(avtale));
        when(stsService.hentToken()).thenReturn(dummyToken);
        when(joarkService.sendJournalpost(dummyToken, journalpost)).thenReturn(dummyJournalpostId);


      //  verify(avtaleRepository).save(eq(avtale));
      //  assertNotNull(avtale.getJournalpostId());
    }

}
