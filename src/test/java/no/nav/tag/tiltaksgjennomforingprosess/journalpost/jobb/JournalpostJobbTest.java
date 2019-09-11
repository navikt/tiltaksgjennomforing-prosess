package no.nav.tag.tiltaksgjennomforingprosess.journalpost.jobb;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@Ignore("Manuell")
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
@DirtiesContext
public class JournalpostJobbTest {

    @Autowired
    private JournalpostJobb journalpostJobb;

    @Test
    public void kjoerJobb(){
        journalpostJobb.JournalfoerAvtaler();
    }
}
