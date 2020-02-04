package no.nav.tag.tiltaksgjennomforingprosess.domene.avtale;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@ToString
public class Avtale {
    private Tiltakstype tiltakstype = Tiltakstype.ARBEIDSTRENING;
    private UUID avtaleId;
    private UUID avtaleVersjonId;
    private String deltakerFnr;
    private String bedriftNr;
    private String veilederNavIdent;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate opprettet;
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
    private Integer stillingprosent;
    private Integer versjon;
    private GodkjentPaVegneGrunn godkjentPaVegneGrunn;
    private String arbeidsgiverKontonummer;
    private String stillingstype;
    private String arbeidsoppgaver;
    private Integer lonnstilskuddProsent;
    private Integer manedslonn;
    private BigDecimal feriepengesats;
    private BigDecimal arbeidsgiveravgift;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate sluttDato;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDato;

    @XStreamImplicit(itemFieldName = "maal")
    private List<Maal> maal = new ArrayList<>();

    @XStreamImplicit(itemFieldName = "oppgave")
    private List<Oppgave> oppgaver = new ArrayList<>();

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate godkjentAvDeltaker;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate godkjentAvArbeidsgiver;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate godkjentAvVeileder;

    private boolean godkjentPaVegneAv;

    public boolean erNyVersjon(){
        return this.versjon > 1;
    }
}


