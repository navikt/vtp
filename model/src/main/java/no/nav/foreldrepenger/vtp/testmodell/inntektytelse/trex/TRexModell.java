package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.trex;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TRexModell {

    @JsonInclude(Include.NON_EMPTY)
    @JsonProperty("foreldrepenger")
    private List<Grunnlag> foreldrepenger = new ArrayList<>();

    @JsonInclude(Include.NON_EMPTY)
    @JsonProperty("svangerskapspenger")
    private List<Grunnlag> svangerskapspenger = new ArrayList<>();

    @JsonInclude(Include.NON_EMPTY)
    @JsonProperty("sykepenger")
    private List<Grunnlag> sykepenger = new ArrayList<>();

    @JsonInclude(Include.NON_EMPTY)
    @JsonProperty("barnsykdom")
    private List<Grunnlag> barnsykdom = new ArrayList<>();

    public List<Grunnlag> getForeldrepenger() {
        return foreldrepenger;
    }

    public void setForeldrepenger(List<Grunnlag> foreldrepenger) {
        this.foreldrepenger = foreldrepenger;
    }

    public List<Grunnlag> getSvangerskapspenger() {
        return svangerskapspenger;
    }

    public void setSvangerskapspenger(List<Grunnlag> svangerskapspenger) {
        this.svangerskapspenger = svangerskapspenger;
    }

    public List<Grunnlag> getSykepenger() {
        return sykepenger;
    }

    public void setSykepenger(List<Grunnlag> sykepenger) {
        this.sykepenger = sykepenger;
    }

    public List<Grunnlag> getBarnsykdom() {
        return barnsykdom;
    }

    public void setBarnsykdom(List<Grunnlag> barnsykdom) {
        this.barnsykdom = barnsykdom;
    }
}
