package no.nav.tag.tiltaksgjennomforingprosess.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.test.EmbeddedKafkaBroker;

@Configuration
@Profile("dev")
@Slf4j
public class KafkaConfig {

    private final String topic = "privat-tiltaksgjennomforing-godkjentAvtale";

    @Autowired
    public EmbeddedKafkaBroker kafkaBroker() {
        log.info("Starter embedded Kafka");
        EmbeddedKafkaBroker embeddedKafka = new EmbeddedKafkaBroker(1, true, topic);
        System.setProperty("spring.kafka.bootstrap-servers", embeddedKafka.getBrokersAsString());
        embeddedKafka.afterPropertiesSet();
        return embeddedKafka;
    }

}
