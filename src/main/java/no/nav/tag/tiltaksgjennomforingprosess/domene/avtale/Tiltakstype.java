package no.nav.tag.tiltaksgjennomforingprosess.domene.avtale;

import lombok.Getter;

@Getter
public enum Tiltakstype {
    ARBEIDSTRENING("Arbeidstrening", "ab0422", "Avtale om arbeidstrening"),
    MIDLERTIDIG_LONNSTILSKUDD("Midlertidig lønnstilskudd", "ab0336", "Avtale om lønnstilskudd"),
    VARIG_LONNSTILSKUDD("Varig lønnstilskudd", "ab0337", "Avtale om lønnstilskudd"),
    MENTOR("Mentor", "ab0416", "Avtale om mentortilskudd");

    private final String tiltaksType;
    private final String behandlingstema;
    private final String tittel;

    Tiltakstype(String tiltaksType, String behandlingstema, String tittel) {
        this.tiltaksType = tiltaksType;
        this.behandlingstema = behandlingstema;
        this.tittel = tittel;
    }
}