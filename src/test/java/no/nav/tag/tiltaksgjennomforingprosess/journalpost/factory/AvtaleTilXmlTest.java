package no.nav.tag.tiltaksgjennomforingprosess.journalpost.factory;

import no.nav.tag.tiltaksgjennomforingprosess.TestData;
import no.nav.tag.tiltaksgjennomforingprosess.domene.Avtale;
import org.junit.Test;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.Diff;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import static org.junit.Assert.assertFalse;

public class AvtaleTilXmlTest {

    private final static String ID_AVTALE = "72c365e7-177a-43ad-9d91-48c6479a6cf0";
    private final static String ID_MAAL_1 = "161de513-9df5-4ea6-b0ac-7d5ce5a02805";
    private final static String ID_MAAL_2 = "eb0f0ceb-4a17-4dca-85f3-13b38fdeebe3";
    private final static String ID_OPPG_1 = "a63ce05a-8337-400a-86d4-3b4a6459e263";
    private final static String ID_OPPG_2 = "8eea897f-40f9-472b-beb1-de64ab632075";
    private final static String FASIT_XML = lesFraXmlFil();

    private AvtaleTilXml avtaleTilXml = new AvtaleTilXml();

    @Test
    public void lagerAvtaleXml() {
        Avtale avtale = TestData.opprettAvtale();
        avtale.setId(UUID.fromString(ID_AVTALE));
        avtale.getMaal().get(0).setId(UUID.fromString(ID_MAAL_1));
        avtale.getMaal().get(1).setId(UUID.fromString(ID_MAAL_2));
        avtale.getOppgaver().get(0).setId(UUID.fromString(ID_OPPG_1));
        avtale.getOppgaver().get(1).setId(UUID.fromString(ID_OPPG_2));

        String xml = avtaleTilXml.genererXml(avtale);

        Diff myDiff = DiffBuilder.compare(FASIT_XML).withTest(xml)
                .ignoreWhitespace()
                .build();

        assertFalse(myDiff.hasDifferences());
    }

    private static String lesFraXmlFil() {
        StringBuilder sb = new StringBuilder();
        try {
            Files.lines(Paths.get(AvtaleTilXmlTest.class.getClassLoader().getResource("journalpost.xml").toURI()), StandardCharsets.UTF_8).forEach(str -> sb.append(str));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

}
