package no.nav.foreldrepenger.vtp.server.auth.rest;

import java.util.List;

public record WellKnownResponse(String issuer,
                                String authorization_endpoint,
                                String jwks_uri,
                                String token_endpoint,
                                List<String> id_token_signing_alg_values_supported) {

    public WellKnownResponse(String issuer, String authorization_endpoint, String jwks_uri, String token_endpoint) {
        this(issuer, authorization_endpoint, jwks_uri, token_endpoint, List.of("RS256"));
    }
}
