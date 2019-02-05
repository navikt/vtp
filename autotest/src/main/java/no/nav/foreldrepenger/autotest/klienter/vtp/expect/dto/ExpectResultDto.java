package no.nav.foreldrepenger.autotest.klienter.vtp.expect.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExpectResultDto {

    protected boolean isExpectationMet;
    protected String resultData;
    
    public ExpectResultDto() {
    }

    public boolean isExpectationMet() {
        return isExpectationMet;
    }
    
    public String getResultData() {
        return resultData;
    }
    
    
}
