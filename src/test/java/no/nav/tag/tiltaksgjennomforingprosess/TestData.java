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
        avtale.setOppfolging("Dette er veldig lang. .oppfølging tekst for test,");
        avtale.setTilrettelegging("Dette er veldig lang tilrettelegging tekst for test, For 3 år siden kunne jeg ikke sett for meg at jeg kom til å sitte 2 timer å gråte på grunn av en 5er i norsk. 5 i norsk var helt uoppnåelig for meg på den tiden. Men etter hvert som jeg begynte å skrive og lese, fant jeg fort ut at dette var meg. ");
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
        oppgave.setBeskrivelse("Dette er veldig lang Oppgave beskrivelse for test,  ");
        oppgave.setOpplaering("Dette er veldig lang opplæring beskrivelse for test, For 3 år siden kunne jeg ikke sett for meg at jeg kom til å s ");
        oppgave.setOpprettetTidspunkt(LocalDateTime.now().minusDays(2));
        return oppgave;
    }

    public static Maal etMaal() {
        Maal maal = new Maal();
        maal.setId(UUID.randomUUID());
        maal.setOpprettetTidspunkt(LocalDateTime.now().minusDays(2));
        maal.setKategori("Kategori");
        maal.setBeskrivelse("Dette er veldig lang beskrivelse for test, For 3 år siden kunne jeg ikke sett for meg at jeg kom til å ");
        return maal;
    }

    public static GodkjentPaVegneGrunn enGrunn() {
        GodkjentPaVegneGrunn godkjentPaVegneGrunn = new GodkjentPaVegneGrunn();
        godkjentPaVegneGrunn.setIkkeBankId(true);
        godkjentPaVegneGrunn.setDigitalKompetanse(true);
        return godkjentPaVegneGrunn;
    }

}
