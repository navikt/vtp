package no.nav.omsorgspenger.rammemeldinger;

import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.omsorgspenger.AleneOmOmsorgen;

import java.util.ArrayList;
import java.util.List;

public class AleneOmOmsorgenResponse {
    @JsonProperty("aleneOmOmsorgen")
    private List<AleneOmOmsorgen> aleneOmOmsorgen = new ArrayList<>();

    public List<AleneOmOmsorgen> getAleneOmOmsorgen() {
        return aleneOmOmsorgen;
    }

    public void setAleneOmOmsorgen(List<AleneOmOmsorgen> aleneOmOmsorgen) {
        this.aleneOmOmsorgen = aleneOmOmsorgen;
    }
}
