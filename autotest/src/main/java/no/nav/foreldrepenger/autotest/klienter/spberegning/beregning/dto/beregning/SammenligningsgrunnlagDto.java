package no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.dto.beregning;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SammenligningsgrunnlagDto {
    protected LocalDate sammenligningsgrunnlagFom;
    protected LocalDate sammenligningsgrunnlagTom;
    protected Double rapportertPrAar;
    
    public SammenligningsgrunnlagDto() {
    }
}
