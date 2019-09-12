package no.nav.tag.tiltaksgjennomforingprosess.domene.avtale;


import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@ToString
public class Avtale {

    public final static String DATOFORMAT_NORGE = "dd.MM.YYYY";

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

    private LocalDate startDato;

    @XStreamImplicit(itemFieldName="maal")
    private List<Maal> maal = new ArrayList<>();

    @XStreamImplicit(itemFieldName="oppgave")
    private List<Oppgave> oppgaver = new ArrayList<>();

    private String godkjentAvDeltaker;
    private String godkjentAvArbeidsgiver;

    private String godkjentAvVeileder;
    private boolean godkjentPaVegneAv;
}


