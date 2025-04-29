package no.nav.tag.tiltaksgjennomforingprosess.persondata;

import no.nav.team_tiltak.felles.persondata.pdl.domene.Diskresjonskode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("local")
@DirtiesContext
public class PersondataServiceTest {
    private static final String STRENGT_FORTROLIG_PERSON = "16053900422";
    private static final String STRENGT_FORTROLIG_UTLAND_PERSON = "28033114267";
    private static final String FORTROLIG_PERSON = "26067114433";
    private static final String UGRADERT_PERSON = "08120689976";
    private static final String ANNET = "23097010706";
    @Autowired
    private PersondataService persondataService;


    private static final Map<String, Optional<Diskresjonskode>> forventetSvar = Map.of(
            STRENGT_FORTROLIG_PERSON, Optional.of(Diskresjonskode.STRENGT_FORTROLIG),
            STRENGT_FORTROLIG_UTLAND_PERSON, Optional.of(Diskresjonskode.STRENGT_FORTROLIG_UTLAND),
            FORTROLIG_PERSON, Optional.of(Diskresjonskode.FORTROLIG),
            UGRADERT_PERSON, Optional.of(Diskresjonskode.UGRADERT),
            ANNET, Optional.of(Diskresjonskode.UGRADERT)
    );


    @Test
    public void hent_graderinger() {
        Map<String, Optional<Diskresjonskode>> diskresjonskode = persondataService.hentDiskresjonskoder(forventetSvar.keySet());
        assertThat(diskresjonskode).isEqualTo(forventetSvar);
    }
}
