package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.trex;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Vedtak {

    private final Periode periode;
    private final int utbetalingsgrad;

    @JsonCreator
    public Vedtak(@JsonProperty("periode") Periode periode, @JsonProperty("utbetalingsgrad") int utbetalingsgrad) {
        this.periode = periode;
        this.utbetalingsgrad = utbetalingsgrad;
    }

    public Periode getPeriode() {
        return periode;
    }

    public int getUtbetalingsgrad() {
        return utbetalingsgrad;
    }

    @Override
    public int hashCode() {
        return Objects.hash(periode, utbetalingsgrad);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Vedtak)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        Vedtak that = (Vedtak) obj;
        return Objects.equals(that.periode, this.periode) &&
                Objects.equals(that.utbetalingsgrad, this.utbetalingsgrad);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[periode=" + periode + ", utbetalingsgrad=" + utbetalingsgrad + "]";
    }
}
