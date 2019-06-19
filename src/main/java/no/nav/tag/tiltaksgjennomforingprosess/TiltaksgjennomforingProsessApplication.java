package no.nav.tag.tiltaksgjennomforingprosess;

import no.nav.tag.tiltaksgjennomforingprosess.domene.Avtale;
import no.nav.tag.tiltaksgjennomforingprosess.journalpost.factory.AvtaleTilPdf;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

import  no.nav.tag.tiltaksgjennomforingprosess.*;
import java.time.LocalDateTime;
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
		Avtale testToPdfAvtale= new Avtale();

		testToPdfAvtale.setDeltakerFnr("000000000000");
		testToPdfAvtale.setBedriftNr("123456789");
		testToPdfAvtale.setDeltakerEtternavn("okoskoskgskg");
		testToPdfAvtale.setId(UUID.fromString("6ae3be81-abcd-477e-a8f3-4a5eb5fe91e3"));
		testToPdfAvtale.setOpprettetTidspunkt(LocalDateTime.now());
		//testToPdfAvtale.setVersjon();
		return testToPdfAvtale;
	}
}
