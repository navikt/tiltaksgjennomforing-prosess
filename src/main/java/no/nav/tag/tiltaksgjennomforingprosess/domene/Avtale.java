package no.nav.tag.tiltaksgjennomforingprosess.domene;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;
import no.nav.tag.tiltaksgjennomforingprosess.journalpost.factory.LocalDateAdapter;
import no.nav.tag.tiltaksgjennomforingprosess.journalpost.factory.LocalDateTimeAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@XmlAccessorType(XmlAccessType.FIELD)
@Data
@ToString
public class Avtale {

    final static String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private String deltakerFnr;
    private String bedriftNr;
    private String veilederNavIdent;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIME_PATTERN)
    private LocalDateTime opprettetTidspunkt;
    private UUID id;
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
    private GodkjentPaVegneGrunn godkjentPaVegneGrunn;
    private Integer arbeidstreningLengde;
    private Integer arbeidstreningStillingprosent;
    private String journalpostId;

    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    private LocalDate startDato;

    private List<Maal> maal = new ArrayList<>();

    private List<Oppgave> oppgaver = new ArrayList<>();

    @XmlJavaTypeAdapter(value = LocalDateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIME_PATTERN)
    private LocalDateTime godkjentAvDeltaker;

    @XmlJavaTypeAdapter(value = LocalDateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIME_PATTERN)
    private LocalDateTime godkjentAvArbeidsgiver;

    @XmlJavaTypeAdapter(value = LocalDateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIME_PATTERN)
    private LocalDateTime godkjentAvVeileder;

    private boolean godkjentPaVegneAv;
}


