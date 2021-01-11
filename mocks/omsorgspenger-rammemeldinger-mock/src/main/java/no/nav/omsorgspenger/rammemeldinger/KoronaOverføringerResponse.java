package no.nav.omsorgspenger.rammemeldinger;

import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.omsorgspenger.KoronaOverføringFått;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.omsorgspenger.KoronaOverføringGitt;

import java.util.ArrayList;
import java.util.List;

public class KoronaOverføringerResponse {
    @JsonProperty("gitt")
    private List<KoronaOverføringGitt> gitt = new ArrayList<>();

    @JsonProperty("fått")
    private List<KoronaOverføringFått> fått = new ArrayList<>();

    public List<KoronaOverføringGitt> getGitt() {
        return gitt;
    }

    public void setGitt(List<KoronaOverføringGitt> gitt) {
        this.gitt = gitt;
    }

    public List<KoronaOverføringFått> getFått() {
        return fått;
    }

    public void setFått(List<KoronaOverføringFått> fått) {
        this.fått = fått;
    }
}
