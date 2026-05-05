package no.nav.tjeneste.virksomhet.organisasjon.rs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OrganisasjonNoekkelinfo(AdresseEReg adresse,
                                      String organisasjonsnummer,
                                      String enhetstype,
                                      OrganisasjonResponse.Navn navn,
                                      String opphoersdato) {
}
