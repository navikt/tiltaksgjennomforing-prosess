package no.nav.tag.tiltaksgjennomforingprosess.factory;

import lombok.RequiredArgsConstructor;
import no.nav.tag.tiltaksgjennomforingprosess.domene.avtale.Avtale;
import no.nav.tag.tiltaksgjennomforingprosess.properties.DokgenProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class DokgenAdapter {
    private final DokgenProperties dokGenProperties;
    private final RestTemplate restTemplate;

    public byte[] genererPdf(Avtale avtale) {
        return restTemplate.postForObject(dokGenProperties.getUri(), avtale, byte[].class);
    }
}
