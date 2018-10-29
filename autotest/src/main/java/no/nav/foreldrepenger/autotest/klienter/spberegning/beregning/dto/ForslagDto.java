package no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ForslagDto {

    protected Integer beregningId;

    public ForslagDto() {
    }
    
    public Integer getBeregningId() {
        return beregningId;
    }
}
