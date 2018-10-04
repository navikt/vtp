package no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.arbeidsforhold;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArbeidsforholdModell {

    @JsonProperty("arbeidsforhold")
    private List<Arbeidsforhold> arbeidsforhold = new ArrayList<>();

    public List<Arbeidsforhold> getArbeidsforhold() {
        return Collections.unmodifiableList(arbeidsforhold);
    }

    public void setArbeidsforhold(List<Arbeidsforhold> arbeidsforhold) {
        this.arbeidsforhold = arbeidsforhold;
    }

    public void leggTil(Arbeidsforhold arbeidsforhold){
        this.arbeidsforhold.add(arbeidsforhold);
    }
}
