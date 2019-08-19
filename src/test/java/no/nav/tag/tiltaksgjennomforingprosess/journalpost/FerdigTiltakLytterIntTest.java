package no.nav.tag.tiltaksgjennomforingprosess.journalpost;

import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tiltaksgjennomforingprosess.TestData;
import no.nav.tag.tiltaksgjennomforingprosess.domene.Avtale;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
@SpringBootTest
@Slf4j
@Ignore("Går i beina på EmbeddedKafkaBroker i KafkaConfig") //TODO
public class FerdigTiltakLytterIntTest {

    private static String TOPIC = "godkjentArbeidsAvtale";

    @Autowired
    private FerdigTiltakLytter ferdigTiltakLytter;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @ClassRule
    public static EmbeddedKafkaRule broker = new EmbeddedKafkaRule(1,true, TOPIC);

    @BeforeClass
    public static void setup() {
        System.setProperty("spring.kafka.bootstrap-servers", broker.getEmbeddedKafka().getBrokersAsString());
    }


    @Test
    public void lytterPaAvtaleOgSenderTilJoark() throws Exception {

        CountDownLatch countDownLatch = new CountDownLatch(1);
        ferdigTiltakLytter.setLatch(countDownLatch);

        Avtale avtale = TestData.opprettAvtale();
        String avtaleJson = TestData.avtaleTilJSON(avtale);

        kafkaTemplate.send(TOPIC, avtale.getId().toString(), avtaleJson);

        countDownLatch.await(3, TimeUnit.SECONDS);
        assertEquals(0, countDownLatch.getCount());
    }


}
