package no.nav.tag.tiltaksgjennomforingprosess.persondata.domene;

public record Navn(
    String fornavn,
    String mellomnavn,
    String etternavn
) {
    public static final Navn TOMT_NAVN = new Navn("", "", "");
}
