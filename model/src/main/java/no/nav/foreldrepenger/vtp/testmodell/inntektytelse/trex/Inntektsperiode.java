package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.trex;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Inntektsperiode {

    private final InntektsperiodeKode kode;
    private final String termnavn;

    @JsonCreator
    public Inntektsperiode(@JsonProperty("kode") InntektsperiodeKode kode, @JsonProperty("termnavn") String termnavn) {
        this.kode = kode;
        this.termnavn = termnavn;
    }

    public InntektsperiodeKode getKode() {
        return kode;
    }

    public String getTermnavn() {
        return termnavn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(kode, termnavn);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Inntektsperiode)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        Inntektsperiode that = (Inntektsperiode) obj;
        return Objects.equals(that.kode, this.kode) &&
                Objects.equals(that.termnavn, this.termnavn);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[kode=" + kode + ", termnavn=" + termnavn + "]";
    }
}
