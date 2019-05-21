package no.nav.foreldrepenger.autotest.klienter.vtp.openam.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccessTokenResponseDTO {
    @JsonProperty("id_token")
    public String idToken;

    @JsonProperty("refresh_token")
    public String refreshToken;

    @JsonProperty("access_token")
    public String accessToken;

    @JsonProperty("expires_in")
    public int expiresIn = 3600;

    @JsonProperty("token_type")
    public String tokenType = "JWKS";
}
