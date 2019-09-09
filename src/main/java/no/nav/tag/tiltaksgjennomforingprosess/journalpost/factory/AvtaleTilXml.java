package no.nav.tag.tiltaksgjennomforingprosess.journalpost.factory;

import no.nav.tag.tiltaksgjennomforingprosess.domene.*;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.ByteArrayOutputStream;

@Component
public class AvtaleTilXml {

    byte[] genererXml(Avtale avtale) throws Exception {

        Generelt generelt = new Generelt(new Arbeidsgiver(avtale.getBedriftNr()), new Arbeidstaker(avtale.getDeltakerFnr()), avtale);
        Innhold innhold = new Innhold(new SkjemaInfo(), generelt);
        Melding melding = new Melding(innhold);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JAXBContext context = JAXBContext.newInstance(Melding.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.marshal(melding, baos);
        baos.close();
        return baos.toByteArray();
    }
}
