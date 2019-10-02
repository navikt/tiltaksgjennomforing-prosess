package no.nav.tag.tiltaksgjennomforingprosess;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import no.nav.tag.tiltaksgjennomforingprosess.domene.avtale.Avtale;
import no.nav.tag.tiltaksgjennomforingprosess.domene.avtale.GodkjentPaVegneGrunn;
import no.nav.tag.tiltaksgjennomforingprosess.domene.avtale.Maal;
import no.nav.tag.tiltaksgjennomforingprosess.domene.avtale.Oppgave;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public class TestData {

    private static ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private static final LocalDate GODKJENT_DATO = LocalDate.of(2019, 8, 16);

    public static final LocalDate START_DATO = GODKJENT_DATO.plusMonths(1);

    public final static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(Avtale.DATOFORMAT_NORGE);

    public static String avtaleTilJSON(Avtale avtale) throws JsonProcessingException {
       return objectMapper.writeValueAsString(avtale);
    }

    public static Avtale opprettAvtale() {
        Avtale avtale = new Avtale();
        avtale.setId(UUID.randomUUID());
        avtale.setVersjon(1);
        avtale.setOpprettet(LocalDate.now().minusDays(2).format(dateTimeFormatter));
        avtale.setDeltakerFornavn("Lillehans");
        avtale.setDeltakerEtternavn("Hansen");
        avtale.setDeltakerFnr("02018099999");
        avtale.setBedriftNavn("Hansen AS");
        avtale.setBedriftNr("999988881");
        avtale.setArbeidsgiverFornavn("Hans");
        avtale.setArbeidsgiverEtternavn("Frøland Hansen");
        avtale.setArbeidsgiverTlf("99990000");
        avtale.setVeilederNavIdent("T123456");
        avtale.setVeilederFornavn("Nils");
        avtale.setVeilederEtternavn("Nilsen");
        avtale.setVeilederTlf("22223333");
        avtale.setOppfolging("Oppfølging under veis om tildelte oppgaver. Og arbeidstager har en oppfølgingsamtale med kjøpmann eller butikksjef ca hver 4 uke, da om miljø på arbeidsplass, arbeidsoppgaver samt egen helse i tildelte oppgaver.");
        avtale.setTilrettelegging("Det er avtalt at Lillehans får tid til og gjennomføre og prioritere opplæring knyttet til sertifikatet. Videre vil NAV følge jevnlig opp.");
        avtale.setStartDato(START_DATO);
        avtale.setArbeidstreningLengde(2);
        avtale.setArbeidstreningStillingprosent(50);
        avtale.setGodkjentAvArbeidsgiver(LocalDateTime.now().format(dateTimeFormatter));
        avtale.setGodkjentAvDeltaker(LocalDateTime.now().plusDays(1).format(dateTimeFormatter));
        avtale.setGodkjentAvVeileder(LocalDateTime.now().plusDays(2).format(dateTimeFormatter));
        avtale.setMaal(List.of(TestData.etMaal(), TestData.etMaal()));
        avtale.setOppgaver(List.of(TestData.enOppgave(), TestData.enOppgave()));
        avtale.setGodkjentPaVegneAv(true);
        avtale.setGodkjentPaVegneGrunn(enGrunn());
        return avtale;
    }

    public static Oppgave enOppgave() {
        Oppgave oppgave = new Oppgave();
        oppgave.setTittel("OppgaveTittel");
        oppgave.setBeskrivelse("  nr 1 Vi skal lære hun bestille varer fra leverandører og besøker de slik hun få erfaring på det.\n" +
                "\n" +
                "Nr 2 vi øve hun  på typiske arbeidsoppgaver er å betjene kunder og kasseapparatet, å rydde i butikken og i hyller, å rydde på lageret, å sette priser på varer, å ta imot bestillinger og ta imot varer.");
        oppgave.setOpplaering("Tanken bak rullering av varer og plassering av varer. \n" +
                "Rutiner rundt fersk og kjølevarer\n" +
                "Opplæring i kasse og kundebehandling\n" +
                "Generell butikk rutiner og daglige oppgaver");
        return oppgave;
    }

    public static Maal etMaal() {
        Maal maal = new Maal();
        maal.setKategori("Her kommer kategorien");
        maal.setBeskrivelse("Målet med arbeidstreningen er og få opplæring i arbeidsoppgaver knyttet til sjåførjobb, lære bedriften og kjenne og får den opplæringen du trenger for og kunne tre inn i stillings funksjonen når du har fått førerkortet. Det foreligger bekreftelse på lovnad om jobb dersom kriteriene er oppfylt i forhold til krav om førerkort.");
        return maal;
    }

    public static GodkjentPaVegneGrunn enGrunn() {
        GodkjentPaVegneGrunn godkjentPaVegneGrunn = new GodkjentPaVegneGrunn();
        godkjentPaVegneGrunn.setIkkeBankId(true);
        godkjentPaVegneGrunn.setDigitalKompetanse(true);
        return godkjentPaVegneGrunn;
    }

}
