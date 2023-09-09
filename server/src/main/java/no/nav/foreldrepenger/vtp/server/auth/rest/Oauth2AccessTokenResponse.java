package no.nav.foreldrepenger.vtp.server.auth.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Oauth2AccessTokenResponse(@JsonProperty("id_token") String idToken, @JsonProperty("refresh_token") String refreshToken,
                                        @JsonProperty("access_token") String accessToken,
                                        @JsonProperty("expires_in") Integer expiresIn, @JsonProperty("token_type") String tokenType) {
    public Oauth2AccessTokenResponse(String idToken) {
        this(idToken, idToken, idToken, 3600, "Bearer");
    }

}
