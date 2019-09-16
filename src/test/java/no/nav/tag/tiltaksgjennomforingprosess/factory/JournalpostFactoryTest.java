package no.nav.tag.tiltaksgjennomforingprosess.factory;

import no.nav.tag.tiltaksgjennomforingprosess.TestData;
import no.nav.tag.tiltaksgjennomforingprosess.domene.avtale.Avtale;
import no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost.Journalpost;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.HttpServerErrorException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JournalpostFactoryTest {

    @Mock
    private AvtaleTilXml avtaleTilXml;

    @InjectMocks
    private JournalpostFactory journalpostFactory;

    @Test
    public void konvertererTilJournalpost() throws Exception {
        Avtale avtale = TestData.opprettAvtale();

        when(avtaleTilXml.genererXml(avtale)).thenCallRealMethod();
        Journalpost journalpost = journalpostFactory.konverterTilJournalpost(avtale);

        assertEquals("ab0422", journalpost.getBehandlingsTema());
        assertEquals("INNGAAENDE", journalpost.getJournalposttype());
        assertEquals("NAV_NO", journalpost.getKanal());
        assertEquals("TIL", journalpost.getTema());
        assertEquals("Avtale om arbeidstrening", journalpost.getTittel());
        assertEquals(avtale.getDeltakerFnr(), journalpost.getBruker().getId());
        assertEquals("FNR", journalpost.getBruker().getIdType());
        assertEquals(1, journalpost.getDokumenter().size());

        journalpost.getDokumenter().get(0).getDokumentVarianter().forEach(dokumentVariant -> {
            if(dokumentVariant.getFiltype().equals("XML")){
                assertEquals("ORIGINAL", dokumentVariant.getVariantformat());
            } else if(dokumentVariant.getFiltype().equals("PDFA")){
                assertEquals("ARKIV", dokumentVariant.getVariantformat());
            } else{
                fail("DokumentType: " + dokumentVariant.getFiltype());
            }
            assertFalse(dokumentVariant.getFysiskDokument().isEmpty());
        });
    }

    @Test(expected = HttpServerErrorException.class)
    public void feiler() throws Exception {
        Avtale avtale = TestData.opprettAvtale();
        when(avtaleTilXml.genererXml(avtale)).thenThrow(HttpServerErrorException.class);
        journalpostFactory.konverterTilJournalpost(avtale);
    }
}