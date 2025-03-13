package no.nav.tag.tiltaksgjennomforingprosess.persondata;

import no.nav.tag.tiltaksgjennomforingprosess.persondata.domene.Navn;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;
import java.util.Set;

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

    @Test
    public void hentGradering__returnerer_strengt_fortrolig_person() {
        Diskresjonskode diskresjonskode = persondataService.hentDiskresjonskode(STRENGT_FORTROLIG_PERSON);
        assertThat(diskresjonskode).isEqualTo(Diskresjonskode.STRENGT_FORTROLIG);
    }

    @Test
    public void hentGradering__returnerer_strengt_fortrolig_utland_person() {
        Diskresjonskode diskresjonskode = persondataService.hentDiskresjonskode(STRENGT_FORTROLIG_UTLAND_PERSON);
        assertThat(diskresjonskode).isEqualTo(Diskresjonskode.STRENGT_FORTROLIG_UTLAND);
    }

    @Test
    public void hentGradering__returnerer_fortrolig_person() {
        Diskresjonskode diskresjonskode = persondataService.hentDiskresjonskode(FORTROLIG_PERSON);
        assertThat(diskresjonskode).isEqualTo(Diskresjonskode.FORTROLIG);
    }

    @Test
    public void hentGradering__returnerer_ugradert_person() {
        Diskresjonskode diskresjonskode = persondataService.hentDiskresjonskode(UGRADERT_PERSON);
        assertThat(diskresjonskode).isEqualTo(Diskresjonskode.UGRADERT);
    }

    @Test
    public void hentGradering__returnerer_tom_gradering() {
        Diskresjonskode diskresjonskode = persondataService.hentDiskresjonskode(USPESIFISERT_GRADERT_PERSON);
        assertThat(diskresjonskode).isEqualTo(Diskresjonskode.UGRADERT);
    }

    @Test
    public void hentGradering__person_finnes_ikke_er_ok() {
        Diskresjonskode diskresjonskode = persondataService.hentDiskresjonskode(PERSON_FINNES_IKKE);
        assertThat(diskresjonskode).isEqualTo(Diskresjonskode.UGRADERT);
    }

    @Test
    public void hentGradering__returnerer_ugradert_tom_gradering() {
        Diskresjonskode diskresjonskode = persondataService.hentDiskresjonskode(UGRADERT_PERSON_TOM_RESPONSE);
        assertThat(diskresjonskode).isEqualTo(Diskresjonskode.UGRADERT);
    }

    @Test
    public void hentGradering__person_får_respons_uten_data() {
        Diskresjonskode diskresjonskode = persondataService.hentDiskresjonskode(PERSON_FOR_RESPONS_UTEN_DATA);
        assertThat(diskresjonskode).isEqualTo(Diskresjonskode.UGRADERT);
    }

    @Test
    public void erKode6__strengt_fortrolig() {
        assertThat(persondataService.hentDiskresjonskode(STRENGT_FORTROLIG_PERSON).erKode6()).isTrue();
    }

    @Test
    public void erKode6__strengt_fortrolig_utland() {
        assertThat(persondataService.hentDiskresjonskode(STRENGT_FORTROLIG_UTLAND_PERSON).erKode6()).isTrue();
    }

    @Test
    public void erKode6__fortrolig() {
        assertThat(persondataService.hentDiskresjonskode(FORTROLIG_PERSON).erKode6()).isFalse();
    }

    @Test
    public void erKode6__ugradert() {
        assertThat(persondataService.hentDiskresjonskode(UGRADERT_PERSON).erKode6()).isFalse();
    }

    @Test
    public void erKode6__ugradertTom() {
        assertThat(persondataService.hentDiskresjonskode(UGRADERT_PERSON_TOM_RESPONSE).erKode6()).isFalse();
    }

    @Test
    public void erKode6__uspesifisert_gradering() {
        assertThat(persondataService.hentDiskresjonskode(USPESIFISERT_GRADERT_PERSON).erKode6()).isFalse();
    }

    @Test
    public void erKode6_person_finnes_ikke_er_ok() {
        assertThat(persondataService.hentDiskresjonskode(PERSON_FINNES_IKKE).erKode6()).isFalse();
    }
}
