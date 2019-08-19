package no.nav.tag.tiltaksgjennomforingprosess.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {
//    private final JdbcTemplate jdbcTemplate;

    public HealthCheckController() {

    }

    @GetMapping(value = "/internal/healthcheck")
    public String healthcheck() {

        return "ok";

        //TODO 'pinge' sts og joark
//        return jdbcTemplate.queryForObject("select 'ok'", String.class);
    }
}
