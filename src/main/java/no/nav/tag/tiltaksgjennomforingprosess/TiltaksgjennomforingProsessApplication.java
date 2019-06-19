package no.nav.tag.tiltaksgjennomforingprosess;

import no.nav.tag.tiltaksgjennomforingprosess.domene.Avtale;
import no.nav.tag.tiltaksgjennomforingprosess.domene.Maal;
import no.nav.tag.tiltaksgjennomforingprosess.domene.Oppgave;
import no.nav.tag.tiltaksgjennomforingprosess.journalpost.factory.AvtaleTilPdf;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties
public class TiltaksgjennomforingProsessApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplicationBuilder(TiltaksgjennomforingProsessApplication.class)
				.initializers(new SjekkAktiveProfilerInitializer())
				.build();
		application.run(args);
		AvtaleTilPdf avtaleTilPdf= new AvtaleTilPdf();
		avtaleTilPdf.generererPdf(getSampleAvtale());
	}

	protected static Avtale getSampleAvtale(){
Avtale testToPdfAvtale= TiltaksgjennomforingProsessApplication.opprettAvtale();
		//testToPdfAvtale.setVersjon();
		return testToPdfAvtale;
	}
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
		avtale.setGodkjentPaVegneAv(false);
		avtale.setMaal(List.of(TiltaksgjennomforingProsessApplication.etMaal(), TiltaksgjennomforingProsessApplication.etMaal()));
		avtale.setOppgaver(List.of(TiltaksgjennomforingProsessApplication.enOppgave(), TiltaksgjennomforingProsessApplication.enOppgave()));
		return avtale;
	}

	public static Oppgave enOppgave() {
		Oppgave oppgave = new Oppgave();
		oppgave.setId(UUID.randomUUID());
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
