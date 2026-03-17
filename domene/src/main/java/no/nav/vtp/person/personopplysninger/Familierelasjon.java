package no.nav.vtp.person.personopplysninger;

import no.nav.vtp.person.ident.PersonIdent;

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
