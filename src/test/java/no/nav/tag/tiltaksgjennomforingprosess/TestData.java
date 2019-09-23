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
        avtale.setOppfolging("Linje-1\nlinje-2 \n linje-3\n \nHer kommer resten i ny linje");
        avtale.setTilrettelegging("Dette er lang tilrettelegging tekst for test");
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
        oppgave.setBeskrivelse("Kandidaten blir fulgt opp av linje ledere / mentor-er Er plassert på Linje A3   Lindal treindustri Akland");
        oppgave.setOpplaering("Dette er veldig lang opplæring");
        return oppgave;
    }

    public static Maal etMaal() {
        Maal maal = new Maal();
        maal.setKategori("Her kommer kategorien");
        maal.setBeskrivelse("Dette er veldig lang Mål beskrivelse for test,");
        return maal;
    }

    public static GodkjentPaVegneGrunn enGrunn() {
        GodkjentPaVegneGrunn godkjentPaVegneGrunn = new GodkjentPaVegneGrunn();
        godkjentPaVegneGrunn.setIkkeBankId(true);
        godkjentPaVegneGrunn.setDigitalKompetanse(true);
        return godkjentPaVegneGrunn;
    }

}
