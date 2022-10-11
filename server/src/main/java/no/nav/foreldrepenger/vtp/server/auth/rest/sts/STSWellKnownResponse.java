package no.nav.foreldrepenger.vtp.server.auth.rest.sts;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

record STSWellKnownResponse(@JsonProperty("issuer") String issuer,
                            @JsonProperty("token_endpoint") String tokenEndpoint,
                            @JsonProperty("exchange_token_endpoint") String exchangeTokenEndpoint,
                            @JsonProperty("jwks_uri") String jwksUri,
                            @JsonProperty("subject_types_supported") List<String> subjectTypesSupported,
                            @JsonProperty("grant_types_supported") List<String> grantTypesSupported,
                            @JsonProperty("scopes_supported") List<String> scopesSupported,
                            @JsonProperty("token_endpoint_auth_methods_supported") List<String> tokenEndpointAuthMethodsSupported,
                            @JsonProperty("response_types_supported") List<String> responseTypesSupported,
                            @JsonProperty("response_modes_supported") List<String> responseModesSupported,
                            @JsonProperty("id_token_signing_alg_values_supported") List<String> idTokenSigningAlgValuesSupported) {
    STSWellKnownResponse {
        subjectTypesSupported = List.of("public");
        grantTypesSupported = List.of("urn:ietf:params:oauth:grant-type:token-exchange", "client_credentials");
        scopesSupported = List.of("openid");
        tokenEndpointAuthMethodsSupported = List.of("client_secret_basic");
        responseTypesSupported = List.of("id_token token");
        responseModesSupported = List.of("form_post");
        idTokenSigningAlgValuesSupported = List.of(
                "ES384",
                "HS256",
                "HS512",
                "ES256",
                "RS256",
                "HS384",
                "ES512");
    }

    public STSWellKnownResponse(String issuer) {
        this(issuer, issuer + "/token", issuer + "/token/exchange", issuer + "/jwks",
                null, null, null, null, null, null, null);
    }
}
