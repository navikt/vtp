package no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.dto.beregning;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BeregningsgrunnlagPeriodeDto {
    protected LocalDate beregningsgrunnlagPeriodeFom;
    protected LocalDate beregningsgrunnlagPeriodeTom;
    protected Double beregnetPrAar;
    protected Double bruttoPrAar;
    protected Double bruttoInkludertBortfaltNaturalytelsePrAar;
    protected List<BeregningsgrunnlagPrStatusOgAndelDto> beregningsgrunnlagPrStatusOgAndel;
    
    public BeregningsgrunnlagPeriodeDto() {
    }
}
