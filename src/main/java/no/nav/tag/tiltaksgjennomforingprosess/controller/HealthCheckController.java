package no.nav.tag.tiltaksgjennomforingprosess.controller;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {
    private final JdbcTemplate jdbcTemplate;

    public HealthCheckController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping(value = "/internal/healthcheck")
    public String healthcheck() {



        //TODO 'pinge' sts og joark
        return jdbcTemplate.queryForObject("select 'ok'", String.class);
    }
}
