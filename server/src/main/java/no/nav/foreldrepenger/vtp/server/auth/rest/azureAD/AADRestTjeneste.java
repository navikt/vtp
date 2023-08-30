package no.nav.foreldrepenger.vtp.server.auth.rest.azureAD;

import static jakarta.ws.rs.core.UriBuilder.fromUri;
import static no.nav.foreldrepenger.vtp.server.auth.rest.isso.OpenAMRestService.CODE;

import java.net.URI;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import no.nav.foreldrepenger.vtp.server.auth.rest.AzureOidcTokenGenerator;
import no.nav.foreldrepenger.vtp.server.auth.rest.KeyStoreTool;
import no.nav.foreldrepenger.vtp.server.auth.rest.Oauth2AccessTokenResponse;
import no.nav.foreldrepenger.vtp.testmodell.ansatt.AnsatteIndeks;
import no.nav.foreldrepenger.vtp.testmodell.ansatt.NAVAnsatt;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;

@Tag(name = "AzureAd")
@Path("/AzureAd")
public class AADRestTjeneste {
    private static final Logger LOG = LoggerFactory.getLogger(AADRestTjeneste.class);
    private static final AnsatteIndeks ansattIndeks = BasisdataProviderFileImpl.getInstance().getAnsatteIndeks();
    private static final Map<String, String> nonceCache = new ConcurrentHashMap<>();
    private static final Map<String, String> clientIdCache = new ConcurrentHashMap<>();

    @GET
    @Path("/isAlive")
    @Produces(MediaType.TEXT_HTML)
    public Response isAliveMock() {
        String isAlive = "Azure AD is OK";
        return Response.ok(isAlive).build();
    }

    @GET
    @Path("/{tenant}/v2.0/.well-known/openid-configuration")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Azure AD Discovery url")
    public Response wellKnown(@SuppressWarnings("unused") @Context HttpServletRequest req, @PathParam("tenant") String tenant) {
        LOG.info("Kall på well-known endepunkt");
        String baseUrl = getBaseUrl(req);
        AADWellKnownResponse wellKnownResponse = new AADWellKnownResponse(baseUrl, tenant);
        return Response.ok(wellKnownResponse).build();
    }

    @GET
    @Path("/{tenant}/discovery/v2.0/keys")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "azureAd/discovery/keys")
    public Response authorize(@SuppressWarnings("unused") @Context HttpServletRequest req) {
        LOG.info("kall på /oauth2/connect/jwk_uri");
        String jwks = KeyStoreTool.getJwks();
        LOG.info("JWKS: {}", jwks);
        return Response.ok(jwks).build();
    }

    @POST
    @Path("/{tenant}/oauth2/v2.0/token")
    @Produces({MediaType.APPLICATION_JSON})
    @Operation(description = "azureAd/access_token")
    @SuppressWarnings("unused")
    public Response accessToken(
            @Context HttpServletRequest req,
            @PathParam("tenant") String tenant,
            @FormParam("grant_type") String grantType,
            @FormParam("realm") String realm,
            @FormParam("code") String code,
            @FormParam("redirect_uri") String redirectUri) {
        // dummy sikkerhet, returnerer alltid en idToken/refresh_token
        String token = createIdToken(req, code, tenant);
        LOG.info("Fikk parametere: {}", req.getParameterMap().toString());
        LOG.info("kall på /oauth2/access_token, opprettet token: {} med redirect-url: {}", token, redirectUri);
        Oauth2AccessTokenResponse oauthResponse = new Oauth2AccessTokenResponse(token);
        return Response.ok(oauthResponse).build();
    }

    static String createIdToken(HttpServletRequest req, String username, String tenant) {
        String issuer = getIssuer(req, tenant);
        String state = req.getParameter("state");
        String nonce = state != null ? nonceCache.get(state) : null;
        AzureOidcTokenGenerator tokenGenerator = new AzureOidcTokenGenerator(username, nonce).withIssuer(issuer);
        if (state != null && clientIdCache.containsKey(state)) {
            String clientId = clientIdCache.get(state);
            tokenGenerator.addAud(clientId);
        }
        return tokenGenerator.create();
    }

    @GET
    @Path("/{tenant}/v2.0/authorize")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML})
    @Operation(description = "AzureAD/v2.0/authorize")
    @SuppressWarnings("unused")
    public Response authorize(
            @Context HttpServletRequest req,
            @Context HttpServletResponse resp,
            @PathParam("tenant") String tenant,
            @QueryParam("response_type") @DefaultValue("code") String responseType,
            @QueryParam("scope") @DefaultValue("openid") String scope,
            @QueryParam("client_id") String clientId,
            @QueryParam("state") String state,
            @QueryParam("redirect_uri") String redirectUri
    )
            throws Exception {
        LOG.info("kall mot AzureAD authorize med redirecturi {}", redirectUri);
        Objects.requireNonNull(scope, "Missing the ?scope=xxx query parameter");
        if (!Objects.equals(scope, "openid")) {
            throw new IllegalArgumentException("Unsupported scope [" + scope + "], should be 'openid'");
        }
        Objects.requireNonNull(responseType, "Missing the ?responseType=xxx query parameter");
        if (!Objects.equals(responseType, "code")) {
            throw new IllegalArgumentException("Unsupported responseType [" + responseType + "], should be 'code'");
        }

        Objects.requireNonNull(clientId, "Missing the ?client_id=xxx query parameter");
        Objects.requireNonNull(state, "Missing the ?state=xxx query parameter");
        Objects.requireNonNull(redirectUri, "Missing the ?redirect_uri=xxx query parameter");

        var uriBuilder = fromUri(redirectUri);
        addQueryParamToRequestIfNotNullOrEmpty(uriBuilder, "scope", scope);
        addQueryParamToRequestIfNotNullOrEmpty(uriBuilder, "state", state);
        addQueryParamToRequestIfNotNullOrEmpty(uriBuilder, "client_id", clientId);
        final String issuer = getIssuer(req, tenant);
        addQueryParamToRequestIfNotNullOrEmpty(uriBuilder, "iss", issuer);
        addQueryParamToRequestIfNotNullOrEmpty(uriBuilder, "redirect_uri", redirectUri);

        clientIdCache.put(state, clientId);
        if (!StringUtils.isEmpty(req.getParameter("nonce"))) {
            nonceCache.put(state, req.getParameter("nonce"));
        }

        return authorizeHtmlPage(uriBuilder);
    }

    public static void addQueryParamToRequestIfNotNullOrEmpty(UriBuilder uriBuilder, String name, String value) {
        if (value != null && !value.isBlank()) {
            uriBuilder.queryParam(name, value);
        }
    }

    public static Response authorizeHtmlPage(UriBuilder location) {
        var ansatte = ansattIndeks.alleAnsatte();
        var htmlSideForInnlogging = String.format("""
                     <!DOCTYPE html>
                     <html>
                     <head>
                     <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
                     <title>Velg bruker</title>
                     </head>
                         <body>
                         <div style="text-align:center;width:100%%;">
                             <caption><h3>Velg bruker:</h3></caption>
                             <table>
                                 <tbody>
                                     %s
                                 </tbody>
                             </table>
                         </div>
                     </body>
                     </html>
                 """, leggTilRaderITabellMedRedirectTilInnloggingAvSamtligeAnsatte(ansatte, location));
        return Response.ok(htmlSideForInnlogging, MediaType.TEXT_HTML).build();
    }

    private static String leggTilRaderITabellMedRedirectTilInnloggingAvSamtligeAnsatte(Collection<NAVAnsatt> ansatte, UriBuilder location) {
        return ansatte.stream()
                .map(ansatt -> leggTilRadITabell(location.build(), ansatt))
                .collect(Collectors.joining("\n"));
    }

    private static String leggTilRadITabell(URI location, NAVAnsatt ansatt) {
        var redirectForInnloggingAvAnsatt = fromUri(location)
                .queryParam(CODE, ansatt.cn())
                .build();
        return String.format("<tr><a href=\"%s\"><h1>%s</h1></a></tr>", redirectForInnloggingAvAnsatt, ansatt.displayName());
    }

    private static String getBaseUrl(HttpServletRequest req) {
        return req.getScheme() + "://vtp:" + req.getServerPort() + "/rest/AzureAd";
    }

    private static String getIssuer(HttpServletRequest req, String tenant) {
        return getBaseUrl(req) + "/" + tenant  + "/v2.0";
    }
}
