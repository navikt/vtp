package no.nav.foreldrepenger.fpmock2.server.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Oauth2AccessTokenResponse {
    @JsonProperty("id_token")
    private String idToken;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("expires_in")
    private int expiresIn = 3600;

    @JsonProperty("token_type")
    private String tokenType = "JWKS";

    public Oauth2AccessTokenResponse(String idToken, String refreshToken) {
        this.idToken = idToken;
        this.refreshToken = refreshToken;
    }

}