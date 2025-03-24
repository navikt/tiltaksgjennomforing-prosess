package no.nav.tag.tiltaksgjennomforingprosess.persondata;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PersondataService {
    private final PersondataClient persondataClient;

    public PersondataService(PersondataClient persondataClient) {
        this.persondataClient = persondataClient;
    }

    public Diskresjonskode hentDiskresjonskode(String fnr) {
        return persondataClient.hentPersondata(fnr).utledDiskresjonskodeEllerUgradert();
    }

    public Map<String, Diskresjonskode> hentDiskresjonskoder(Set<String> fnrSet) {
        if (fnrSet.size() > 1000) {
            throw new IllegalArgumentException("Kan ikke hente diskresjonkode for mer enn 1000 om gangen");
        }

        return persondataClient.hentPersonBolk(fnrSet)
            .utledDiskresjonskoder(fnrSet)
            .entrySet()
            .stream()
            .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().orElse(Diskresjonskode.UGRADERT)));
    }
}
