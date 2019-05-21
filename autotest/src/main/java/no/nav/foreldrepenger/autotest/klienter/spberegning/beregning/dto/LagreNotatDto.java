package no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LagreNotatDto {

    protected long beregningId;
    protected String notat;
    protected long beregningsgrunnlagId;
    
    
    public LagreNotatDto(long beregningId, String notat, long beregningsgrunnlagId) {
        super();
        this.beregningId = beregningId;
        this.notat = notat;
        this.beregningsgrunnlagId = beregningsgrunnlagId;
    }
    
    public LagreNotatDto() {
    }
}
