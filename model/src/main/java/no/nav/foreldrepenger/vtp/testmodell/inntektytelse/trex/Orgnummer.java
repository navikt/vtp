package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.trex;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public class Orgnummer {

    private final String orgnr;

    @JsonCreator
    public static Orgnummer forValue(@JsonProperty("orgnr") String orgnr) {
        return new Orgnummer(orgnr);
    }

    private Orgnummer(String orgnr) {
        this.orgnr = orgnr;
    }

    @JsonValue
    public String getOrgnr() {
        return orgnr;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orgnr);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Orgnummer)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        Orgnummer that = (Orgnummer) obj;
        return Objects.equals(that.orgnr, this.orgnr);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[orgnr=" + orgnr + "]";
    }
}
