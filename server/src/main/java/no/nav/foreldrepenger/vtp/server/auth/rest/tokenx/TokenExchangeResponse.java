package no.nav.foreldrepenger.vtp.server.auth.rest.tokenx;

public record TokenExchangeResponse(String access_token,
                                    String issued_token_type,
                                    String token_type,
                                    int expires_in) {

    public TokenExchangeResponse(String access_token) {
        this(access_token, "urn:ietf:params:oauth:token-type:access_token", "Bearer", 299);
    }
}
