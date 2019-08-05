package no.nav.tag.tiltaksgjennomforingprosess.journalpost.factory;

import no.nav.tag.tiltaksgjennomforingprosess.domene.*;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;

@Component
public class AvtaleTilXml {

    public String genererXml(Avtale avtale) {

        Generelt generelt = new Generelt(new Arbeidsgiver(avtale.getBedriftNr()), new Arbeidstaker(avtale.getDeltakerFnr()), avtale);
        Innhold innhold = new Innhold(new SkjemaInfo(), generelt);
        Melding melding = new Melding(innhold);

        StringWriter stringWriter = new StringWriter();
        JAXBContext context = null;
        Marshaller marshaller = null;
        try {
            if (JAXBContext.newInstance(Melding.class) != null)
                context = JAXBContext.newInstance(Melding.class);
            marshaller = context.createMarshaller();
            marshaller.marshal(melding, stringWriter);
        } catch (Exception e) {
            throw new RuntimeException("Feil ved oppretting av dokument xml: ", e); //TODO Feilhåndter
        }
        return stringWriter.getBuffer().toString();
    }
}
