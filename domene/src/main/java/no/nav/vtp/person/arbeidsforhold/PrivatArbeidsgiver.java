package no.nav.vtp.person.arbeidsforhold;

import no.nav.vtp.person.ident.PersonIdent;

public record PrivatArbeidsgiver(PersonIdent ident) implements Arbeidsgiver {
    @Override
    public String identifikator() {
        return ident.fnr();
    }
}
