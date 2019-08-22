package no.nav.tag.tiltaksgjennomforingprosess.journalpost;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tiltaksgjennomforingprosess.journalpost.jobb.JournalpostJobb;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

@Component
@Data
@Slf4j
public class FerdigTiltakLytter {

    @Autowired
    private JournalpostJobb journalpostJobb;

    private CountDownLatch latch; //TODO For test: Heller få til noe stubbing


    @KafkaListener(topics = "godkjentArbeidsAvtale", groupId = "tag-tiltak")
    public void lyttPaFerdigAvtale(ConsumerRecord<String, String> avtaleMelding){
        log.info("Henter avtale med id {}", avtaleMelding.key());
        try {
            journalpostJobb.prosesserAvtale(avtaleMelding.value());
        } catch (IOException e) {
            log.error("Feil ved deserialisering av avtale fra kafka kø - avtaleId=" + avtaleMelding.key(), e);
        } finally {
            this.countdownLatch();
        }
    }

    private void countdownLatch(){
        if(latch != null){
            latch.countDown();
        }
    }

}
