package no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.dto.beregning;

import java.time.LocalDate;
import java.util.List;

public class BeregningsgrunnlagPeriodeDto {
    LocalDate beregningsgrunnlagPeriodeFom;
    LocalDate beregningsgrunnlagPeriodeTom;
    Double beregnetPrAar;
    Double bruttoPrAar;
    Double bruttoInkludertBortfaltNaturalytelsePrAar;
    List<BeregningsgrunnlagPrStatusOgAndelDto> beregningsgrunnlagPrStatusOgAndel;
    
    public BeregningsgrunnlagPeriodeDto() {
    }
}
