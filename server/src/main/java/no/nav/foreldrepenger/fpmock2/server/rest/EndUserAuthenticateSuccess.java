package no.nav.foreldrepenger.fpmock2.server.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EndUserAuthenticateSuccess {

    @JsonProperty("tokenId")
    private String tokenId;
    
    @JsonProperty("successUrl")
    private String successUrl;

    public EndUserAuthenticateSuccess() {
        // må ha default ctor
    }
    
    public EndUserAuthenticateSuccess(String tokenId, String successUrl) {
        this.tokenId = tokenId;
        this.successUrl = successUrl;
    }
    
    public String getTokenId() {
        return tokenId;
    }

    public String getSuccessUrl() {
        return successUrl;
    }
}
