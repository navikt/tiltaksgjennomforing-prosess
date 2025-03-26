package no.nav.tag.tiltaksgjennomforingprosess.persondata;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("local")
@DirtiesContext
public class PersondataServiceTest {
    private static final String STRENGT_FORTROLIG_PERSON = "16053900422";
    private static final String STRENGT_FORTROLIG_UTLAND_PERSON = "28033114267";
    private static final String FORTROLIG_PERSON = "26067114433";
    private static final String UGRADERT_PERSON = "00000000000";
    private static final String UGRADERT_PERSON_TOM_RESPONSE = "27030960020";
    private static final String USPESIFISERT_GRADERT_PERSON = "18076641842";
    private static final String PERSON_FINNES_IKKE = "24080687881";
    private static final String PERSON_FOR_RESPONS_UTEN_DATA = "23097010706";
    private static final String DONALD_DUCK = "00000000000";
    @Autowired
    private PersondataService persondataService;


    private static final Map<String, Diskresjonskode> forventetSvar = Map.of(
            STRENGT_FORTROLIG_PERSON, Diskresjonskode.STRENGT_FORTROLIG,
            STRENGT_FORTROLIG_UTLAND_PERSON, Diskresjonskode.STRENGT_FORTROLIG_UTLAND,
            FORTROLIG_PERSON, Diskresjonskode.FORTROLIG);


    @Test
    public void hent_graderinger() {
        Map<String, Diskresjonskode> diskresjonskode = persondataService.hentDiskresjonskoder(forventetSvar.keySet());
        assertThat(diskresjonskode).isEqualTo(forventetSvar);
    }
}
