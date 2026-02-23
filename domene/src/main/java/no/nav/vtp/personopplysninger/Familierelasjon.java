package no.nav.vtp.personopplysninger;

import no.nav.vtp.ident.PersonIdent;

public record Familierelasjon(Relasjon relasjon, PersonIdent relatertTilId) {

    public enum Relasjon {
        EKTE,
        SAMBOER,
        BARN,
        FAR,
        MOR,
        MEDMOR
    }
}
