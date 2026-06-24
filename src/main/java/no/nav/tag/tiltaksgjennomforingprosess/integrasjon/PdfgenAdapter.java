package no.nav.tag.tiltaksgjennomforingprosess.integrasjon;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tiltaksgjennomforingprosess.domene.PdfGenException;
import no.nav.tag.tiltaksgjennomforingprosess.domene.avtale.Avtale;
import no.nav.tag.tiltaksgjennomforingprosess.properties.PdfgenProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class PdfgenAdapter {
    private final PdfgenProperties pdfgenProperties;
    private final RestTemplate restTemplate;

    public byte[] genererPdf(Avtale avtale) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Avtale> request = new HttpEntity<>(avtale, headers);
            String url = pdfgenProperties.getUri() + "/tiltak-avtale/" + avtale.getTiltakstype().name().toLowerCase().replace("_", "-");
            return restTemplate.postForObject(url, request, byte[].class);
        } catch (Exception e) {
            log.error("Feil v/generering pdf av avtale-versjon {}", avtale.getAvtaleVersjonId(), e);
            throw new PdfGenException(e);
        }
    }
}
