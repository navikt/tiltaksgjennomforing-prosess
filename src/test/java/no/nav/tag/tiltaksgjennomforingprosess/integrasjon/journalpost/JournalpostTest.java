package no.nav.tag.tiltaksgjennomforingprosess.integrasjon.journalpost;

import no.nav.tag.tiltaksgjennomforingprosess.journalpost.request.Journalpost;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@RunWith(MockitoJUnitRunner.class)
public class JournalpostTest {

    private Journalpost journalpost;

    @Test
    @Ignore
    public void oppretter_journalpost() throws Exception {

         System.out.println(encodeToBase64());
    }

    private String encodeToBase64() throws Exception {
        Path fil = Paths.get(getClass().getClassLoader()
                .getResource("dummy.pdf").toURI());

        byte[] bytes = Files.readAllBytes(fil);
        return Base64.getEncoder().encodeToString(bytes);
    }

}
