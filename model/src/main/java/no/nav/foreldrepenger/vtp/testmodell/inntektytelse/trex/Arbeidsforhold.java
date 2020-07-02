package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.trex;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Arbeidsforhold {

    private final Orgnummer orgnr;
    private final Integer inntekt;
    private final Inntektsperiode inntektperiode;
    private final Boolean refusjon;

    @JsonCreator
    public Arbeidsforhold(
            @JsonProperty("orgnr") @JsonAlias("arbeidsgiverOrgnr") Orgnummer orgnr,
            @JsonProperty("inntekt") @JsonAlias("inntektForPerioden") Integer inntekt,
            @JsonProperty("inntektperiode") Inntektsperiode inntektperiode,
            @JsonProperty("refusjon") Boolean refusjon) {
        this.orgnr = orgnr;
        this.inntekt = inntekt;
        this.inntektperiode = inntektperiode;
        this.refusjon = refusjon;
    }

    public Orgnummer getOrgnr() {
        return orgnr;
    }

    public Integer getInntekt() {
        return inntekt;
    }

    public Inntektsperiode getInntektperiode() {
        return inntektperiode;
    }

    public Boolean getRefusjon() {
        return refusjon;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orgnr, inntekt, inntektperiode, refusjon);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Arbeidsforhold)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        Arbeidsforhold that = (Arbeidsforhold) obj;
        return Objects.equals(that.orgnr, this.orgnr) &&
                Objects.equals(that.inntekt, this.inntekt) &&
                Objects.equals(that.inntektperiode, this.inntektperiode) &&
                Objects.equals(that.refusjon, this.refusjon);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[orgnr=" + orgnr + ", inntekt=" + inntekt + ", inntektperiode="
                + inntektperiode + ", refusjon=" + refusjon + "]";
    }
}
