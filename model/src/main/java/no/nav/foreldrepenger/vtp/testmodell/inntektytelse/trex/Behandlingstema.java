package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.trex;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Behandlingstema {

    private final BehandlingstemaKode kode;
    private final String termnavn;

    @JsonCreator
    public Behandlingstema(@JsonProperty("kode") BehandlingstemaKode kode, @JsonProperty("termnavn") String termnavn) {
        this.kode = kode;
        this.termnavn = termnavn;
    }

    public BehandlingstemaKode getKode() {
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
        if (obj == null || !(obj instanceof Behandlingstema)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        Behandlingstema that = (Behandlingstema) obj;
        return Objects.equals(that.kode, this.kode) &&
                Objects.equals(that.termnavn, this.termnavn);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[kode=" + kode + ", termnavn=" + termnavn + "]";
    }

}
