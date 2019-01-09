package no.nav.foreldrepenger.autotest.klienter.vtp.expect.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExpectRequestDto {

    protected String mock;
    protected String webMethod;
    
    public ExpectRequestDto(String mock, String webMethod) {
        this.mock = mock;
        this.webMethod = webMethod;
    }
    
    public ExpectRequestDto() {
    }
}
