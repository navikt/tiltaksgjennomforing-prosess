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
        avtalerTilJournalfoert.put(UUID.fromString("878c49f4-7225-4bb7-becf-a63b90a1baf7"), "001");
        avtalerTilJournalfoert.put(UUID.fromString("4c8058e9-1d07-4a76-9d9f-201e9a2ca401"), "002");
        avtalerTilJournalfoert.put(UUID.fromString("4558cfad-9cf0-4543-94e5-7970cdaeeb8b"), "003");

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
        assertEquals("02018099999", avtale.getDeltakerFnr());
        assertEquals("999999999", avtale.getBedriftNr());
        assertEquals("X123456", avtale.getVeilederNavIdent());
        assertEquals(LocalDate.of(2019, 9, 9), avtale.getOpprettet());
        assertEquals("Ronny", avtale.getDeltakerFornavn());
        assertEquals("Deltaker", avtale.getDeltakerEtternavn());
        assertEquals("00000000", avtale.getDeltakerTlf());
        assertEquals("Hansen AS", avtale.getBedriftNavn());
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
