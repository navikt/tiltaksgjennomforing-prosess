package no.nav.tag.tiltaksgjennomforingprosess.integrasjon;

import no.nav.tag.tiltaksgjennomforingprosess.domene.avtale.Avtale;
import no.nav.tag.tiltaksgjennomforingprosess.domene.avtale.Tiltakstype;
//import org.junit.Test;
//import org.junit.runner.RunWith;
import org.junit.jupiter.api.Test;
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

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

//import static org.junit.Assert.*;

//@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("local")
@DirtiesContext
public class TiltaksgjennomforingApiIntTest {

    @Autowired
    private TiltaksgjennomfoeringApiService service;

    @Test
    public void setterAvtalerTilJournalfoert() {
        Map<UUID, String> avtalerTilJournalfoert = new HashMap<>();
        avtalerTilJournalfoert.put(UUID.fromString("9f17ac5f-6a3e-47b6-828e-590de574250e"), "001");
        avtalerTilJournalfoert.put(UUID.fromString("463accd8-27cd-4ac7-9735-7cbda7d0a6d2"), "002");
        avtalerTilJournalfoert.put(UUID.fromString("fb8461e5-fb0c-49cf-b349-530c0a086c93"), "003");

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
        Avtale avtale = avtaleList.stream().filter(avt -> avt.getTiltakstype().equals(Tiltakstype.ARBEIDSTRENING)).findFirst().orElseThrow(() -> new AssertionError("Arbeidstrening-avtale mangler"));
        assertEquals("79001b47-6b3a-43bd-b548-d114ed8965f6", avtale.getAvtaleId().toString());
        assertEquals("9f17ac5f-6a3e-47b6-828e-590de574250e", avtale.getAvtaleVersjonId().toString());
        assertEquals("24096122116", avtale.getDeltakerFnr());
        assertEquals("910825518", avtale.getBedriftNr());
        assertEquals("Z992785", avtale.getVeilederNavIdent());
        assertEquals(LocalDate.of(2020, 2, 3), avtale.getOpprettet());
        assertEquals("Jan", avtale.getDeltakerFornavn());
        assertEquals("Banan", avtale.getDeltakerEtternavn());
        assertEquals("67676767", avtale.getDeltakerTlf());
        assertEquals("Maura og Kolbu regnskap", avtale.getBedriftNavn());
        assertEquals("Knut", avtale.getArbeidsgiverFornavn());
        assertEquals("Knutsen", avtale.getArbeidsgiverEtternavn());
        assertEquals("56565656", avtale.getArbeidsgiverTlf());
        assertEquals("Berit", avtale.getVeilederFornavn());
        assertEquals("78787877", avtale.getVeilederTlf());
        assertEquals("Følge opp med Arb.trening", avtale.getOppfolging());
        assertEquals("Legget til rette", avtale.getTilrettelegging());
        assertEquals(70, avtale.getStillingprosent().intValue());
        assertEquals(3, avtale.getAntallDagerPerUke().intValue());
        assertEquals(LocalDate.of(2020, 2, 3), avtale.getGodkjentAvDeltaker());
        assertEquals(LocalDate.of(2020, 2, 3), avtale.getGodkjentAvArbeidsgiver());
        assertEquals(LocalDate.of(2020, 2, 3), avtale.getGodkjentAvVeileder());
        assertTrue(avtale.isGodkjentPaVegneAv());

        Avtale tilskuddAvtale = avtaleList.stream().filter(avt -> avt.getTiltakstype().equals(Tiltakstype.MIDLERTIDIG_LONNSTILSKUDD)).findFirst().orElseThrow(() -> new AssertionError("Midlertidling lønnstilskudd-avtale mangler"));
        if(tilskuddAvtale.getTiltakstype().equals(Tiltakstype.MIDLERTIDIG_LONNSTILSKUDD)) {
            assertEquals(3, tilskuddAvtale.getTilskuddsPerioder().size());
        }

        assertTrue(avtaleList.stream().anyMatch(avt -> avt.getTiltakstype().equals(Tiltakstype.MIDLERTIDIG_LONNSTILSKUDD)));
        assertTrue(avtaleList.stream().anyMatch(avt -> avt.getTiltakstype().equals(Tiltakstype.MENTOR)));
    }
}
