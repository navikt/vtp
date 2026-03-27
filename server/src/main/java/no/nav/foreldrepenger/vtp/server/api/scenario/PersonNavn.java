package no.nav.foreldrepenger.vtp.server.api.scenario;

import no.nav.vtp.person.personopplysninger.Kjønn;

public record PersonNavn(String fornavn, String etternavn, Kjønn kjønn) {}
