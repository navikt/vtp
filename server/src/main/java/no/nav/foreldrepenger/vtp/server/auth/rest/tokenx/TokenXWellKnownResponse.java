package no.nav.foreldrepenger.vtp.server.auth.rest.tokenx;

import java.util.List;

public record TokenXWellKnownResponse(String issuer,
                                      String token_endpoint,
                                      String jwks_uri,
                                      List<String> grant_types_supported,
                                      List<String> token_endpoint_auth_methods_supported,
                                      List<String> token_endpoint_auth_signing_alg_values_supported,
                                      List<String> subject_types_supported) {

    public TokenXWellKnownResponse(String issuer, String tokenEndpoint, String jwksUri) {
        this(issuer, tokenEndpoint, jwksUri, List.of("urn:ietf:params:oauth:grant-type:token-exchange"), List.of("private_key_jwt"),
                List.of("RS256"), List.of("public"));
    }
}
