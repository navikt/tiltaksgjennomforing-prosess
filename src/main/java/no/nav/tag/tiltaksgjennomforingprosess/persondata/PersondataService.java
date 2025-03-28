package no.nav.tag.tiltaksgjennomforingprosess.persondata;

import lombok.extern.slf4j.Slf4j;
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

    public PersondataService(PersondataClient persondataClient) {
        this.persondataClient = persondataClient;
    }

    public Map<String, Diskresjonskode> hentDiskresjonskoder(Set<String> fnrSet) {
        if(fnrSet.isEmpty()) {
            return Collections.emptyMap();
        }

        if (fnrSet.size() > 1000) {
            throw new IllegalArgumentException("Kan ikke hente diskresjonkode for mer enn 1000 om gangen");
        }

        Map<String, Optional<Diskresjonskode>> diskresjonskodeOptFraPdl = persondataClient
                .hentPersonBolk(fnrSet)
                .utledDiskresjonskoder(fnrSet);

        Map<String, Diskresjonskode> diskresjonskodeFraPdl = diskresjonskodeOptFraPdl.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().orElse(Diskresjonskode.UGRADERT)
                ));

        return diskresjonskodeFraPdl;
    }
}
