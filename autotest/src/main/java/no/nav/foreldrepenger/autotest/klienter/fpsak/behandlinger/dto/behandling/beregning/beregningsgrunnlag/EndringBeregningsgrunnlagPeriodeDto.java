package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.beregningsgrunnlag;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EndringBeregningsgrunnlagPeriodeDto {

    protected LocalDate fom; 
    protected LocalDate tom;
    protected List<EndringBeregningsgrunnlagAndelDto> endringBeregningsgrunnlagAndeler;
    protected boolean harPeriodeAarsakGraderingEllerRefusjon;
    protected boolean skalKunneEndreRefusjon;
}
