package no.nav.tag.tiltaksgjennomforingprosess.domene;


import lombok.Data;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@XmlAccessorType(XmlAccessType.FIELD)
@Data
@ToString
public class Avtale {

    private UUID id;
    private String deltakerFnr;
    private String bedriftNr;
    private String veilederNavIdent;
    private String opprettet;
    private Integer versjon;
    private String deltakerFornavn;
    private String deltakerEtternavn;
    private String deltakerTlf;
    private String bedriftNavn;
    private String arbeidsgiverFornavn;
    private String arbeidsgiverEtternavn;
    private String arbeidsgiverTlf;
    private String veilederFornavn;
    private String veilederEtternavn;
    private String veilederTlf;
    private String oppfolging;
    private String tilrettelegging;
    private Integer arbeidstreningLengde;
    private Integer arbeidstreningStillingprosent;
    private String journalpostId;
    private GodkjentPaVegneGrunn godkjentPaVegneGrunn;

    private String startDato;

    private List<Maal> maal = new ArrayList<>();

    private List<Oppgave> oppgaver = new ArrayList<>();
    private String godkjentAvDeltaker;
    private String godkjentAvArbeidsgiver;

    private String godkjentAvVeileder;
    private boolean godkjentPaVegneAv;
}


