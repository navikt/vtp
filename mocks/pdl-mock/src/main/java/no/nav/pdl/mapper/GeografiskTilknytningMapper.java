package no.nav.pdl.mapper;

import no.nav.pdl.GeografiskTilknytning;
import no.nav.pdl.GtType;
import no.nav.vtp.person.Person;

public class GeografiskTilknytningMapper {

    private GeografiskTilknytningMapper() {
    }

    public static GeografiskTilknytning tilGeografiskTilknytning(Person person) {
        var geografiskTilknytning = new GeografiskTilknytning();
        var tilknytning = person.personopplysninger().geografiskTilknytning();
        if (tilknytning == null) {
            return geografiskTilknytning;
        } else {
            switch (tilknytning.type()) {
                case LAND -> {
                    geografiskTilknytning.setGtType(GtType.UTLAND);
                    geografiskTilknytning.setGtLand(tilknytning.land());
                }
                case KOMMUNE -> {
                    geografiskTilknytning.setGtType(GtType.KOMMUNE);
                    geografiskTilknytning.setGtKommune(tilknytning.land());
                }
                case BYDEL -> {
                    geografiskTilknytning.setGtType(GtType.BYDEL);
                    geografiskTilknytning.setGtBydel(tilknytning.land());
                }
                default -> geografiskTilknytning.setGtType(GtType.UDEFINERT);
            }
            geografiskTilknytning.setRegel("UVISST");
            return geografiskTilknytning;
        }
    }
}
