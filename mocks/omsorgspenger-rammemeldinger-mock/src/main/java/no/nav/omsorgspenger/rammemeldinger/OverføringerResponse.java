package no.nav.omsorgspenger.rammemeldinger;

import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.omsorgspenger.OverføringFått;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.omsorgspenger.OverføringGitt;

import java.util.ArrayList;
import java.util.List;

public class OverføringerResponse {
    @JsonProperty("gitt")
    private List<OverføringGitt> gitt = new ArrayList<>();

    @JsonProperty("fått")
    private List<OverføringFått> fått = new ArrayList<>();

    public List<OverføringGitt> getGitt() {
        return gitt;
    }

    public void setGitt(List<OverføringGitt> gitt) {
        this.gitt = gitt;
    }

    public List<OverføringFått> getFått() {
        return fått;
    }

    public void setFått(List<OverføringFått> fått) {
        this.fått = fått;
    }
}
