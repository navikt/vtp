package no.nav.vtp.personopplysninger;

import java.util.UUID;

public record Familierelasjon(Relasjon relasjon, UUID relatertTilId) {

    public enum Relasjon {
        EKTE,
        SAMBOER,
        BARN,
        FAR,
        MOR,
        MEDMOR
    }
}
