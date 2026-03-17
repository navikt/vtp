package no.nav.foreldrepenger.vtp.kontrakter.v2;

public record FamilierelasjonModellDto(Relasjon relasjon, String relatertTilId) {

    public enum Relasjon {
        EKTE,
        SAMBOER,
        BARN,
        FAR,
        MOR,
        MEDMOR
    }
}
