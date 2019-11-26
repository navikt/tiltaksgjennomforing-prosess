package no.nav.tag.tiltaksgjennomforingprosess;

import no.nav.tag.tiltaksgjennomforingprosess.domene.avtale.Avtale;
import no.nav.tag.tiltaksgjennomforingprosess.domene.avtale.GodkjentPaVegneGrunn;
import no.nav.tag.tiltaksgjennomforingprosess.domene.avtale.Maal;
import no.nav.tag.tiltaksgjennomforingprosess.domene.avtale.Oppgave;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class TestData {

    private static final LocalDate GODKJENT_DATO = LocalDate.of(2019, 8, 16);

    public static final LocalDate START_DATO = GODKJENT_DATO.plusMonths(1);

    public static Avtale opprettAvtale() {
        Avtale avtale = new Avtale();
        avtale.setId(UUID.randomUUID());
        avtale.setOpprettet(LocalDate.now().minusDays(2));
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
        avtale.setOppfolging("- Deltaker har Kollega som kontaktperson.\n" +
                "- Deltaker utfører i hovedsak arbeidsoppgaver sammen med Kollega i starten. \n" +
                "- Grad av oppfølging vurderes underveis. ");
//        avtale.setTilrettelegging("- Kortere dager.\n" +
//                "- Utprøvings periode på 12 uker av gangen, med mål på sikt om å øke arbeidsmengden.\n" +
//                "- Følges opp samtaler \n" +
//                "- «Aktiv på dagtid»\n" +
//                "- Fleksibel arbeidsbelastning. ");
        avtale.setTilrettelegging("Deltaker will be able to work to a large extent independently as soon as the on-boarding and training took place.\n" +
                "We will define with specific tasks and further agree on 1) need for support to deliver, 2) the checkpoints and 3) the deadlines of the deliveries");
        avtale.setStartDato(START_DATO);
        avtale.setSluttDato(START_DATO.plusMonths(2));
        avtale.setStillingprosent(50);
        avtale.setGodkjentAvArbeidsgiver(LocalDate.now());
        avtale.setGodkjentAvDeltaker(LocalDate.now().plusDays(1));
        avtale.setGodkjentAvVeileder(LocalDate.now().plusDays(2));
        avtale.setMaal(List.of(TestData.etMaal(), TestData.etMaal()));
        avtale.setOppgaver(List.of(TestData.enOppgave(), TestData.endaEnOppgave()));
        avtale.setGodkjentPaVegneAv(true);
        avtale.setGodkjentPaVegneGrunn(enGrunn());
        return avtale;
    }

    public static Oppgave enOppgave() {
        Oppgave oppgave = new Oppgave();
        oppgave.setTittel("Her er oppgave 1");
        oppgave.setBeskrivelse("  nr 1 Vi skal lære hun bestille varer fra leverandører og besøker de slik hun få erfaring på det.\n" +
                "\n" +
                "Nr 2 vi øve hun  på typiske arbeidsoppgaver er å betjene kunder og kasseapparatet, å rydde i butikken og i hyller, å rydde på lageret, å sette priser på varer, å ta imot bestillinger og ta imot varer.");
        oppgave.setOpplaering("Tanken bak rullering av varer og plassering av varer. \n" +
                "Rutiner rundt fersk og kjølevarer\n" +
                "Opplæring i kasse og kundebehandling\n" +
                "Generell butikk rutiner og daglige oppgaver");
        return oppgave;
    }

    public static Oppgave endaEnOppgave() {
        Oppgave oppgave = new Oppgave();
        oppgave.setTittel("Her er oppgave 2");
        oppgave.setBeskrivelse("The business needs to be able to gather all the data about the performance in one place.\n" +
                "Deltaker needs to coordinate/perform the integration of various sources of information into Google Data Studio (GDS). The current implementation is with our database.\n" +
                "1)\t We need to evaluate how to bring in information from: mailerlite, suverymonkey, Zendesk etc.\n" +
                "2)\t Based on the evaluation, do the actual implementation of the data source\n" +
                "\n" +
                "This requires as a first step to understand how data is organized and sourced in Arb. giver, which will be the base for her to perform other tasks.");
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
