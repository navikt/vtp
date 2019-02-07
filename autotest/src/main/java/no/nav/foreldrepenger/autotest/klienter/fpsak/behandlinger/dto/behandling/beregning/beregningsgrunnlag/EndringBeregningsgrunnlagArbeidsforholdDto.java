package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.beregningsgrunnlag;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EndringBeregningsgrunnlagArbeidsforholdDto extends BeregningsgrunnlagArbeidsforholdDto {


    protected List<GraderingEllerRefusjonDto> perioderMedGraderingEllerRefusjon;

    public List<GraderingEllerRefusjonDto> getPerioderMedGraderingEllerRefusjon() {
        return perioderMedGraderingEllerRefusjon;
    }
}
