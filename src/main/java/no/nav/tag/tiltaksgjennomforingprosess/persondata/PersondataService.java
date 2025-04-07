package no.nav.tag.tiltaksgjennomforingprosess.persondata;

import lombok.extern.slf4j.Slf4j;
import no.nav.security.token.support.client.core.ClientProperties;
import no.nav.security.token.support.client.core.oauth2.OAuth2AccessTokenService;
import no.nav.security.token.support.client.spring.ClientConfigurationProperties;
import no.nav.team_tiltak.felles.persondata.PersondataClient;
import no.nav.team_tiltak.felles.persondata.pdl.domene.Diskresjonskode;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PersondataService {
    private final PersondataClient persondataClient;

    public PersondataService(
        PersondataProperties persondataProperties,
        ClientConfigurationProperties clientConfigurationProperties,
        OAuth2AccessTokenService oAuth2AccessTokenService
    ) {
        ClientProperties clientProperties = clientConfigurationProperties.getRegistration().get("pdl-api");
        this.persondataClient = new PersondataClient(
            persondataProperties.getUri(),
            () -> Optional.ofNullable(clientProperties)
                .map(prop -> oAuth2AccessTokenService.getAccessToken(prop).getAccessToken())
                .orElse(null)
        );
    }

    public Map<String, Optional<Diskresjonskode>> hentDiskresjonskoder(Set<String> fnrSet) {
        return persondataClient.hentDiskresjonskoder(fnrSet);
    }
}
