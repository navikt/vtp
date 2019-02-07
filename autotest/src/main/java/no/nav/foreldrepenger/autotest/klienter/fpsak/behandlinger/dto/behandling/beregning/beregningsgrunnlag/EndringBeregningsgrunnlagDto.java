package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.beregningsgrunnlag;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EndringBeregningsgrunnlagDto {

    protected List<EndringBeregningsgrunnlagPeriodeDto> endringBeregningsgrunnlagPerioder;
    protected List<EndringBeregningsgrunnlagArbeidsforholdDto> endredeArbeidsforhold;

    public List<EndringBeregningsgrunnlagPeriodeDto> getEndringBeregningsgrunnlagPerioder() {
        return endringBeregningsgrunnlagPerioder;
    }

    public List<EndringBeregningsgrunnlagArbeidsforholdDto> getEndredeArbeidsforhold() {
        return endredeArbeidsforhold;
    }
}
