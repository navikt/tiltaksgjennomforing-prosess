package no.nav.tag.tiltaksgjennomforingprosess;

import no.nav.security.token.support.client.spring.oauth2.EnableOAuth2Client;
import no.nav.security.token.support.spring.api.EnableJwtTokenValidation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableJwtTokenValidation(ignore = {
		"org.springdoc",
		"springfox.documentation.swagger.web.ApiResourceController",
		"org.springframework"
})
@SpringBootApplication
@EnableConfigurationProperties
@EnableOAuth2Client(cacheEnabled = true)
public class TiltaksgjennomforingProsessApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplicationBuilder(TiltaksgjennomforingProsessApplication.class)
				.initializers(new SjekkAktiveProfilerInitializer())
				.build();
		application.run(args);
	}
}
