package no.nav.vtp.ansatt;

import java.util.List;
import java.util.UUID;

import no.nav.foreldrepenger.vtp.kontrakter.organisasjon.NavAnsattDto;

public record NavAnsatt(String ident, UUID oid, String displayName, String givenName, String surname, String email,
                        String streetAddress, List<NavGruppe> groups) {

    public NavAnsatt(NavAnsattDto a, List<NavGruppe> groups) {
        this(a.ident(), a.oid(), a.fornavn() + " " + a.etternavn(), a.fornavn(), a.etternavn(),
                epost(a), a.enhetId(), groups);
    }

    private static String epost(NavAnsattDto navAnsattDto) {
        var fornavn = navAnsattDto.fornavn().toLowerCase().replace(" ", ".");
        var etternavn = navAnsattDto.etternavn().toLowerCase().replace(" ", ".");
        return fornavn + "." + etternavn + "@example.com";
    }

}
