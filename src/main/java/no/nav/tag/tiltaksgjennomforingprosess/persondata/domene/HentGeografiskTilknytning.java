package no.nav.tag.tiltaksgjennomforingprosess.persondata.domene;

public record HentGeografiskTilknytning(
   String gtKommune,
   String gtBydel,
   String gtLand,
   String regel
) {
   String getGeoTilknytning(){
        if(gtBydel == null){
            return gtKommune;
        }
        return gtBydel;
    }
}
