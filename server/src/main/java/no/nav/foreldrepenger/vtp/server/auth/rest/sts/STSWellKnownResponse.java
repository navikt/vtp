package no.nav.foreldrepenger.vtp.server.auth.rest.sts;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

class STSWellKnownResponse {

    @JsonProperty("issuer")
    private String issuer;

    @JsonProperty("token_endpoint")
    private String tokenEndpoint;

    @JsonProperty("exchange_token_endpoint")
    private String exchangeTokenEndpoint;

    @JsonProperty("jwks_uri")
    private String jwksUri;

    @JsonProperty("subject_types_supported")
    private List<String> subjectTypesSupported = List.of("public");

    @JsonProperty("grant_types_supported")
    private List<String> grantTypesSupported = List.of("urn:ietf:params:oauth:grant-type:token-exchange", "client_credentials");

    @JsonProperty("scopes_supported")
    private List<String> scopesSupported = List.of("openid");

    @JsonProperty("token_endpoint_auth_methods_supported")
    private List<String> tokenEndpointAuthMethodsSupported = List.of("client_secret_basic");

    @JsonProperty("response_types_supported")
    private List<String> responseTypesSupported = List.of("id_token token");

    @JsonProperty("response_modes_supported")
    private List<String> responseModesSupported = List.of("form_post");

    @JsonProperty("id_token_signing_alg_values_supported")
    private List<String> idTokenSigningAlgValuesSupported = List.of("ES384", "HS256", "HS512", "ES256", "RS256", "HS384", "ES512");

    public STSWellKnownResponse(String issuer) {
        this.issuer = issuer;
    }

    public String getIssuer() {
        return issuer;
    }

    public String getTokenEndpoint() {
        return tokenEndpoint;
    }

    public String getJwksUri() {
        return jwksUri;
    }

    public List<String> getSubjectTypesSupported() {
        return subjectTypesSupported;
    }

    public List<String> getGrantTypesSupported() {
        return grantTypesSupported;
    }

    public List<String> getScopesSupported() {
        return scopesSupported;
    }

    public List<String> getTokenEndpointAuthMethodsSupported() {
        return tokenEndpointAuthMethodsSupported;
    }

    public List<String> getResponseTypesSupported() {
        return responseTypesSupported;
    }

    public List<String> getResponseModesSupported() {
        return responseModesSupported;
    }

    public List<String> getIdTokenSigningAlgValuesSupported() {
        return idTokenSigningAlgValuesSupported;
    }

    public String getExchangeTokenEndpoint() {
        return exchangeTokenEndpoint;
    }

    public void setTokenEndpoint(String tokenEndpoint) {
        this.tokenEndpoint = tokenEndpoint;
    }

    public void setExchangeTokenEndpoint(String exchangeTokenEndpoint) {
        this.exchangeTokenEndpoint = exchangeTokenEndpoint;
    }

    public void setJwksUri(String jwksUri) {
        this.jwksUri = jwksUri;
    }

}
