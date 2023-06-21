package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.trex;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public record Orgnummer(@JsonValue String orgnummer) {

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public Orgnummer {
    }
}
