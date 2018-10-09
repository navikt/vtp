package no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.sigrun;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class SigrunModell {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("inntektsår")
    private List<Inntektsår> inntektsår = new ArrayList<>();

    public List<Inntektsår> getInntektsår() { return inntektsår; }

    public void setInntektsår(List<Inntektsår> inntektsår) {
        this.inntektsår.clear();
        this.inntektsår.addAll(inntektsår);
    }

}
