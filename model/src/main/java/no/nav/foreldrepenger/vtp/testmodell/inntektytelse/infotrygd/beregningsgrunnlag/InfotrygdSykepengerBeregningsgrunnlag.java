package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.infotrygd.beregningsgrunnlag;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("sykepenger")
public class InfotrygdSykepengerBeregningsgrunnlag extends InfotrygdBeregningsgrunnlagPeriodeYtelse {

    private Integer inntektsgrunnlagProsent;

    public Integer getInntektsgrunnlagProsent() {
        return inntektsgrunnlagProsent;
    }

    public void setInntektsgrunnlagProsent(Integer inntektsgrunnlagProsent) {
        this.inntektsgrunnlagProsent = inntektsgrunnlagProsent;
    }
}
