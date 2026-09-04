package no.nav.vtp.person.personopplysninger;

public record GeografiskTilknytning(String land, GeografiskTilknytningType type) {

    public enum GeografiskTilknytningType {
        BYDEL,
        KOMMUNE,
        LAND
    }
}
