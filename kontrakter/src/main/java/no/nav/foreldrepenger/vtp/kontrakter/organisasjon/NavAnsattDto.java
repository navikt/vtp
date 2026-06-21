package no.nav.foreldrepenger.vtp.kontrakter.organisasjon;

import java.util.List;
import java.util.UUID;

public record NavAnsattDto(String ident, UUID oid, String fornavn, String etternavn, String enhetId, List<String> grupper) {
}
