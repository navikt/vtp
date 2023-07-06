package no.nav.foreldrepenger.vtp.kontrakter.v2;

import java.util.UUID;

public record FamilierelasjonModellDto(Relasjon relasjon, UUID relatertTilId) {

    public enum Relasjon {
        EKTE,
        SAMBOER,
        BARN,
        FAR,
        MOR
    }
}
