package no.nav.tag.tiltaksgjennomforingprosess.integrasjon;

import no.nav.tag.tiltaksgjennomforingprosess.TestData;
import no.nav.tag.tiltaksgjennomforingprosess.domene.avtale.Avtale;
import no.nav.tag.tiltaksgjennomforingprosess.domene.avtale.Tiltakstype;
import no.nav.tag.tiltaksgjennomforingprosess.persondata.Diskresjonskode;
import no.nav.tag.tiltaksgjennomforingprosess.persondata.PersondataService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("local")
@DirtiesContext
public class TiltaksgjennomforingApiIntTest {

    @Autowired
    private TiltaksgjennomfoeringApiService service;

    @MockBean
    private PersondataService persondataService;

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
        Avtale testAvtale = TestData.opprettArbeidstreningAvtale();
        List<Avtale> avtaleList;
        Set<String> deltakerFnr = Set.of(testAvtale.getDeltakerFnr());
        when(persondataService.hentDiskresjonskoder(eq(deltakerFnr))).thenReturn(Map.of(
                testAvtale.getDeltakerFnr(), Diskresjonskode.UGRADERT)
        );

        avtaleList = service.finnAvtalerTilJournalfoering();
        Avtale avtale = avtaleList.stream()
                .filter(avt -> avt.getTiltakstype().equals(Tiltakstype.ARBEIDSTRENING))
                .findFirst().orElseThrow(() -> new AssertionError("Arbeidstrening-avtale mangler"));
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

        Avtale tilskuddAvtale = avtaleList.stream().filter(avt -> avt.getTiltakstype().equals(Tiltakstype.MIDLERTIDIG_LONNSTILSKUDD)).findFirst()
                .orElseThrow(() -> new AssertionError("Midlertidling lønnstilskudd-avtale mangler"));
        if (tilskuddAvtale.getTiltakstype().equals(Tiltakstype.MIDLERTIDIG_LONNSTILSKUDD)) {
            assertEquals(3, tilskuddAvtale.getTilskuddsPerioder().size());
        }

        assertTrue(avtaleList.stream().anyMatch(avt -> avt.getTiltakstype().equals(Tiltakstype.MIDLERTIDIG_LONNSTILSKUDD)));
        assertTrue(avtaleList.stream().anyMatch(avt -> avt.getTiltakstype().equals(Tiltakstype.MENTOR)));
    }

    @Test
    public void henterVTAOAvtaleTilJournalfoering() {
        List<Avtale> avtaleList;
        avtaleList = service.finnAvtalerTilJournalfoering();
        Avtale avtale = avtaleList.stream()
                .filter(avt -> avt.getTiltakstype().equals(Tiltakstype.VTAO))
                .findFirst().orElseThrow(() -> new AssertionError("VTAO-avtale mangler"));
        assertEquals("79001b47-6b3a-43bd-b548-d114ed8965f9", avtale.getAvtaleId().toString());
        assertEquals("9f17ac5f-6a3e-47b6-828e-590de574250f", avtale.getAvtaleVersjonId().toString());
        assertEquals("24096122116", avtale.getDeltakerFnr());
        assertEquals("910825518", avtale.getBedriftNr());
        assertEquals("Z992785", avtale.getVeilederNavIdent());
        assertEquals(LocalDate.of(2020, 2, 3), avtale.getOpprettet());
        assertEquals(2020, avtale.getOpprettetAar());
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
        assertEquals(6808, avtale.getSumLonnstilskudd());

        Avtale tilskuddAvtale = avtaleList.stream().filter(avt -> avt.getTiltakstype().equals(Tiltakstype.MIDLERTIDIG_LONNSTILSKUDD)).findFirst()
                .orElseThrow(() -> new AssertionError("Midlertidling lønnstilskudd-avtale mangler"));
        if (tilskuddAvtale.getTiltakstype().equals(Tiltakstype.MIDLERTIDIG_LONNSTILSKUDD)) {
            assertEquals(3, tilskuddAvtale.getTilskuddsPerioder().size());
        }

        assertTrue(avtaleList.stream().anyMatch(avt -> avt.getTiltakstype().equals(Tiltakstype.MIDLERTIDIG_LONNSTILSKUDD)));
        assertTrue(avtaleList.stream().anyMatch(avt -> avt.getTiltakstype().equals(Tiltakstype.MENTOR)));
    }
}
