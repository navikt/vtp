package no.nav.foreldrepenger.vtp.kontrakter.person.v2;

import java.util.UUID;

public record FamilierelasjonDto(Relasjon relasjon, UUID relatertTilId) {

    public enum Relasjon {
        EKTE,
        SAMBOER,
        BARN,
        FAR,
        MOR,
        MEDMOR
    }
}
