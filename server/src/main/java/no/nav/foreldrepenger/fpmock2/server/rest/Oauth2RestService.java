package no.nav.foreldrepenger.fpmock2.server.rest;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = { "oauth2" })
@Path("/isso/oauth2")
public class Oauth2RestService {

    @GET
    @Path("/authorize")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "authorize", notes = ("Mock impl av Oauth2 authorize"))
    public Response authorize(
                              @SuppressWarnings("unused") @Context HttpServletRequest req,
                              @SuppressWarnings("unused") @QueryParam("session") @DefaultValue("winssochain") String session,
                              @SuppressWarnings("unused") @QueryParam("authIndexType") @DefaultValue("service") String authIndexType,
                              @SuppressWarnings("unused") @QueryParam("authIndexValue") @DefaultValue("winssochain") String authIndexValue,
                              @QueryParam("response_type") @DefaultValue("code") String responseType,
                              @QueryParam("scope") @DefaultValue("openid") String scope,
                              @QueryParam("client_id") String clientId,
                              @QueryParam("state") String state,
                              @QueryParam("redirect_uri") String redirectUri)
            throws URISyntaxException {

        Objects.requireNonNull(scope, "scope");
        if (!Objects.equals(scope, "openid")) {
            throw new IllegalArgumentException("Unsupported scope [" + scope + "], should be 'openid'");
        }
        Objects.requireNonNull(responseType, "responseType");
        if (!Objects.equals(responseType, "code")) {
            throw new IllegalArgumentException("Unsupported responseType [" + responseType + "], should be 'code'");
        }

        Objects.requireNonNull(clientId, "client_id");
        Objects.requireNonNull(state, "state");
        Objects.requireNonNull(redirectUri, "redirectUri");

        URI locationUri = new URI(redirectUri);

        Map<String, String> query = new LinkedHashMap<>();
        query.put("code", "im-just-a-fake-code");
        query.put("scope", scope);
        query.put("state", state);
        query.put("client_id", clientId);
        query.put("iss", new URI(req.getScheme(), req.getServerName(), "/isso/oauth2", null).toASCIIString());

        query.put("redirect_uri", redirectUri);

        URI location = new URI(locationUri.getScheme(), null, locationUri.getHost(), locationUri.getPort(), locationUri.getPath(), formatQueryParams(query),
            null);
        return Response.status(HttpServletResponse.SC_FOUND).location(location).build();

        // back-channel:
        // https://isso-t.adeo.no/isso/oauth2/connect/jwk_uri
        // {"keys":[{"kty":"RSA","kid":"SH1IeRSk1OUFH3swZ+EuUq19TvQ=","use":"sig","alg":"RS256","n":"AM2uHZfbHbDfkCTG8GaZO2zOBDmL4sQgNzCSFqlQ-ikAwTV5ptyAHYC3JEy_LtMcRSv3E7r0yCW_7WtzT-CgBYQilb_lz1JmED3TgiThEolN2kaciY06UGycSj8wEYik-3PxuVeKr3uw6LVEohM3rrCjdlkQ_jctuvuUrCedbsb2hVw6Q17PQbWURq8v3gtXmGMD8KcR7e0dtf0ZoMOfZQoFJZ-a5dMFzXeP8Ffz_c0uBLSddd-FqOhzVDiMbvFI9XKE22TWghYanPpPsGGZYioQbJfu5VtphR6zNjiUp9O4lA_qEkbBpRA8SaUTCz3PcirFYDg0zvV8p2hgY9jyCj0","e":"AQAB"}]}

    }

    @POST
    @Path("/access_token")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "access_token", notes = ("Mock impl av Oauth2 access_token"))
    public Response accessToken(
                              @SuppressWarnings("unused") @Context HttpServletRequest req,
                              @SuppressWarnings("unused") @QueryParam("grant_type") String grantType,
                              @SuppressWarnings("unused") @QueryParam("realm") String realm,
                              @SuppressWarnings("unused") @QueryParam("code") String code,
                              @SuppressWarnings("unused") @QueryParam("redirect_uri") String redirectUri) {
        // dummy sikkerhet, returnerer alltid en idToken/refresh_token
        String issuer = req.getScheme() +"://"+ req.getServerName() + ":"+req.getServerPort() +"/isso/oauth2";
        String token = new OidcTokenGenerator().withIssuer(issuer).create();
        Oauth2AccessTokenResponse oauthResponse = new Oauth2AccessTokenResponse(token, UUID.randomUUID().toString());
        return Response.ok(oauthResponse).build();
    }
    
    @GET
    @Path("/connect/jwk_uri")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "connect/jwk_uri", notes = ("Mock impl av Oauth2 jwk_uri"))
    public Response authorize( @SuppressWarnings("unused") @Context HttpServletRequest req) {
        String jwks = KeyStoreTool.getJwks();
        return Response.ok(jwks).build();
    }


    protected String formatQueryParams(Map<String, String> params) {
        return params.entrySet().stream()
            .map(p -> urlEncodeUTF8(p.getKey()) + "=" + urlEncodeUTF8(p.getValue()))
            .reduce((p1, p2) -> p1 + "&" + p2)
            .orElse("");
    }

    private static String urlEncodeUTF8(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedOperationException(e);
        }
    }

}