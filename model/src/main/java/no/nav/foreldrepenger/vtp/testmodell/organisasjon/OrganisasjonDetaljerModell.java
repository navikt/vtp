package no.nav.foreldrepenger.vtp.testmodell.organisasjon;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public record OrganisasjonDetaljerModell(LocalDate registreringsDato,
                                         LocalDate datoSistEndret,
                                         List<AdresseEReg> forretningsadresser,
                                         List<AdresseEReg> postadresser,
                                         LocalDate opphoersdato) {
}

