package no.nav.tag.tiltaksgjennomforingprosess.domene.avtale;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Data
@ToString
public class Avtale {
    private final static BigDecimal HUNDRE = BigDecimal.valueOf(100);
    private final static Double DEFAULT_OTP = 2.0;

    private Tiltakstype tiltakstype;
    private UUID avtaleId;
    private UUID avtaleVersjonId;
    private String deltakerFnr;
    private String bedriftNr;
    private String veilederNavIdent;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate opprettet;
    private Integer opprettetAar;
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
    private BigDecimal stillingprosent;
    private BigDecimal antallDagerPerUke;
    private String enhetOppfolging;
    private Integer versjon;
    private String mentorFornavn;
    private String mentorEtternavn;
    private String mentorOppgaver;
    private Double mentorAntallTimer;
    private Integer mentorTimelonn;
    private String mentorFnr;

    private GodkjentPaVegneGrunn godkjentPaVegneGrunn;
    private String arbeidsgiverKontonummer;
    private String stillingstype;
    private String arbeidsoppgaver;
    private Integer lonnstilskuddProsent;
    private Integer manedslonn;
    private BigDecimal feriepengesats;
    private Integer feriepengerBelop;
    private BigDecimal arbeidsgiveravgift;
    private Integer arbeidsgiveravgiftBelop;
    private Double otpSats;
    private Integer otpBelop;
    private Integer sumLonnsutgifter;
    private Integer sumLonnstilskudd;
    private Integer manedslonn100pst;
    private List<TilskuddsPeriode> tilskuddsPerioder = new ArrayList<>();
    private Boolean harFamilietilknytning;
    private String familietilknytningForklaring;

    // Inkluderingstilskudd
    private List<Inkluderingstilskuddsutgift> inkluderingstilskuddsutgift = new ArrayList<>();
    private String inkluderingstilskuddBegrunnelse;

    public BigDecimal getFeriepengesats() {
        return Optional.ofNullable(this.feriepengesats)
                .orElse(BigDecimal.ZERO)
                .multiply(HUNDRE);
    }

    public BigDecimal getArbeidsgiveravgift() {
        return Optional.ofNullable(this.arbeidsgiveravgift)
                .orElse(BigDecimal.ZERO)
                .multiply(HUNDRE);
    }

    public Double getOtpSats() {
        return Optional.ofNullable(this.otpSats)
                .map(otp -> otp * 100)
                .orElse(DEFAULT_OTP);
    }

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
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate godkjentTaushetserklæringAvMentor;

    private boolean godkjentPaVegneAv;

    public boolean erNyVersjon() {
        return this.versjon > 1;
    }

    public void utførSladdingAvFelter() {
        this.deltakerFnr = "***********";
        this.deltakerTlf = "***********";
        this.deltakerEtternavn = "***********";
        this.deltakerFornavn = "***********";
    }
}
