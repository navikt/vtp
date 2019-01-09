package no.nav.foreldrepenger.autotest.klienter.vtp.expect.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExpectTokenDto {
    
    public String mock;
    public String webMethod;
    public String token;
    
    public ExpectTokenDto() {
    }
}
