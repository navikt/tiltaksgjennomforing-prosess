package no.nav.tag.tiltaksgjennomforingprosess.persondata.domene;

public record HentPersonBolk(
    String ident,
    HentPerson person,
    String code
) {
    private static final String OK = "ok";
    public boolean isOk() {
        return OK.equals(code);
    }
}
