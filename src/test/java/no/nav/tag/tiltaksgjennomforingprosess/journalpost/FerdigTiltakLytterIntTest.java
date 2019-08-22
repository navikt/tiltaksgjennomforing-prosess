package no.nav.tag.tiltaksgjennomforingprosess.journalpost;

import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tiltaksgjennomforingprosess.TestData;
import no.nav.tag.tiltaksgjennomforingprosess.domene.Avtale;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@Profile("dev")
@SpringBootTest
@DirtiesContext
@Slf4j
public class FerdigTiltakLytterIntTest {

    private static String TOPIC = "godkjentArbeidsAvtale";

    @ClassRule
    public static EmbeddedKafkaRule embeddedKafkaRule = new EmbeddedKafkaRule(1,true, TOPIC);

    @Autowired
    private FerdigTiltakLytter ferdigTiltakLytter;

    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    private KafkaTemplate<String, String> kafkaTemplate;

    @BeforeClass
    public static void setup() {
        System.setProperty("spring.kafka.bootstrap-servers", embeddedKafkaRule.getEmbeddedKafka().getBrokersAsString());
    }

    @Before
    public void setUp(){
        Map<String, Object> senderProps = KafkaTestUtils.senderProps(embeddedKafkaRule.getEmbeddedKafka().getBrokersAsString());
        senderProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        senderProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        DefaultKafkaProducerFactory producerFactory = new DefaultKafkaProducerFactory(senderProps);
        kafkaTemplate = new KafkaTemplate<>(producerFactory);
        kafkaTemplate.setDefaultTopic(TOPIC);
        kafkaListenerEndpointRegistry.getListenerContainers()
                .forEach(messageListenerContainer ->
                        ContainerTestUtils.waitForAssignment(messageListenerContainer, embeddedKafkaRule.getEmbeddedKafka().getPartitionsPerTopic()));
    }


    @Test
    public void lytterPaAvtale() throws Exception {

        CountDownLatch countDownLatch = new CountDownLatch(1);
        ferdigTiltakLytter.setLatch(countDownLatch);

        Avtale avtale = TestData.opprettAvtale();
        String avtaleJson = TestData.avtaleTilJSON(avtale);

        kafkaTemplate.send(TOPIC, avtale.getId().toString(), avtaleJson);

        countDownLatch.await(3, TimeUnit.SECONDS);
        assertEquals(0, countDownLatch.getCount());
    }


}