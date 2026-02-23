package no.nav.foreldrepenger.vtp.kontrakter.person;

import java.util.UUID;

public record FamilierelasjonModellDto(Relasjon relasjon, UUID relatertTilId) {

    public enum Relasjon {
        EKTE,
        SAMBOER,
        BARN,
        FAR,
        MOR,
        MEDMOR
    }
}
