package no.nav.tag.tiltaksgjennomforingprosess;

import no.nav.tag.tiltaksgjennomforingprosess.domene.Avtale;
import no.nav.tag.tiltaksgjennomforingprosess.domene.GodkjentPaVegneGrunn;
import no.nav.tag.tiltaksgjennomforingprosess.domene.Maal;
import no.nav.tag.tiltaksgjennomforingprosess.domene.Oppgave;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class TestData {

    public static Avtale opprettAvtale() {
        Avtale avtale = new Avtale();
        avtale.setId(UUID.randomUUID());
        avtale.setVersjon(1);
        avtale.setOpprettetTidspunkt(LocalDateTime.now().minusDays(2));
        avtale.setDeltakerFornavn("DeltakerFornavn");
        avtale.setDeltakerEtternavn("DeltakerEtternavn");
        avtale.setDeltakerFnr("88888899999");
        avtale.setBedriftNavn("Bedriftnavn");
        avtale.setBedriftNr("12345678");
        avtale.setArbeidsgiverFornavn("AG fornavn");
        avtale.setArbeidsgiverEtternavn("AG etternavn");
        avtale.setArbeidsgiverTlf("AG tlf");
        avtale.setVeilederNavIdent("navIdent");
        avtale.setVeilederFornavn("Veilederfornavn");
        avtale.setVeilederEtternavn("Veilederetternavn");
        avtale.setVeilederTlf("Veiledertlf");
        avtale.setOppfolging("Dette er veldig lang.\n .oppfølging tekst for test, For 3 år siden kunne jeg ikke sett for meg at jeg kom til å sitte 2 timer å gråte på grunn av en 5er i norsk. 5 i norsk var helt uoppnåelig for meg på den tiden. Men etter hvert som jeg begynte å skrive og lese, fant jeg fort ut at dette var meg. Endelig fant jeg noe JEG var god på. Noe jeg kunne bedre enn de andre. Og denne gaven har jeg tatt godt vare på." +
                "Karakteren min i norsk gikk opp fra 3 til 4 og fra 4 til 5, og i år var året jeg skulle få 6. Alle innleveringene mine har jeg fått høyeste score på, og alt ser utrolig lovende ut. Men en dag tar læreren meg til side. Hun spør meg hvorfor jeg ikke er muntlig i timene. Jeg svarer som sant er, at jeg ikke liker å snakke høyt i klasserommet. Jeg er utrolig ukomfortabel med klassen min, og jeg har mange ganger vært nær å besvime når jeg har blitt tvunget til å snakke. Jeg forklarer henne situasjonen og hun sier at hun skal ta meg ut på gangen å snakke med meg for å kunne gi meg en muntlig karakter." +

                "Noe av det siste hun sa til meg, og noe av det som klistret seg mest fast til meg, var at jeg var en 6er i norsk. Jeg hadde endelig klart noe jeg også. En av mine første 6ere. Hun sier ingenting om at jeg må bli bedre til å snakke høyt i klasserommet, fordi de faglige diskusjonene skulle jeg jo ta på gangen." +

                "Timene gikk og karakterene ble satt. 5. Der fikk jeg den i fleisen. Der hadde hun stått og sagt at jeg var en 6er, siden hun skulle respektere at jeg ikke klarte å snakke høyt i klassen. Men så var grunnen til at jeg ikke var oppnåelig nok for en 6er, at jeg ikke var muntlig i timene.");
        avtale.setTilrettelegging("Dette er veldig lang tilrettelegging tekst for test, For 3 år siden kunne jeg ikke sett for meg at jeg kom til å sitte 2 timer å gråte på grunn av en 5er i norsk. 5 i norsk var helt uoppnåelig for meg på den tiden. Men etter hvert som jeg begynte å skrive og lese, fant jeg fort ut at dette var meg. Endelig fant jeg noe JEG var god på. Noe jeg kunne bedre enn de andre. Og denne gaven har jeg tatt godt vare på." +
                "Karakteren min i norsk gikk opp fra 3 til 4 og fra 4 til 5, og i år var året jeg skulle få 6. Alle innleveringene mine har jeg fått høyeste score på, og alt ser utrolig lovende ut. Men en dag tar læreren meg til side. Hun spør meg hvorfor jeg ikke er muntlig i timene. Jeg svarer som sant er, at jeg ikke liker å snakke høyt i klasserommet. Jeg er utrolig ukomfortabel med klassen min, og jeg har mange ganger vært nær å besvime når jeg har blitt tvunget til å snakke. Jeg forklarer henne situasjonen og hun sier at hun skal ta meg ut på gangen å snakke med meg for å kunne gi meg en muntlig karakter." +

                "Noe av det siste hun sa til meg, og noe av det som klistret seg mest fast til meg, var at jeg var en 6er i norsk. Jeg hadde endelig klart noe jeg også. En av mine første 6ere. Hun sier ingenting om at jeg må bli bedre til å snakke høyt i klasserommet, fordi de faglige diskusjonene skulle jeg jo ta på gangen." +

                "Timene gikk og karakterene ble satt. 5. Der fikk jeg den i fleisen. Der hadde hun stått og sagt at jeg var en 6er, siden hun skulle respektere at jeg ikke klarte å snakke høyt i klassen. Men så var grunnen til at jeg ikke var oppnåelig nok for en 6er, at jeg ikke var muntlig i timene.");
        avtale.setStartDato(LocalDate.now());
        avtale.setArbeidstreningLengde(2);
        avtale.setArbeidstreningStillingprosent(50);
        avtale.setGodkjentAvArbeidsgiver(LocalDateTime.now());
        avtale.setGodkjentAvDeltaker(LocalDateTime.now());
        avtale.setGodkjentAvVeileder(LocalDateTime.now());
        avtale.setMaal(List.of(TestData.etMaal(), TestData.etMaal()));
        avtale.setOppgaver(List.of(TestData.enOppgave(), TestData.enOppgave()));
        avtale.setGodkjentPaVegneAv(true);
        avtale.setGodkjentPaVegneGrunn(enGrunn());
        return avtale;
    }

    public static Oppgave enOppgave() {
        Oppgave oppgave = new Oppgave();
        oppgave.setId(UUID.randomUUID());
        oppgave.setTittel("OppgaveTittel");
        oppgave.setBeskrivelse("Dette er veldig lang Oppgave beskrivelse for test,  For 3 år siden kunne jeg ikke sett for meg at jeg kom til å sitte 2 timer å gråte på grunn av en 5er i norsk. 5 i norsk var helt uoppnåelig for meg på den tiden. Men etter hvert som jeg begynte å skrive og lese, fant jeg fort ut at dette var meg. Endelig fant jeg noe JEG var god på. Noe jeg kunne bedre enn de andre. Og denne gaven har jeg tatt godt vare på." +
                "Karakteren min i norsk gikk opp fra 3 til 4 og fra 4 til 5, og i år var året jeg skulle få 6. Alle innleveringene mine har jeg fått høyeste score på, og alt ser utrolig lovende ut. Men en dag tar læreren meg til side. Hun spør meg hvorfor jeg ikke er muntlig i timene. Jeg svarer som sant er, at jeg ikke liker å snakke høyt i klasserommet. Jeg er utrolig ukomfortabel med klassen min, og jeg har mange ganger vært nær å besvime når jeg har blitt tvunget til å snakke. Jeg forklarer henne situasjonen og hun sier at hun skal ta meg ut på gangen å snakke med meg for å kunne gi meg en muntlig karakter." +

                "Noe av det siste hun sa til meg, og noe av det som klistret seg mest fast til meg, var at jeg var en 6er i norsk. Jeg hadde endelig klart noe jeg også. En av mine første 6ere. Hun sier ingenting om at jeg må bli bedre til å snakke høyt i klasserommet, fordi de faglige diskusjonene skulle jeg jo ta på gangen." +

                "Timene gikk og karakterene ble satt. 5. Der fikk jeg den i fleisen. Der hadde hun stått og sagt at jeg var en 6er, siden hun skulle respektere at jeg ikke klarte å snakke høyt i klassen. Men så var grunnen til at jeg ikke var oppnåelig nok for en 6er, at jeg ikke var muntlig i timene.");
        oppgave.setOpplaering("Dette er veldig lang opplæring beskrivelse for test, For 3 år siden kunne jeg ikke sett for meg at jeg kom til å sitte 2 timer å gråte på grunn av en 5er i norsk. 5 i norsk var helt uoppnåelig for meg på den tiden. Men etter hvert som jeg begynte å skrive og lese, fant jeg fort ut at dette var meg. Endelig fant jeg noe JEG var god på. Noe jeg kunne bedre enn de andre. Og denne gaven har jeg tatt godt vare på." +
                "Karakteren min i norsk gikk opp fra 3 til 4 og fra 4 til 5, og i år var året jeg skulle få 6. Alle innleveringene mine har jeg fått høyeste score på, og alt ser utrolig lovende ut. Men en dag tar læreren meg til side. Hun spør meg hvorfor jeg ikke er muntlig i timene. Jeg svarer som sant er, at jeg ikke liker å snakke høyt i klasserommet. Jeg er utrolig ukomfortabel med klassen min, og jeg har mange ganger vært nær å besvime når jeg har blitt tvunget til å snakke. Jeg forklarer henne situasjonen og hun sier at hun skal ta meg ut på gangen å snakke med meg for å kunne gi meg en muntlig karakter." +

                "Noe av det siste hun sa til meg, og noe av det som klistret seg mest fast til meg, var at jeg var en 6er i norsk. Jeg hadde endelig klart noe jeg også. En av mine første 6ere. Hun sier ingenting om at jeg må bli bedre til å snakke høyt i klasserommet, fordi de faglige diskusjonene skulle jeg jo ta på gangen." +

                "Timene gikk og karakterene ble satt. 5. Der fikk jeg den i fleisen. Der hadde hun stått og sagt at jeg var en 6er, siden hun skulle respektere at jeg ikke klarte å snakke høyt i klassen. Men så var grunnen til at jeg ikke var oppnåelig nok for en 6er, at jeg ikke var muntlig i timene.");
        oppgave.setOpprettetTidspunkt(LocalDateTime.now().minusDays(2));
        return oppgave;
    }

    public static Maal etMaal() {
        Maal maal = new Maal();
        maal.setId(UUID.randomUUID());
        maal.setOpprettetTidspunkt(LocalDateTime.now().minusDays(2));
        maal.setKategori("Kategori");
        maal.setBeskrivelse("Dette er veldig lang beskrivelse for test, For 3 år siden kunne jeg ikke sett for meg at jeg kom til å sitte 2 timer å gråte på grunn av en 5er i norsk. 5 i norsk var helt uoppnåelig for meg på den tiden. Men etter hvert som jeg begynte å skrive og lese, fant jeg fort ut at dette var meg. Endelig fant jeg noe JEG var god på. Noe jeg kunne bedre enn de andre. Og denne gaven har jeg tatt godt vare på." +
                "Karakteren min i norsk gikk opp fra 3 til 4 og fra 4 til 5, og i år var året jeg skulle få 6. Alle innleveringene mine har jeg fått høyeste score på, og alt ser utrolig lovende ut. Men en dag tar læreren meg til side. Hun spør meg hvorfor jeg ikke er muntlig i timene. Jeg svarer som sant er, at jeg ikke liker å snakke høyt i klasserommet. Jeg er utrolig ukomfortabel med klassen min, og jeg har mange ganger vært nær å besvime når jeg har blitt tvunget til å snakke. Jeg forklarer henne situasjonen og hun sier at hun skal ta meg ut på gangen å snakke med meg for å kunne gi meg en muntlig karakter." +

                "Noe av det siste hun sa til meg, og noe av det som klistret seg mest fast til meg, var at jeg var en 6er i norsk. Jeg hadde endelig klart noe jeg også. En av mine første 6ere. Hun sier ingenting om at jeg må bli bedre til å snakke høyt i klasserommet, fordi de faglige diskusjonene skulle jeg jo ta på gangen." +

                "Timene gikk og karakterene ble satt. 5. Der fikk jeg den i fleisen. Der hadde hun stått og sagt at jeg var en 6er, siden hun skulle respektere at jeg ikke klarte å snakke høyt i klassen. Men så var grunnen til at jeg ikke var oppnåelig nok for en 6er, at jeg ikke var muntlig i timene.");
        return maal;
    }
    public static GodkjentPaVegneGrunn enGrunn(){
        GodkjentPaVegneGrunn godkjentPaVegneGrunn =new GodkjentPaVegneGrunn();
        godkjentPaVegneGrunn.setIkkeBankId(true);
        godkjentPaVegneGrunn.setDigitalKompetanse(true);
        return godkjentPaVegneGrunn;
    }

}
