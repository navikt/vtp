package no.nav.foreldrepenger.vtp.kontrakter;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TilbakekrevingKonsistensDto (
    @JsonProperty("saksnummer") String saksnummer,
    @JsonProperty("behandlingId") String behandlingId
) {}
