package no.nav.pdl.oversetter;

import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonModell;
import no.nav.pdl.GeografiskTilknytning;
import no.nav.pdl.GtType;

public class GeografiskTilknytningAdapter {

    private GeografiskTilknytningAdapter() {
    }

    public static GeografiskTilknytning tilGeografiskTilknytning(PersonModell bruker) {
        var geografiskTilknytning = new GeografiskTilknytning();
        var tilknytning = bruker.getGeografiskTilknytning();
        if (tilknytning == null) {
            return geografiskTilknytning;
        } else {
            switch (tilknytning.getGeografiskTilknytningType()) {
                case Land -> {
                    geografiskTilknytning.setGtType(GtType.UTLAND);
                    geografiskTilknytning.setGtLand(tilknytning.getKode());
                }
                case Kommune -> {
                    geografiskTilknytning.setGtType(GtType.KOMMUNE);
                    geografiskTilknytning.setGtKommune(tilknytning.getKode());
                }
                case Bydel -> {
                    geografiskTilknytning.setGtType(GtType.BYDEL);
                    geografiskTilknytning.setGtBydel(tilknytning.getKode());
                }
                default -> geografiskTilknytning.setGtType(GtType.UDEFINERT);
            }
            geografiskTilknytning.setRegel("UVISST");
            return geografiskTilknytning;
        }
    }
}
