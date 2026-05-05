package no.nav.vtp.person.personopplysninger;

import com.neovisionaries.i18n.CountryCode;

public record GeografiskTilknytning(CountryCode land, GeografiskTilknytningType type) {

    public enum GeografiskTilknytningType {
        BYDEL,
        KOMMUNE,
        LAND
    }
}
