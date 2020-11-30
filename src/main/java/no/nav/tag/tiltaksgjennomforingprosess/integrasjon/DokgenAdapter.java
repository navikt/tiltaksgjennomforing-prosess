package no.nav.tag.tiltaksgjennomforingprosess.integrasjon;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tiltaksgjennomforingprosess.domene.PdfGenException;
import no.nav.tag.tiltaksgjennomforingprosess.domene.avtale.Avtale;
import no.nav.tag.tiltaksgjennomforingprosess.properties.DokgenProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class DokgenAdapter {
    private final DokgenProperties dokGenProperties;
    private final RestTemplate restTemplate;

    public byte[] genererPdf(Avtale avtale) {
        try {
        return restTemplate.postForObject(dokGenProperties.getUri(), avtale, byte[].class);
        } catch (Exception e){
            log.error("Feil v/generering pdf av avtale-versjon " + avtale.getAvtaleVersjonId());
            throw new PdfGenException(e);
        }
    }
}
