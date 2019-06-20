package no.nav.tag.tiltaksgjennomforingprosess;

import no.nav.tag.tiltaksgjennomforingprosess.domene.Avtale;
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
        avtale.setOppfolging("Oppfolging");
        avtale.setTilrettelegging("Tilrettelegging");
        avtale.setStartDato(LocalDate.now());
        avtale.setArbeidstreningLengde(2);
        avtale.setArbeidstreningStillingprosent(50);
        avtale.setGodkjentAvArbeidsgiver(LocalDateTime.now());
        avtale.setGodkjentAvDeltaker(LocalDateTime.now());
        avtale.setGodkjentAvVeileder(LocalDateTime.now());
        avtale.setMaal(List.of(TestData.etMaal(), TestData.etMaal()));
        avtale.setOppgaver(List.of(TestData.enOppgave(), TestData.enOppgave()));
        avtale.setGodkjentPaVegneAv(true);
        return avtale;
    }

    public static Oppgave enOppgave() {
        Oppgave oppgave = new Oppgave();
        oppgave.setId(UUID.randomUUID());
        oppgave.setTittel("OppgaveTittel");
        oppgave.setBeskrivelse("OppgaveBeskrivelse");
        oppgave.setOpplaering("Opplæring");
        oppgave.setOpprettetTidspunkt(LocalDateTime.now().minusDays(2));
        return oppgave;
    }

    public static Maal etMaal() {
        Maal maal = new Maal();
        maal.setId(UUID.randomUUID());
        maal.setOpprettetTidspunkt(LocalDateTime.now().minusDays(2));
        maal.setKategori("Kategori");
        maal.setBeskrivelse("MaalBeskrivelse");
        return maal;
    }

}
