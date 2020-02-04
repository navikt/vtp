package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.trex;

import java.time.LocalDate;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Periode {

    private final LocalDate fom;
    private final LocalDate tom;

    @JsonCreator
    public Periode(@JsonProperty("fom") LocalDate fom, @JsonProperty("tom") LocalDate tom) {
        this.fom = fom;
        this.tom = tom;
    }

    public LocalDate getFom() {
        return fom;
    }

    public LocalDate getTom() {
        return tom;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fom, tom);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Periode)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        Periode that = (Periode) obj;
        return Objects.equals(that.fom, this.fom) &&
                Objects.equals(that.tom, this.tom);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[fom=" + fom + ", tom=" + tom + "]";
    }

}
