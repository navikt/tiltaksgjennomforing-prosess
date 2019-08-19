package no.nav.tag.tiltaksgjennomforingprosess.journalpost.factory;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDateTime;

public class LocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {

    public LocalDateTime unmarshal(String string) {
        return LocalDateTime.parse(string);
    }

    public String marshal(LocalDateTime localDateTime) {
        return localDateTime.toString();
    }
}
