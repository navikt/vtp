package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.omsorgspenger;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record OmsorgspengerModell(OmsorgspengerRammemeldingerModell rammemeldinger) {

    public OmsorgspengerModell() {
        this(null);
    }

    @JsonCreator
    public OmsorgspengerModell(@JsonProperty("rammemeldinger") OmsorgspengerRammemeldingerModell rammemeldinger) {
        this.rammemeldinger = Optional.ofNullable(rammemeldinger).orElse(new OmsorgspengerRammemeldingerModell());
    }
}
