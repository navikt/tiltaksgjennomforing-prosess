package no.nav.tag.tiltaksgjennomforingprosess;

import no.nav.tag.tiltaksgjennomforingprosess.domene.Avtale;
import no.nav.tag.tiltaksgjennomforingprosess.domene.Maal;
import no.nav.tag.tiltaksgjennomforingprosess.domene.Oppgave;

import java.time.LocalDate;
import java.util.List;

public class TestData {

    public static Avtale opprettAvtale() {
        Avtale avtale = new Avtale();
        avtale.setDeltakerFornavn("Fornavn");
        avtale.setDeltakerEtternavn("Etternavn");
        avtale.setDeltakerFnr("88888899999");
        avtale.setBedriftNavn("Bedriftnavn");
        avtale.setBedriftNr("12345678");
        avtale.setArbeidsgiverFornavn("AG fornavn");
        avtale.setArbeidsgiverEtternavn("AG etternavn");
        avtale.setArbeidsgiverTlf("AG tlf");
        avtale.setVeilederFornavn("Veilederfornavn");
        avtale.setVeilederEtternavn("Veilederetternavn");
        avtale.setVeilederTlf("Veiledertlf");
        avtale.setOppfolging("Oppfolging");
        avtale.setTilrettelegging("Tilrettelegging");
        avtale.setStartDato(LocalDate.now());
        avtale.setArbeidstreningLengde(2);
        avtale.setArbeidstreningStillingprosent(50);
        avtale.setMaal(List.of(TestData.etMaal(), TestData.etMaal()));
        avtale.setOppgaver(List.of(TestData.enOppgave(), TestData.enOppgave()));
        return avtale;
    }

    public static Oppgave enOppgave() {
        return new Oppgave();
    }

    public static Maal etMaal() {
        return new Maal();
    }

}
