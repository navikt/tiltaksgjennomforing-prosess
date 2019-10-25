package no.nav.tag.tiltaksgjennomforingprosess.factory;

import com.thoughtworks.xstream.XStream;
import no.nav.tag.tiltaksgjennomforingprosess.domene.avtale.*;
import org.springframework.stereotype.Component;

@Component
public class AvtaleTilXml {

    String genererXml(Avtale avtale){

        Generelt generelt = new Generelt(new Arbeidsgiver(avtale.getBedriftNr(), avtale.getBedriftNavn()), new Arbeidstaker(avtale.getDeltakerFnr()), avtale);
        Innhold innhold = new Innhold(new SkjemaInfo(avtale.getStartDato(), avtale.getSluttDato()), generelt);
        Melding melding = new Melding(innhold);

        XStream xstream = new XStream();
        xstream.processAnnotations(Melding.class);
        xstream.processAnnotations(Avtale.class);
        xstream.processAnnotations(Maal.class);
        xstream.processAnnotations(Oppgave.class);

        return xstream.toXML(melding);
    }
}
