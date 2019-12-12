package no.nav.tag.tiltaksgjennomforingprosess.integrasjon;

import no.nav.tag.tiltaksgjennomforingprosess.domene.avtale.Avtale;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
@DirtiesContext
public class TiltaksgjennomforingApiIntTest {

    @Autowired
    private TiltaksgjennomfoeringApiService service;

    @Test
    public void setterAvtalerTilJournalfoert() {

        Map<UUID, String> avtalerTilJournalfoert = new HashMap<>();
        avtalerTilJournalfoert.put(UUID.fromString("c34250d3-d68f-4389-b4ff-13a73032212e"), "1");
        avtalerTilJournalfoert.put(UUID.fromString("c18abcb0-e1b4-43af-be51-7bfeced2efe5"), "2");

        try {
            service.settAvtalerTilJournalfoert(avtalerTilJournalfoert);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void henterAvtalerTilJournalfoering() {
        List<Avtale> avtaleList;
        avtaleList = service.finnAvtalerTilJournalfoering();
        Avtale avtale = avtaleList.get(0);
        assertEquals("ca3d7189-0852-4693-a3dd-d518b4ec42e4", avtale.getAvtaleId().toString());
        assertEquals("878c49f4-7225-4bb7-becf-a63b90a1baf7", avtale.getAvtaleVersjonId().toString());
        assertEquals("01093434109", avtale.getDeltakerFnr());
        assertEquals("975959171", avtale.getBedriftNr());
        assertEquals("X123456", avtale.getVeilederNavIdent());
        assertEquals(LocalDate.of(2019, 9, 9), avtale.getOpprettet());
        assertEquals("Ronny", avtale.getDeltakerFornavn());
        assertEquals("Deltaker", avtale.getDeltakerEtternavn());
        assertEquals("00000000", avtale.getDeltakerTlf());
        assertEquals("Ronnys butikk", avtale.getBedriftNavn());
        assertEquals("Ronnys", avtale.getArbeidsgiverFornavn());
        assertEquals("Kremmer", avtale.getArbeidsgiverEtternavn());
        assertEquals("22334455", avtale.getArbeidsgiverTlf());
        assertEquals("Jan-Ronny", avtale.getVeilederFornavn());
        assertEquals("33445566", avtale.getVeilederTlf());
        assertEquals("Telefon hver uke", avtale.getOppfolging());
        assertEquals("Ingen", avtale.getTilrettelegging());
        assertEquals(100, avtale.getStillingprosent().intValue());
        assertEquals(LocalDate.of(2019, 9, 9), avtale.getGodkjentAvDeltaker());
        assertEquals(LocalDate.of(2019, 10, 9), avtale.getGodkjentAvArbeidsgiver());
        assertEquals(LocalDate.of(2019, 11, 9), avtale.getGodkjentAvVeileder());
        assertFalse(avtale.isGodkjentPaVegneAv());
    }

}
