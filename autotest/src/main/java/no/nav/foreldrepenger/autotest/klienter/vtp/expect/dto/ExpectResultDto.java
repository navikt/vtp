package no.nav.foreldrepenger.autotest.klienter.vtp.expect.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExpectResultDto {

    protected boolean isExpectationMet;
    
    public ExpectResultDto() {
    }

    public boolean isExpectationMet() {
        return isExpectationMet;
    }
    
    
}
