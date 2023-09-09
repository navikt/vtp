package no.nav.foreldrepenger.vtp.server.auth.rest.tokenx;

public record TokenExchangeResponse(String access_token, String issued_token_type, String token_type, int expires_in) {

    public static final int EXPIRE_IN_SECONDS = 3600;

    public TokenExchangeResponse(String accessToken) {
        this(accessToken, "urn:ietf:params:oauth:token-type:access_token", "Bearer", EXPIRE_IN_SECONDS);
    }
}
