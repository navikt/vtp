package no.nav.pdl.oversetter;

import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonModell;
import no.nav.pdl.GeografiskTilknytning;
import no.nav.pdl.GtType;

public class GeografiskTilknytningAdapter {

    public static GeografiskTilknytning tilGeografiskTilknytning(PersonModell bruker) {
        var tilknytning = bruker.getGeografiskTilknytning();
        if (tilknytning == null) {
            return null;
        } else {
            GeografiskTilknytning geo = new GeografiskTilknytning();
            switch (tilknytning.getGeografiskTilknytningType()) {
                case Land:
                    geo.setGtType(GtType.UTLAND);
                    geo.setGtLand(tilknytning.getKode());
                    break;
                case Kommune:
                    geo.setGtType(GtType.KOMMUNE);
                    geo.setGtKommune(tilknytning.getKode());
                    break;
                case Bydel:
                    geo.setGtType(GtType.BYDEL);
                    geo.setGtBydel(tilknytning.getKode());
                default:
                    geo.setGtType(GtType.UDEFINERT);
            }
            return geo;
        }
    }
}
