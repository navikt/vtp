package no.nav.foreldrepenger.vtp.testmodell.felles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


public record Prosent(@JsonValue Integer prosent) {

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public Prosent {
    }

    @Override
    public Integer prosent() {
        return prosent;
    }
}
