package no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.inntektkomponent;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class InntektskomponentModell {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("inntektsperioder")
    List<Inntektsperiode> inntektsperioder  = new ArrayList<>();

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("frilansarbeidsforholdperioder")
    List<FrilansArbeidsforholdsperiode> frilansarbeidsforholdperioder = new ArrayList<>();

    public void setInntektsperioder(List<Inntektsperiode> inntektsperioder) {
        this.inntektsperioder.clear();
        this.inntektsperioder.addAll(inntektsperioder);
    }

    public List<Inntektsperiode> getInntektsperioder(){
        return inntektsperioder;
    }

}
