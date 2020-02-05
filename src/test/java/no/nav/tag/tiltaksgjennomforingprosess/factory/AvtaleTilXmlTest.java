package no.nav.tag.tiltaksgjennomforingprosess.factory;

import no.nav.tag.tiltaksgjennomforingprosess.TestData;
import no.nav.tag.tiltaksgjennomforingprosess.domene.avtale.Avtale;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.time.LocalDate;
import java.util.UUID;

import static no.nav.tag.tiltaksgjennomforingprosess.TestData.START_DATO;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AvtaleTilXmlTest {

    private final static String ID_AVTALE = "72c365e7-177a-43ad-9d91-48c6479a6cf0";

    private AvtaleTilXml avtaleTilXml = new AvtaleTilXml();

    @Test
    public void lagerXmlAvArbeidstrening(){
        Avtale avtale = TestData.opprettArbeidstreningAvtale();

        avtale.setAvtaleId(UUID.fromString(ID_AVTALE));
        String xml = avtaleTilXml.genererXml(avtale);
        assertGenerellInnhold(xml, avtale);
        System.out.println(xml);
        assertTrue(xml.contains("<tiltaksType>Arbeidstrening</tiltaksType>"));
        assertTrue(xml.contains("<typeBehandling>ab0422</typeBehandling>"));
        assertTrue(xml.contains("<tiltakstype>ARBEIDSTRENING</tiltakstype>"));
    }

    @Test
    public void lagerXmlAvMentor(){
        Avtale avtale = TestData.opprettMentorAvtale();

        avtale.setAvtaleId(UUID.fromString(ID_AVTALE));
        String xml = avtaleTilXml.genererXml(avtale);
        System.out.println(xml);
        assertGenerellInnhold(xml, avtale);
        assertTrue(xml.contains("<tiltaksType>Mentor</tiltaksType>"));
        assertTrue(xml.contains("<typeBehandling>ab0416</typeBehandling>"));
        assertTrue(xml.contains("<tiltakstype>MENTOR</tiltakstype>"));

        //assertFalse();
    }

    private void assertGenerellInnhold(String xml, Avtale avtale) {

        assertTrue(xml.contains(ID_AVTALE));
        assertTrue(xml.contains(avtale.getDeltakerFnr()));
        assertTrue(xml.contains(avtale.getBedriftNr()));
        assertTrue(xml.contains(avtale.getDeltakerFornavn()));

        String xmlElemStr = StringUtils.substringBetween(xml, "<fraDato>", "</fraDato>");
        LocalDate faktiskDato = LocalDate.parse(xmlElemStr);
        assertEquals(START_DATO, faktiskDato);

        xmlElemStr = StringUtils.substringBetween(xml, "<tilDato>", "</tilDato>");
        faktiskDato = LocalDate.parse(xmlElemStr);
        assertEquals(avtale.getSluttDato(), faktiskDato);
        assertTrue(xml.contains("<versjon>1</versjon>"));
    }

}
