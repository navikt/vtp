package no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LagreNotatDto {

    protected int beregningId;
    protected String notat;
    protected int beregningsgrunnlagId;
    
    
    public LagreNotatDto(int beregningId, String notat, int beregningsgrunnlagId) {
        super();
        this.beregningId = beregningId;
        this.notat = notat;
        this.beregningsgrunnlagId = beregningsgrunnlagId;
    }
    
    public LagreNotatDto() {
    }
}
