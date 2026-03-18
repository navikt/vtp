package no.nav.foreldrepenger.vtp.server.auth.rest.texas;

public record TokenResponse(String access_token, int expires_in, String token_type) {

}
