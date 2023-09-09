package no.nav.foreldrepenger.vtp.server.auth.rest;

public record WellKnownResponse(String issuer, String authorization_endpoint, String jwks_uri, String token_endpoint) {
}
