package no.nav.foreldrepenger.vtp.kontrakter.person.v2;

public record GeografiskTilknytningDto(String land, GeografiskTilknytningType type) {

    public static GeografiskTilknytningDto norsk() {
        return new GeografiskTilknytningDto("NOR", GeografiskTilknytningType.KOMMUNE);
    }

    public enum GeografiskTilknytningType {
        BYDEL,
        KOMMUNE,
        LAND
    }
}
