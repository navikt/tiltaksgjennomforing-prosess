package no.nav.tag.tiltaksgjennomforingprosess;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import no.nav.tag.tiltaksgjennomforingprosess.domene.Avtale;
import no.nav.tag.tiltaksgjennomforingprosess.domene.GodkjentPaVegneGrunn;
import no.nav.tag.tiltaksgjennomforingprosess.domene.Maal;
import no.nav.tag.tiltaksgjennomforingprosess.domene.Oppgave;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class TestData {

    private static ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private static final LocalDateTime GODKJENT_DATO = LocalDateTime.of(2019, 8, 16, 12,0);

    public static String avtaleTilJSON(Avtale avtale) throws JsonProcessingException {
       return objectMapper.writeValueAsString(avtale);
    }

    public static Avtale opprettAvtale() {
        Avtale avtale = new Avtale();
        avtale.setId(UUID.randomUUID());
        avtale.setVersjon(1);
        avtale.setOpprettetTidspunkt(LocalDateTime.now().minusDays(2));
        avtale.setDeltakerFornavn("Lillehans");
        avtale.setDeltakerEtternavn("Hansen");
        avtale.setDeltakerFnr("88888899999");
        avtale.setBedriftNavn("Hansen AS");
        avtale.setBedriftNr("12345678");
        avtale.setArbeidsgiverFornavn("Hans");
        avtale.setArbeidsgiverEtternavn("Frøland Hansen");
        avtale.setArbeidsgiverTlf("99990000");
        avtale.setVeilederNavIdent("T123456");
        avtale.setVeilederFornavn("Nils");
        avtale.setVeilederEtternavn("Nilsen");
        avtale.setVeilederTlf("22223333");
        avtale.setOppfolging("Dette er veldig lang. .oppfølging tekst for test,");
        avtale.setTilrettelegging("Dette er lang tilrettelegging tekst for test");
        avtale.setStartDato(GODKJENT_DATO.plusMonths(1).toLocalDate());
        avtale.setArbeidstreningLengde(2);
        avtale.setArbeidstreningStillingprosent(50);
        avtale.setGodkjentAvArbeidsgiver(LocalDateTime.now());
        avtale.setGodkjentAvDeltaker(LocalDateTime.now());
        avtale.setGodkjentAvVeileder(LocalDateTime.now());
        avtale.setMaal(List.of(TestData.etMaal(), TestData.etMaal()));
        avtale.setOppgaver(List.of(TestData.enOppgave(), TestData.enOppgave()));
        avtale.setGodkjentPaVegneAv(true);
        avtale.setGodkjentPaVegneGrunn(enGrunn());
        avtale.setGodkjentAvArbeidsgiver(GODKJENT_DATO);
        avtale.setGodkjentAvDeltaker(GODKJENT_DATO.plusDays(1));
        avtale.setGodkjentAvVeileder(GODKJENT_DATO.plusDays(2));
        return avtale;
    }

    public static Oppgave enOppgave() {
        Oppgave oppgave = new Oppgave();
        oppgave.setTittel("OppgaveTittel");
        oppgave.setBeskrivelse("Dette er veldig lang Oppgave beskrivelse for test,");
        oppgave.setOpplaering("Dette er veldig lang opplæring");
        oppgave.setOpprettetTidspunkt(LocalDateTime.now().minusDays(2));
        return oppgave;
    }

    public static Maal etMaal() {
        Maal maal = new Maal();
        maal.setOpprettetTidspunkt(LocalDateTime.now().minusDays(2));
        maal.setKategori("Kategori");
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
