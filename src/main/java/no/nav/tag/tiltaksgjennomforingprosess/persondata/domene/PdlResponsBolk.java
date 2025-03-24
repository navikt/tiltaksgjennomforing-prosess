package no.nav.tag.tiltaksgjennomforingprosess.persondata.domene;

import no.nav.tag.tiltaksgjennomforingprosess.persondata.Diskresjonskode;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public record PdlResponsBolk(Data data) {
    public record Data(List<HentPersonBolk> hentPersonBolk) {}

    public Map<String, Optional<Diskresjonskode>> utledDiskresjonskoder(Set<String> fnrSet) {
        Map<String, Optional<Diskresjonskode>> diskresjonskodeMap = data().hentPersonBolk().stream()
            .filter(HentPersonBolk::isOk)
            .flatMap(person -> person.person().folkeregisteridentifikator().stream().map(a -> Map.entry(
                a.identifikasjonsnummer(),
                PdlRespons.utledAdressebeskyttelse(person.person()).map(Adressebeskyttelse::getGradering)
            )))
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (a, b) -> a.isEmpty() ? b : a
            ));

        return fnrSet.stream().collect(Collectors.toMap(
            fnr -> fnr,
            fnr -> diskresjonskodeMap.getOrDefault(fnr, Optional.empty())
        ));
    }
}
