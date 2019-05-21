package no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.infotrygd.beregningsgrunnlag;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("sykepenger")
public class InfotrygdSykepengerBeregningsgrunnlag extends InfotrygdBeregningsgrunnlagPeriodeYtelse {

    @JsonProperty("inntektsgrunnlagProsent")
    private Integer inntektsgrunnlagProsent;

    public Integer getInntektsgrunnlagProsent() {
        return inntektsgrunnlagProsent;
    }

    public void setInntektsgrunnlagProsent(Integer inntektsgrunnlagProsent) {
        this.inntektsgrunnlagProsent = inntektsgrunnlagProsent;
    }
}
