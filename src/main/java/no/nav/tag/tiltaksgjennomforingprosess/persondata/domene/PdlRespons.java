package no.nav.tag.tiltaksgjennomforingprosess.persondata.domene;

import no.nav.tag.tiltaksgjennomforingprosess.persondata.Diskresjonskode;

import java.util.NoSuchElementException;
import java.util.Optional;

public record PdlRespons(Data data) {
    public record Data(
        HentPerson hentPerson,
        HentIdenter hentIdenter,
        HentGeografiskTilknytning hentGeografiskTilknytning
    ) { }

    public Diskresjonskode utledDiskresjonskodeEllerUgradert() {
        return utledDiskresjonskode().orElse(Diskresjonskode.UGRADERT);
    }

    public Optional<Diskresjonskode> utledDiskresjonskode() {
        return Optional.ofNullable(data())
            .flatMap(data -> utledAdressebeskyttelse(data.hentPerson()))
            .map(Adressebeskyttelse::getGradering);
    }

    public static Optional<Adressebeskyttelse> utledAdressebeskyttelse(HentPerson hentPerson) {
        try {
            return Optional.of(hentPerson.adressebeskyttelse().getFirst());
        } catch (NullPointerException | NoSuchElementException e) {
            return Optional.empty();
        }
    }
}
