package no.nav.tag.tiltaksgjennomforingprosess.persondata.domene;

public record Folkeregisteridentifikator(
    String identifikasjonsnummer,
    String status,
    String type
) {}
