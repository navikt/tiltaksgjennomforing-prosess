package no.nav.tag.tiltaksgjennomforingprosess.domene.avtale;

import lombok.Getter;

@Getter
public enum Tiltakstype {

    ARBEIDSTRENING("Arbeidstrening", "ab0422", "Avtale om arbeidstrening", "tiltak-arbtren", true),
    MIDLERTIDIG_LONNSTILSKUDD("LonnstilskuddMidlertidig", "ab0336", "Avtale om midlertidig lønnstilskudd", "tiltak-midl-lønnst", false),
    VARIG_LONNSTILSKUDD("LonnstilskuddVarig", "ab0337", "Avtale om varig lønnstilskudd", "tiltak-varig-lønnst", false),
    MENTOR("mentor", "ab0416", "Avtale om tilskudd til mentor", "tiltak-mentor", true),
    INKLUDERINGSTILSKUDD("inkluderingstilskudd", "ab0417", "Avtale om inkluderingstilskudd", "tiltak-inkl-tilsk", true),

    // Sjekke hva som er riktig behandlingstema og brevkode
    SOMMERJOBB("Sommerjobb", "", "Avtale om sommerjobb", "tiltak-sommerjobb", false),
    VTAO("vtao", "ab0418", "Avtale om varig tilrettelagt arbeid i ordinær virksomhet", "tiltak-vtao", true);

    private final String dokforTiltakskodeSkjema;
    private final String behandlingstema;
    private final String tittel;
    private final String brevkode;
    public boolean skalTilArena;

    Tiltakstype(String tiltaksType, String behandlingstema, String tittel, String brevkode, boolean skalTilArena) {
        this.dokforTiltakskodeSkjema = tiltaksType;
        this.behandlingstema = behandlingstema;
        this.tittel = tittel;
        this.brevkode = brevkode;
        this.skalTilArena = skalTilArena;
    }
}
