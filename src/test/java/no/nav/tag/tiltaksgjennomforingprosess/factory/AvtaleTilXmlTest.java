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
    public void lagerAvtaleXml() {
        Avtale avtale = TestData.opprettAvtale();
        avtale.setId(UUID.fromString(ID_AVTALE));
        String xml = avtaleTilXml.genererXml(avtale);

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
    }

}
