package no.nav.tag.tiltaksgjennomforingprosess.factory;

import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tiltaksgjennomforingprosess.TestData;
import no.nav.tag.tiltaksgjennomforingprosess.domene.avtale.Avtale;
import no.nav.tag.tiltaksgjennomforingprosess.domene.avtale.Tiltakstype;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static no.nav.tag.tiltaksgjennomforingprosess.TestData.START_DATO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class AvtaleTilXmlTest {

    private final static String ID_AVTALE = "72c365e7-177a-43ad-9d91-48c6479a6cf0";

    private AvtaleTilXml avtaleTilXml = new AvtaleTilXml();
    @Test
    public void lagerXmlAvArbeidstrening_skal_sladde_deltaker_info() {
        Avtale avtale = TestData.opprettArbeidstreningAvtale();
        avtale.setSkalSladdes(true);

        avtale.setAvtaleId(UUID.fromString(ID_AVTALE));
        String xml = avtaleTilXml.genererXml(avtale);
        assertGenerellInnhold(xml, avtale);
        log.debug(xml);
        assertTrue(xml.contains("<deltakerFornavn>***********</deltakerFornavn>"));
        assertTrue(xml.contains("<deltakerEtternavn>***********</deltakerEtternavn>"));
        assertTrue(xml.contains("<deltakerFnr>***********</deltakerFnr>"));
    }

    @Test
    public void lagerXmlAvArbeidstrening() {
        Avtale avtale = TestData.opprettArbeidstreningAvtale();

        avtale.setAvtaleId(UUID.fromString(ID_AVTALE));
        String xml = avtaleTilXml.genererXml(avtale);
        assertGenerellInnhold(xml, avtale);
        log.debug(xml);
        assertTrue(xml.contains("<tiltaksType>Arbeidstrening</tiltaksType>"));
        assertTrue(xml.contains("<typeBehandling>ab0422</typeBehandling>"));
        assertTrue(xml.contains("<tiltakstype>ARBEIDSTRENING</tiltakstype>"));
    }

    @Test
    public void lagerXmlAvMidlertidigLonnstilskudd() {
        Avtale avtale = TestData.opprettLonnstilskuddsAvtale();

        avtale.setAvtaleId(UUID.fromString(ID_AVTALE));
        String xml = avtaleTilXml.genererXml(avtale);
        log.debug(xml);
        assertGenerellInnhold(xml, avtale);
        assertTrue(xml.contains("<tiltaksType>LonnstilskuddMidlertidig</tiltaksType>"));
        assertTrue(xml.contains("<typeBehandling>ab0336</typeBehandling>"));
        assertTrue(xml.contains("<tiltakstype>MIDLERTIDIG_LONNSTILSKUDD</tiltakstype>"));
    }

    @Test
    public void lagerXmlAvVarigLonnstilskudd() {
        Avtale avtale = TestData.opprettLonnstilskuddsAvtale();
        avtale.setTiltakstype(Tiltakstype.VARIG_LONNSTILSKUDD);

        avtale.setAvtaleId(UUID.fromString(ID_AVTALE));
        String xml = avtaleTilXml.genererXml(avtale);
        log.debug(xml);
        assertGenerellInnhold(xml, avtale);
        assertTrue(xml.contains("<tiltaksType>LonnstilskuddVarig</tiltaksType>"));
        assertTrue(xml.contains("<typeBehandling>ab0337</typeBehandling>"));
        assertTrue(xml.contains("<tiltakstype>VARIG_LONNSTILSKUDD</tiltakstype>"));
    }

    @Test
    public void lagerXmlAvMentor() {
        Avtale avtale = TestData.opprettMentorAvtale();

        avtale.setAvtaleId(UUID.fromString(ID_AVTALE));
        String xml = avtaleTilXml.genererXml(avtale);
        log.debug(xml);
        assertGenerellInnhold(xml, avtale);
        assertTrue(xml.contains("<tiltaksType>mentor</tiltaksType>"));
        assertTrue(xml.contains("<typeBehandling>ab0416</typeBehandling>"));
        assertTrue(xml.contains("<tiltakstype>MENTOR</tiltakstype>"));
    }

    private void assertGenerellInnhold(String xml, Avtale avtale) {

        assertTrue(xml.contains(ID_AVTALE));
        assertTrue(xml.contains(avtale.getDeltakerFnr()));
        assertTrue(xml.contains(avtale.getBedriftNr()));
        assertTrue(xml.contains(avtale.getDeltakerFornavn()));

        String xmlElemStr = substringBetween(xml, "<fraDato>", "</fraDato>");
        LocalDate faktiskDato = LocalDate.parse(xmlElemStr);
        assertEquals(START_DATO, faktiskDato);

        xmlElemStr = substringBetween(xml, "<tilDato>", "</tilDato>");
        faktiskDato = LocalDate.parse(xmlElemStr);
        assertEquals(avtale.getSluttDato(), faktiskDato);
        assertTrue(xml.contains("<versjon>1</versjon>"));
    }

    private static String substringBetween(String streng, String start, String slutt) {
        var del1 = streng.substring(streng.indexOf(start) + start.length());
        return del1.substring(0, del1.indexOf(slutt));
    }
}
