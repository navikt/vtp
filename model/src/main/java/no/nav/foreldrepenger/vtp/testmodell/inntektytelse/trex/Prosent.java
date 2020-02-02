package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.trex;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public class Prosent {

    private final Integer prosent;

    @JsonCreator
    public static Prosent forValue(Integer value) {
        return new Prosent(value);
    }

    private Prosent(Integer prosent) {
        this.prosent = prosent;
    }

    @Override
    public int hashCode() {
        return Objects.hash(prosent);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Prosent)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        Prosent that = (Prosent) obj;
        return Objects.equals(that.prosent, this.prosent);
    }

    @JsonValue
    public Integer getProsent() {
        return prosent;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [prosent=" + prosent + "]";
    }
}
