package no.nav.tag.tiltaksgjennomforingprosess.journalpost.factory;

import no.nav.tag.tiltaksgjennomforingprosess.TestData;
import no.nav.tag.tiltaksgjennomforingprosess.domene.Avtale;
import org.junit.Test;

import java.util.UUID;

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
    }

}
