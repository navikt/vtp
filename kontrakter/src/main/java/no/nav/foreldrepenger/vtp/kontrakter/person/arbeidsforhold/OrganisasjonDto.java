package no.nav.foreldrepenger.vtp.kontrakter.person.arbeidsforhold;

import java.time.LocalDate;

public record OrganisasjonDto(String orgnummer, OrganisasjonsdetaljerDto organisasjonsdetaljer) implements ArbeidsgiverDto {

    @Override
    public String identifikator() {
        return orgnummer;
    }

    public record OrganisasjonsdetaljerDto(String navn, LocalDate registreringsdato) {
    }
}

