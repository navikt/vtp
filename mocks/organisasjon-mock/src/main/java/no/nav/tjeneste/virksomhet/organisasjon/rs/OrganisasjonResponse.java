package no.nav.tjeneste.virksomhet.organisasjon.rs;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import no.nav.foreldrepenger.vtp.testmodell.organisasjon.AdresseEReg;
import no.nav.foreldrepenger.vtp.testmodell.organisasjon.OrganisasjonstypeEReg;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OrganisasjonResponse(String organisasjonsnummer,
                                   OrganisasjonstypeEReg type,
                                   Navn navn,
                                   OrganisasjonDetaljer organisasjonDetaljer) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    record OrganisasjonDetaljer(LocalDateTime registreringsdato,
                                LocalDate opphoersdato,
                                List<AdresseEReg> forretningsadresser,
                                List<AdresseEReg> postadresser) {
    }

    record Navn(String sammensattnavn,
                String navnelinje1,
                String navnelinje2,
                String navnelinje3,
                String navnelinje4,
                String navnelinje5) {
    }
}
