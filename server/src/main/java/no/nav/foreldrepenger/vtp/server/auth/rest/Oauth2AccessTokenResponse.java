package no.nav.foreldrepenger.vtp.server.auth.rest;

import java.util.Optional;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Oauth2AccessTokenResponse(@JsonProperty("id_token") @Valid Token idToken,
                                        @JsonProperty("refresh_token") @Valid Token refreshToken,
                                        @JsonProperty("access_token") @Valid Token accessToken,
                                        @JsonProperty("expires_in") Integer expiresIn,
                                        @JsonProperty("token_type")  String tokenType) {
    public Oauth2AccessTokenResponse {
        expiresIn = Optional.ofNullable(expiresIn).orElse(accessToken.expiresIn());
        tokenType = Optional.ofNullable(tokenType).orElse("Bearer");
    }

    public Oauth2AccessTokenResponse(Token token) {
        this(token, token, token, token.expiresIn(), null);
    }

    public Oauth2AccessTokenResponse(Token idToken, Token accessToken) {
        this(idToken, idToken, accessToken, accessToken.expiresIn(), null);
    }

}
