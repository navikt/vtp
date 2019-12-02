package no.nav.aktoerregister.rest.api.v1;

import java.util.List;
import java.util.Objects;

public class IdentinfoForAktoer {

    private List<Identinfo> identer;
    private String feilmelding;

    public IdentinfoForAktoer() {
    }

    public IdentinfoForAktoer(List<Identinfo> identer, String feilmelding) {
        this.identer = identer;
        this.feilmelding = feilmelding;
    }

    public List<Identinfo> getIdenter() {
        return identer;
    }

    public void setIdenter(List<Identinfo> identer) {
        this.identer = identer;
    }

    public String getFeilmelding() {
        return feilmelding;
    }

    public void setFeilmelding(String feilmelding) {
        this.feilmelding = feilmelding;
    }

    @Override
    public String toString() {
        return "IdentinfoForAktoer{" + "identer=" + identer +
                ", feilmelding=" + feilmelding +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IdentinfoForAktoer that = (IdentinfoForAktoer) o;
        return Objects.equals(getIdenter(), that.getIdenter()) &&
                Objects.equals(getFeilmelding(), that.getFeilmelding());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdenter(), getFeilmelding());
    }
}


