package no.nav.foreldrepenger.autotest.klienter.vtp.expect.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExpectRequestDto {

    protected String mock;
    protected String webMethod;
    protected Map<String, String> predicate;
    
    public ExpectRequestDto(String mock, String webMethod, Map<String, String> predicate) {
        this(mock, webMethod);
        this.predicate = predicate;
    }
    
    public ExpectRequestDto(String mock, String webMethod) {
        this.mock = mock;
        this.webMethod = webMethod;
    }
    
    public ExpectRequestDto() {
    }
}
