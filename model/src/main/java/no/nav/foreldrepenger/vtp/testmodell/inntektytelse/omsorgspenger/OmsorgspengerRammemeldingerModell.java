package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.omsorgspenger;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class OmsorgspengerRammemeldingerModell {
    @JsonProperty("aleneOmOmsorgen")
    private List<AleneOmOmsorgen> aleneOmOmsorgen;

    @JsonProperty("overføringerGitt")
    private List<OverføringGitt> overføringerGitt;

    @JsonProperty("overføringerFått")
    private List<OverføringFått> overføringerFått;

    public List<AleneOmOmsorgen> getAleneOmOmsorgen() {
        if(aleneOmOmsorgen == null) {
            aleneOmOmsorgen = new ArrayList<>();
        }
        return aleneOmOmsorgen;
    }

    public void setAleneOmOmsorgen(List<AleneOmOmsorgen> aleneOmOmsorgen) {
        this.aleneOmOmsorgen = aleneOmOmsorgen;
    }

    public List<OverføringGitt> getOverføringerGitt() {
        if(overføringerGitt == null) {
            overføringerGitt = new ArrayList<>();
        }
        return overføringerGitt;
    }

    public void setOverføringerGitt(List<OverføringGitt> overføringerGitt) {
        this.overføringerGitt = overføringerGitt;
    }

    public List<OverføringFått> getOverføringerFått() {
        if(overføringerFått == null) {
            overføringerFått = new ArrayList<>();
        }
        return overføringerFått;
    }

    public void setOverføringerFått(List<OverføringFått> overføringerFått) {
        this.overføringerFått = overføringerFått;
    }
}
