package no.nav.foreldrepenger.vtp.server.auth.rest.isso;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

class OpenAMWellKnownResponse {


    @JsonProperty("response_types_supported")
    private List<String> responseTypesSupported = List.of(
            "code token id_token",
            "code",
            "code id_token",
            "id_token",
            "code token",
            "token",
            "token id_token"
    );

    @JsonProperty("claims_parameter_supported")
    private Boolean claimsParameterSupported = false;

    @JsonProperty("end_session_endpoint")
    private String endSessionEndpoint;

    @JsonProperty("version")
    private String version = "3.0";

    @JsonProperty("check_session_iframe")
    private String checkSessionIframe;


    @JsonProperty("scopes_supported")
    private List<String> scopesSupported = List.of("openid");

    @JsonProperty("issuer")
    private String issuer;

    @JsonProperty("id_token_encryption_enc_values_supported")
    private List<String> idTokenEncryptionEncValuesSupported = List.of(
            "A128CBC-HS256",
            "A256CBC-HS512"
    );

    @JsonProperty("acr_values_supported")
    private List<String> acrValuesSupported = List.of();

    @JsonProperty("authorization_endpoint")
    private String authorizationEndpoint;

    @JsonProperty("claims_supported")
    private List<String> claimsSupported = List.of();


    @JsonProperty("id_token_encryption_alg_values_supported")
    private List<String> idTokenEncryptionAlgValuesSupported = List.of(
            "RSA1_5"
    );

    @JsonProperty("jwks_uri")
    private String jwksUri;

    @JsonProperty("subject_types_supported")
    private List<String> subjectTypesSupported = List.of(
            "public"
    );

    @JsonProperty("id_token_signing_alg_values_supported")
    private List<String> idTokenSigningAlgValuesSupported = List.of(
            "ES384",
            "HS256",
            "HS512",
            "ES256",
            "RS256",
            "HS384",
            "ES512"
    );

    @JsonProperty("registration_endpoint")
    private String registrationEndpoint;


    @JsonProperty("token_endpoint_auth_methods_supported")
    private List<String> tokenEndpointAuthMethodsSupported = List.of(
            "private_key_jwt",
            "client_secret_basic"
    );

    @JsonProperty("token_endpoint")
    private String tokenEndpoint;

    public OpenAMWellKnownResponse(String url) {
        if (null != System.getenv("AUTHORIZE_BASE_URL")) {
            this.issuer = System.getenv("AUTHORIZE_BASE_URL") + "/rest/isso";
        } else {
            this.issuer = url + "/rest/isso/oauth2";
        }

        this.endSessionEndpoint = url + "/rest/isso/oauth2/connect/endSession";
        this.checkSessionIframe = url + "/rest/isso/oauth2/connect/checkSession";
        /**
         * Gjør det mulig å kjøre redirect flow i browseren når vi kjører med docker-compose.
         */
        if (null != System.getenv("AUTHORIZE_BASE_URL")) {
            this.authorizationEndpoint = System.getenv("AUTHORIZE_BASE_URL") + "/rest/isso/oauth2/authorize";
        } else {
            this.authorizationEndpoint = url + "/rest/isso/oauth2/authorize";
        }
        this.jwksUri = url + "/rest/isso/oauth2/connect/jwk_uri";
        this.registrationEndpoint = url + "/rest/isso/oauth2/connect/register";
        this.tokenEndpoint = url + "/rest/isso/oauth2/access_token";
    }

    public List<String> getResponseTypesSupported() {
        return responseTypesSupported;
    }

    public Boolean getClaimsParameterSupported() {
        return claimsParameterSupported;
    }

    public String getEndSessionEndpoint() {
        return endSessionEndpoint;
    }

    public String getVersion() {
        return version;
    }

    public String getCheckSessionIframe() {
        return checkSessionIframe;
    }

    public List<String> getScopesSupported() {
        return scopesSupported;
    }

    public String getIssuer() {
        return issuer;
    }

    public List<String> getIdTokenEncryptionEncValuesSupported() {
        return idTokenEncryptionEncValuesSupported;
    }

    public List<String> getAcrValuesSupported() {
        return acrValuesSupported;
    }

    public String getAuthorizationEndpoint() {
        return authorizationEndpoint;
    }

    public List<String> getClaimsSupported() {
        return claimsSupported;
    }

    public List<String> getIdTokenEncryptionAlgValuesSupported() {
        return idTokenEncryptionAlgValuesSupported;
    }

    public String getJwksUri() {
        return jwksUri;
    }

    public List<String> getSubjectTypesSupported() {
        return subjectTypesSupported;
    }

    public List<String> getIdTokenSigningAlgValuesSupported() {
        return idTokenSigningAlgValuesSupported;
    }

    public String getRegistrationEndpoint() {
        return registrationEndpoint;
    }

    public List<String> getTokenEndpointAuthMethodsSupported() {
        return tokenEndpointAuthMethodsSupported;
    }

    public String getTokenEndpoint() {
        return tokenEndpoint;
    }

}
