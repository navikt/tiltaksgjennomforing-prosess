package no.nav.tag.tiltaksgjennomforingprosess.journalpost.jobb;

import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tiltaksgjennomforingprosess.AvtaleRepository;
import no.nav.tag.tiltaksgjennomforingprosess.domene.Avtale;
import no.nav.tag.tiltaksgjennomforingprosess.journalpost.integrasjon.JoarkService;
import no.nav.tag.tiltaksgjennomforingprosess.sts.StsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;

@Slf4j
@Component
public class JournalpostJobb {

    @Autowired
    private JoarkService joarkService;

    @Autowired
    private StsService stsService;

    @Autowired
    private AvtaleRepository avtaleRepository;

    @Scheduled(cron = "${journalpost.jobb.cron}")
    public void kjoerJobb() {
        List<Avtale> avtaler = avtaleRepository.finnIkkeJournalfoerte();

        if(avtaler.isEmpty()){
            return;
        }

        log.info("Hentet {} avtaler", avtaler.size());
        String token = stsService.hentToken();

        //TODO Test hvordan integrasjon håndterer parallelle kall
        avtaler.parallelStream().forEach(avtale -> {
            try {
                String jornalpostId = joarkService.opprettOgSendJournalpost(token, avtale);
                avtale.setJournalpostId(jornalpostId);
                avtaleRepository.save(avtale);
            } catch (HttpServerErrorException e){
                log.error("Feil ved journalføring av avtale {}: {}", avtale.getId(), e.getMessage());
            }

        });

    }
}


