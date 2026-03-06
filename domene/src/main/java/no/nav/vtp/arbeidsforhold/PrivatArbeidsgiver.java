package no.nav.vtp.arbeidsforhold;

import no.nav.vtp.ident.PersonIdent;

public record PrivatArbeidsgiver(PersonIdent ident) implements Arbeidsgiver {
    @Override
    public String identifikator() {
        return ident.fnr();
    }
}
