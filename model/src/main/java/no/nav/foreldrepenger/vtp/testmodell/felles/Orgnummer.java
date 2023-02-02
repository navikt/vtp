package no.nav.foreldrepenger.vtp.testmodell.felles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public record Orgnummer(@JsonValue String orgnummer) {

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public Orgnummer {
    }

    @Override
    public String orgnummer() {
        return orgnummer;
    }
}
