package no.nav.tjeneste.virksomhet.organisasjon.rs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import no.nav.foreldrepenger.vtp.testmodell.organisasjon.AdresseEReg;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OrganisasjonNoekkelinfo(AdresseEReg adresse,
                                      String organisasjonsnummer,
                                      String enhetstype,
                                      OrganisasjonResponse.Navn navn,
                                      String opphoersdato) {
}
