package no.nav.tag.tiltaksgjennomforingprosess.journalpost.factory;

import no.nav.tag.tiltaksgjennomforingprosess.domene.Avtale;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.validation.SchemaFactory;
import java.io.StringWriter;
import java.net.URL;

@Component
public class AvtaleTilXml {


    public String genererXml(Avtale avtale) {

        StringWriter stringWriter = new StringWriter();
        try {
            JAXBContext context = JAXBContext.newInstance(Avtale.class);
            Marshaller marshaller = null;
            marshaller = context.createMarshaller();

            final SchemaFactory schemaFact = SchemaFactory.newInstance(
                    javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
            URL url = this.getClass().getResource("/xsd/inkluderingstilskudd.xsd"); //TODO Eget skjema
            marshaller.setSchema(schemaFact.newSchema(url));
            marshaller.marshal(avtale, stringWriter);
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return stringWriter.getBuffer().toString();

    }

    public String avtaleTilXml2(Avtale avtale) {
        StringWriter stringWriter = new StringWriter();
        JAXBContext context = null;
        Marshaller marshaller = null;
        try {
            context = JAXBContext.newInstance(Avtale.class);
            marshaller = context.createMarshaller();
            marshaller.marshal(avtale, stringWriter);
        } catch (Exception e) {
            throw new RuntimeException("Feil ved oppretting av dokument xml: ", e); //TODO Feilhåndter
        }
        return stringWriter.getBuffer().toString();
    }
}
