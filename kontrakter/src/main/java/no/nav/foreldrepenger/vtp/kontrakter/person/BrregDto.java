package no.nav.foreldrepenger.vtp.kontrakter.person;

import java.util.List;

public record BrregDto(List<VirksomhetDto> virksomheter) {

    public BrregDto {
        virksomheter = virksomheter == null ? List.of() : List.copyOf(virksomheter);
    }

    public record VirksomhetDto(
            String organisasjonsnummer,
            String navn,
            String organisasjonsformKode,
            String organisasjonsformBeskrivelse,
            String næringskode,
            String næringskodeBeskrivelse) {
    }
}
