package no.nav.foreldrepenger.vtp.server.rest.auth;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Oauth2AccessTokenResponse {
    @JsonProperty("id_token")
    private String idToken;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private int expiresIn = 3600;

    @JsonProperty("token_type")
    private String tokenType = "JWKS";

    public Oauth2AccessTokenResponse(String idToken) {
        this.idToken = idToken;
        this.refreshToken = UUID.randomUUID().toString();
        this.accessToken = UUID.randomUUID().toString();
    }

    public String getIdToken() {
        return idToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

}
