package no.nav.tag.tiltaksgjennomforingprosess.domene.avtale;

import lombok.Getter;

@Getter
public enum Tiltakstype {

    ARBEIDSTRENING("Arbeidstrening", "ab0422", "Avtale om arbeidstrening"),
    MIDLERTIDIG_LONNSTILSKUDD("LonnstilskuddMidlertidig", "ab0336", "Avtale om midlertidig lønnstilskudd"),
    VARIG_LONNSTILSKUDD("LonnstilskuddVarig", "ab0337", "Avtale om varig lønnstilskudd"),
    MENTOR("mentor", "ab0416", "Avtale om tilskudd til mentor");

    private final String dokforTiltakskodeSkjema;
    private final String behandlingstema;
    private final String tittel;

    Tiltakstype(String tiltaksType, String behandlingstema, String tittel) {
        this.dokforTiltakskodeSkjema = tiltaksType;
        this.behandlingstema = behandlingstema;
        this.tittel = tittel;
    }
}
