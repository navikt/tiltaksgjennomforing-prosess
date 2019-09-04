package no.nav.tag.tiltaksgjennomforingprosess.journalpost.integrasjon;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.matching.ContainsPattern;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
@DirtiesContext
public class TiltaksgjennomforingApiIntTest {

    @ClassRule
    public static WireMockRule wireMockRule = new WireMockRule(8091); //Fordi mvn install gir SocketException med embedded MockServer :(

    @Autowired
    TiltaksgjennomfoeringApiService service;

    private final String TOKEN = "eyxXxx";

    @Test
    public void setterAvtalerTilJournalfoert() {

        stubFor(put(urlPathMatching("/internal/avtaler"))
                .withHeader("Authorization", new ContainsPattern("Bearer eyXxx"))
                .withRequestBody(new ContainsPattern("c34250d3-d68f-4389-b4ff-13a73032212e : 1"))
                .withRequestBody(new ContainsPattern("c18abcb0-e1b4-43af-be51-7bfeced2efe5 : 2"))
                .willReturn(aResponse()
                        .withStatus(200)));

        Map<UUID, String> avtalerTilJournalfoert = new HashMap<>();
        avtalerTilJournalfoert.put(UUID.fromString("c34250d3-d68f-4389-b4ff-13a73032212e"), "1");
        avtalerTilJournalfoert.put(UUID.fromString("c18abcb0-e1b4-43af-be51-7bfeced2efe5"), "2");

        try {
            service.settAvtalerTilJournalfoert(TOKEN, avtalerTilJournalfoert);
        } catch (Exception e) {
            fail(e.getMessage());
        }

    }

}
