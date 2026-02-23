package no.nav.vtp.personopplysninger;

import com.neovisionaries.i18n.CountryCode;

public record GeografiskTilknytning(CountryCode land, GeografiskTilknytningType type) {


    public static GeografiskTilknytning norsk() {
        return new GeografiskTilknytning(CountryCode.NO, GeografiskTilknytningType.KOMMUNE);
    }

    public enum GeografiskTilknytningType {
        BYDEL,
        KOMMUNE,
        LAND
    }
}
