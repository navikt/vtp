package no.nav.dokarkiv.dto;

import java.time.LocalDateTime;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AvsluttSakRequest(
    String tema,
    String fagsakId,
    String fagsaksystem,
    Bruker bruker,
    LocalDateTime opprettetDato,
    LocalDateTime avsluttetDato,
    String administrativEnhet,
    String sakAnsvarlig
) {

    public AvsluttSakRequest{
        Objects.requireNonNull(tema, "tema cannot be null");
        Objects.requireNonNull(fagsakId, "fagsakId cannot be null");
        Objects.requireNonNull(fagsaksystem, "fagsaksystem cannot be null");
        Objects.requireNonNull(bruker, "bruker cannot be null");
        Objects.requireNonNull(opprettetDato, "opprettetDato cannot be null");
        Objects.requireNonNull(administrativEnhet, "administrativEnhet cannot be null");
    }
}
