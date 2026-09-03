package no.nav.foreldrepenger.vtp.kontrakter.person.v2;

public record RegistrertNæringsvirksomhetDto(
        String organisasjonsnummer,
        String navn,
        String organisasjonsformKode,
        String organisasjonsformBeskrivelse,
        String næringskode,
        String næringskodeBeskrivelse) {
}
