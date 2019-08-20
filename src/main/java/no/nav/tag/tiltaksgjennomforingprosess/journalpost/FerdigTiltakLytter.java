package no.nav.tag.tiltaksgjennomforingprosess.journalpost;

import lombok.Data;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
@Data
public class FerdigTiltakLytter {

    private CountDownLatch latch;

    private void countdownLatch(){
        if(latch != null){
            latch.countDown();
        }
    }

    @KafkaListener(topics = "godkjentArbeidsAvtale", groupId = "arbeidsAvtale")
    public void lyttPaFerdigAvtale(ConsumerRecord<?, ?> avtale){
        System.out.println("Henter " + avtale);
        this.countdownLatch();
    }
}
