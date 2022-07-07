package no.nav.tag.tiltaksgjennomforingprosess.domene.avtale;

import lombok.Getter;

@Getter
public enum Tiltakstype {

    ARBEIDSTRENING("Arbeidstrening", "ab0422", "Avtale om arbeidstrening", "tiltak-arbtren"),
    MIDLERTIDIG_LONNSTILSKUDD("LonnstilskuddMidlertidig", "ab0336", "Avtale om midlertidig lønnstilskudd", "tiltak-midl-lønnst"),
    VARIG_LONNSTILSKUDD("LonnstilskuddVarig", "ab0337", "Avtale om varig lønnstilskudd", "tiltak-varig-lønnst"),
    MENTOR("mentor", "ab0416", "Avtale om tilskudd til mentor", "tiltak-mentor"),
    INKLUDERINGSTILSKUDD("Inkluderingstilskudd", "ab0417", "Avtale om inkluderingstilskudd", "tiltak-inkl-tilsk"),

    // Sjekke hva som er riktig behandlingstema og brevkode
    SOMMERJOBB("Sommerjobb", "", "Avtale om sommerjobb", "tiltak-sommerjobb");

    private final String dokforTiltakskodeSkjema;
    private final String behandlingstema;
    private final String tittel;
    private final String brevkode;

    Tiltakstype(String tiltaksType, String behandlingstema, String tittel, String brevkode) {
        this.dokforTiltakskodeSkjema = tiltaksType;
        this.behandlingstema = behandlingstema;
        this.tittel = tittel;
        this.brevkode = brevkode;
    }
}
