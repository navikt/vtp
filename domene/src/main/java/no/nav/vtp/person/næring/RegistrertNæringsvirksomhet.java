package no.nav.vtp.person.næring;

public record RegistrertNæringsvirksomhet(
        String organisasjonsnummer,
        String navn,
        String organisasjonsformKode,
        String organisasjonsformBeskrivelse,
        String næringskode,
        String næringskodeBeskrivelse) {
}
