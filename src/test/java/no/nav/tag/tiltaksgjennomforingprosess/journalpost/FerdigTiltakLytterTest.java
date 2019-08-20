package no.nav.tag.tiltaksgjennomforingprosess.journalpost;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
public class FerdigTiltakLytterTest {

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
    public void testLytter() throws InterruptedException {

        ferdigTiltakLytter.setLatch(new CountDownLatch(1));

        String melding = "TestMelding";
        kafkaTemplate.send(TOPIC, melding);

        ferdigTiltakLytter.getLatch().await(3l, TimeUnit.SECONDS);

        assertEquals(0, ferdigTiltakLytter.getLatch().getCount());

    }




}
