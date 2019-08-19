package no.nav.tag.tiltaksgjennomforingprosess.journalpost.factory;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;

public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {

    public LocalDate unmarshal(String string) {
        return LocalDate.parse(string);
    }

    public String marshal(LocalDate localDate) {
        return localDate.toString();
    }
}
