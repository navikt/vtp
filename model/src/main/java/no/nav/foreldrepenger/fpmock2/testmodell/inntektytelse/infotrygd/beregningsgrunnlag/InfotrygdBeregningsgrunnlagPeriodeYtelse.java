package no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.infotrygd.beregningsgrunnlag;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class InfotrygdBeregningsgrunnlagPeriodeYtelse extends InfotrygdBeregningsgrunnlag {
    
    @JsonProperty("arbeidskategori")
    private InfotrygdArbeidskategori arbeidskategori;

    @JsonInclude(Include.NON_EMPTY)
    @JsonProperty("arbeidsforhold")
    private List<InfotrygdArbeidsforhold> arbeidsforhold = new ArrayList<>();

    public InfotrygdArbeidskategori getArbeidskategori() {
        return arbeidskategori;
    }

    public void setArbeidskategori(InfotrygdArbeidskategori arbeidskategori) {
        this.arbeidskategori = arbeidskategori;
    }

    public List<InfotrygdArbeidsforhold> getArbeidsforhold() {
        return arbeidsforhold;
    }

    public void setArbeidsforhold(List<InfotrygdArbeidsforhold> arbeidsforhold) {
        this.arbeidsforhold.clear();
        this.arbeidsforhold.addAll(arbeidsforhold);
    }

}
