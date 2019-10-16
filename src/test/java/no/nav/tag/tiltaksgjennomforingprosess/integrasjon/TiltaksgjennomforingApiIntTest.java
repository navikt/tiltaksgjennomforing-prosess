package no.nav.tag.tiltaksgjennomforingprosess.integrasjon;

import no.nav.tag.tiltaksgjennomforingprosess.domene.avtale.Avtale;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

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

    private final String TOKEN = "eyxXxx";

    @Test
    public void setterAvtalerTilJournalfoert() {

        Map<UUID, String> avtalerTilJournalfoert = new HashMap<>();
        avtalerTilJournalfoert.put(UUID.fromString("c34250d3-d68f-4389-b4ff-13a73032212e"), "1");
        avtalerTilJournalfoert.put(UUID.fromString("c18abcb0-e1b4-43af-be51-7bfeced2efe5"), "2");

        try {
            service.settAvtalerTilJournalfoert(TOKEN, avtalerTilJournalfoert);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void henterAvtalerTilJournalfoering() {

        List<Avtale> avtaleList = null;
        try {
            avtaleList = service.finnAvtalerTilJournalfoering(TOKEN);
        } catch (Exception e) {
            fail(e.getMessage());
        }
        Avtale avtale = avtaleList.get(0);
        assertEquals("ca3d7189-0852-4693-a3dd-d518b4ec42e4", avtale.getId().toString());
        assertEquals("01093434109", avtale.getDeltakerFnr());
        assertEquals("975959171", avtale.getBedriftNr());
        assertEquals("X123456", avtale.getVeilederNavIdent());
        assertEquals("09.09.2019", avtale.getOpprettet());
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
        assertEquals(2, avtale.getArbeidstreningLengde().intValue());
        assertEquals(100, avtale.getArbeidstreningStillingprosent().intValue());
        assertEquals("09.09.2019", avtale.getGodkjentAvDeltaker());
        assertEquals("09.09.2019", avtale.getGodkjentAvDeltaker());
        assertEquals("09.10.2019", avtale.getGodkjentAvArbeidsgiver());
        assertFalse(avtale.isGodkjentPaVegneAv());
    }

}
