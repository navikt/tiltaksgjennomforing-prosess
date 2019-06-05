package no.nav.tag.tiltaksgjennomforingprosess.sts;

import no.nav.tag.tiltaksgjennomforingprosess.TestData;
import no.nav.tag.tiltaksgjennomforingprosess.domene.Avtale;
import no.nav.tag.tiltaksgjennomforingprosess.properties.StsProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpServerErrorException;

import java.net.URI;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
@DirtiesContext
public class StsServiceTest {

    private StsService stsService;

    @Autowired
    StsProperties stsProperties;

    @Autowired
    public void setStsService(StsService stsService){
        this.stsService = stsService;
    }

    @Test
    public void henter_token() {
        String token = stsService.hentToken();
        assertEquals("eyxXxx", token);
    }

    @Test(expected = HttpServerErrorException.class)
    public void hentToken_gir_status_500() {
        Avtale avtale = TestData.opprettAvtale();
        stsProperties.setUri(URI.create("http://localhost:8091"));
        stsService = new StsService(stsProperties);
        stsService.hentToken();
    }

}
