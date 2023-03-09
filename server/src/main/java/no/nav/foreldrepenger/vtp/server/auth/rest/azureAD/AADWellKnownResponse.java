package no.nav.foreldrepenger.vtp.server.auth.rest.azureAD;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

class AADWellKnownResponse {
    private final String baseUrl;
    private final String tenant;

    @JsonProperty("authorization_endpoint")
    public String getAuthorizationEndpoint() {
        return baseUrl + "/" + tenant + "/v2.0/authorize";
    }

    @JsonProperty("claims_supported")
    public final List<String> claimsSupported = List.of(
            "sub",
            "iss",
            "cloud_instance_name",
            "cloud_instance_host_name",
            "cloud_graph_host_name",
            "msgraph_host",
            "aud",
            "exp",
            "iat",
            "auth_time",
            "acr",
            "nonce",
            "preferred_username",
            "name",
            "tid",
            "ver",
            "at_hash",
            "c_hash",
            "email",
            "oid",
            "NAVident",
            "azp_name"
    );

    @JsonProperty("cloud_graph_host_name")
    public final String cloudGraphHostName = "graph.windows.net";

    @JsonProperty("cloud_instance_name")
    public final String cloudInstanceName = "microsoftonline.com";

    @JsonProperty("end_session_endpoint")
    public final String getEndSessionEndpoint() {
        return baseUrl + "/" + tenant + "/v2.0/logout";
    }

    @JsonProperty("frontchannel_logout_supported")
    public final boolean frontchannelLogoutSupported = true;

    @JsonProperty("http_logout_supported")
    public final boolean httpLogoutSupported = true;


    @JsonProperty("id_token_signing_alg_values_supported")
    public final List<String> idTokenSigningAlgValuesSupported = List.of("RS256");

    @JsonProperty("issuer")
    public final String getIssuer() {
        return baseUrl + "/" + tenant  + "/v2.0";
    }

    @JsonProperty("jwks_uri")
    public String getJwksUri() {
        return baseUrl + "/" + tenant + "/discovery/v2.0/keys";
    }

    @JsonProperty("msgraph_host")
    public final String msgraphHost = "graph.microsoft.com";

    @JsonProperty("rbac_url")
    public final String rbacUrl = "https://pas.windows.net";

    @JsonProperty("request_uri_parameter_supported")
    public final boolean request_uri_parameter_supported = false;

    @JsonProperty("response_modes_supported")
    public final List<String> responseModesSupported = List.of("query", "fragment", "form_post");

    @JsonProperty("response_types_supported")
    public final List<String> responseTypesSupported = List.of("code", "id_token", "code id_token", "id_token token");

    @JsonProperty("scopes_supported")
    public final List<String> scopesSupported = List.of("openid", "profile", "email", "offline_access");

    @JsonProperty("subject_types_supported")
    public final List<String> subjectTypesSupported = List.of("pairwise");

    @JsonProperty("tenant_region_scope")
    public final String tenantRegionScope = "EU";

    @JsonProperty("token_endpoint")
    public String getTokenEndpoint() {
        return baseUrl + "/" + tenant + "/oauth2/v2.0/token";
    }

    @JsonProperty("token_endpoint_auth_methods_supported")
    public final List<String> tokenEndpointAuthMethodsSupported = List.of("client_secret_post", "private_key_jwt", "client_secret_basic");

    @JsonProperty("userinfo_endpoint")
    public String getUserinfoEndpoint() {
        return baseUrl + "/rest/AzureGraphAPI/oidc/userinfo";
    }

    AADWellKnownResponse(String url, String tenant) {
        this.baseUrl = url;
        this.tenant = tenant;
    }
}
