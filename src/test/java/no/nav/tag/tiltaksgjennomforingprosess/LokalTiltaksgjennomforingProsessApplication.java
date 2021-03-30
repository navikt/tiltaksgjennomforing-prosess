package no.nav.tag.tiltaksgjennomforingprosess;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
@Profile("local")
@EnableConfigurationProperties
public class LokalTiltaksgjennomforingProsessApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplicationBuilder(LokalTiltaksgjennomforingProsessApplication.class)
				.profiles("local")
				.build();
		application.run(args);
	}
}
