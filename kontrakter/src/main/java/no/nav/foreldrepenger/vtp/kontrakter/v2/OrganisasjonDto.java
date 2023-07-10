package no.nav.foreldrepenger.vtp.kontrakter.v2;

import java.time.LocalDate;

public record OrganisasjonDto(Orgnummer orgnummer, OrganisasjonsdetaljerDto organisasjonsdetaljer) implements Arbeidsgiver {

    public record OrganisasjonsdetaljerDto(String navn,
                                           LocalDate registreringsdato,
                                           LocalDate datoSistEndret) {
    }
}
