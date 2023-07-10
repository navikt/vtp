package no.nav.foreldrepenger.vtp.kontrakter.v2;

import com.neovisionaries.i18n.CountryCode;

public record GeografiskTilknytningDto(CountryCode land, GeografiskTilknytningType type) {


    public static GeografiskTilknytningDto norsk() {
        return new GeografiskTilknytningDto(CountryCode.NO, GeografiskTilknytningType.LAND);
    }

    public enum GeografiskTilknytningType {
        BYDEL,
        KOMMUNE,
        LAND
    }
}
