package no.nav.tag.tiltaksgjennomforingprosess.domene.journalpost;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Avsender {

    private static final String ID_TYPE_ORGNR  = "ORGNR";

    private final String idType = ID_TYPE_ORGNR;
    private final String id;
    private final String navn;
}
