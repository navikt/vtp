package no.nav.foreldrepenger.vtp.server.auth.rest.foraad;

import static javax.ws.rs.core.UriBuilder.fromUri;
import static no.nav.foreldrepenger.vtp.server.auth.rest.isso.OpenAMRestService.CODE;

import java.net.URI;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.apache.commons.lang3.StringUtils;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.foreldrepenger.vtp.server.auth.rest.KeyStoreTool;
import no.nav.foreldrepenger.vtp.server.auth.rest.Oauth2AccessTokenResponse;
import no.nav.foreldrepenger.vtp.testmodell.ansatt.AnsatteIndeks;
import no.nav.foreldrepenger.vtp.testmodell.ansatt.NAVAnsatt;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;

@Api(tags = {"AzureAd"})
@Path("/for/AzureAd")
public class AADRestTjeneste {
    private static final Logger LOG = LoggerFactory.getLogger(AADRestTjeneste.class);
    private static final AnsatteIndeks ANSATTE_INDEKS = BasisdataProviderFileImpl.getInstance().getAnsatteIndeks();
    private static final Map<String, String> NONCE_CACHE = new ConcurrentHashMap<>();
    private static final Map<String, String> CLIENT_ID_CACHE = new ConcurrentHashMap<>();
    private static final JwtConsumer UNVALIDATING_CONSUMER = new JwtConsumerBuilder()
            .setSkipAllValidators()
            .setDisableRequireSignature()
            .setSkipSignatureVerification()
            .build();

    @GET
    @Path("/isAlive")
    @Produces(MediaType.TEXT_HTML)
    public Response isAliveMock() {
        String isAlive = "Azure AD is OK";
        return Response.ok(isAlive).build();
    }

    @GET
    @Path("/{tenant}/v2.0/authorize")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML})
    @ApiOperation(value = "AzureAD/v2.0/authorize", notes = ("Mock impl av Azure AD authorize"))
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

        CLIENT_ID_CACHE.put(state, clientId);
        if (!StringUtils.isEmpty(req.getParameter("nonce"))) {
            NONCE_CACHE.put(state, req.getParameter("nonce"));
        }

        return authorizeHtmlPage(uriBuilder);
    }

    @GET
    @Path("/{tenant}/v2.0/.well-known/openid-configuration")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Azure AD Discovery url", notes = ("Mock impl av Azure AD discovery urlen. "))
    public Response wellKnown(@SuppressWarnings("unused") @Context HttpServletRequest req, @PathParam("tenant") String tenant) {
        LOG.info("Kall på well-known endepunkt");
        String baseUrl = getBaseUrl(req);
        AADWellKnownResponse wellKnownResponse = new AADWellKnownResponse(baseUrl, tenant);
        return Response.ok(wellKnownResponse).build();
    }

    @GET
    @Path("/{tenant}/discovery/v2.0/keys")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "azureAd/discovery/keys", notes = ("Mock impl av Azure AD jwk_uri"))
    public Response authorize(@SuppressWarnings("unused") @Context HttpServletRequest req) {
        LOG.info("kall på /oauth2/connect/jwk_uri");
        String jwks = KeyStoreTool.getJwks();
        LOG.info("JWKS: {}", jwks);
        return Response.ok(jwks).build();
    }


    @POST
    @Path("/{tenant}/oauth2/v2.0/token")
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "azureAd/access_token", notes = ("Mock impl av Azure AD access_token"))
    @SuppressWarnings("unused")
    public Response accessToken(
            @Context HttpServletRequest req,
            @PathParam("tenant") String tenant,
            @FormParam("grant_type") String grantType,
            @FormParam("client_id") String clientId,
            @FormParam("scope") String scope,
            @FormParam("refresh_token") String refreshToken,
            @FormParam("realm") String realm,
            @FormParam("code") String code,
            @FormParam("assertion") String assertion,
            @FormParam("redirect_uri") String redirectUri)  {
        // dummy sikkerhet, returnerer alltid en idToken/refresh_token
        var claimsIn = Optional.ofNullable(req.getHeader(HttpHeaders.AUTHORIZATION))
                .filter(auth -> auth.toLowerCase().startsWith("bearer "))
                .map(a -> a.substring("Bearer ".length()))
                .map(this::getClaims).orElse(null);
        var clientIdIn = Optional.ofNullable(req.getHeader(HttpHeaders.AUTHORIZATION))
                .map(a -> getUser(req.getHeader("Authorization"), claimsIn)).orElse(null);
        Objects.requireNonNull(scope);
        String token;
        if ("client_credentials".equalsIgnoreCase(grantType)) {
            var issuer = getIssuer(req, tenant);
            var aud = audFromScope(scope);
            token = createClientCredentialsToken(List.of(aud), tenant, clientId, scope, issuer);
            return Response.ok(new Oauth2AccessTokenResponse(token)).build();
        } else if ("urn:ietf:params:oauth:grant-type:jwt-bearer".equalsIgnoreCase(grantType)) {
            Objects.requireNonNull(assertion);
            var claimsAssertion = getClaims(assertion);
            var ansattId = getNavIdent(claimsAssertion);
            if (!harRiktigAudience(claimsAssertion, clientId)) {
                throw new WebApplicationException("Feil audience", Response.Status.BAD_REQUEST);
            }
            var issuer = getIssuer(req, tenant);
            var aud = audFromScope(scope);
            var state = req.getParameter("state");
            token = createToken(List.of(aud), ansattId, tenant, clientId, null, scope, null, issuer);
        } else if ("authorization_code".equalsIgnoreCase(grantType)) {
            if (code == null) {
                throw new WebApplicationException("No code", Response.Status.BAD_REQUEST);
            }
            var codeSplit = code.split(";");  // Er dette en pen-konvensjon
            var ansattId = codeSplit[0];
            var state = req.getParameter("state");
            var nonce = state != null ? NONCE_CACHE.get(state) : null; // codeSplit.length > 1 ? codeSplit[1] :  null;
            var aud = (state != null && CLIENT_ID_CACHE.containsKey(state)) ? CLIENT_ID_CACHE.get(state) : audFromScope(scope);
            var issuer = getIssuer(req, tenant);
            token = createToken(List.of(aud), ansattId, tenant, clientId, nonce, scope, code, issuer);
        } else if ("refresh_token".equalsIgnoreCase(grantType)) {
            Objects.requireNonNull(refreshToken);
            var claimsRefresh = getClaims(assertion);
            var ansattId = getNavIdent(claimsRefresh);
            var issuer = getIssuer(req, tenant);
            var aud = audFromScope(scope);
            var state = req.getParameter("state");
            token = createToken(List.of(aud), ansattId, tenant, clientId, null, scope, null, issuer);
        } else {
            LOG.warn("Ukjent grant type {}", grantType);
            throw new WebApplicationException("Ukjent grant type " + grantType, Response.Status.UNAUTHORIZED);
        }
        LOG.info("Fikk parametere: {}", req.getParameterMap().toString());
        LOG.info("kall på /oauth2/access_token, opprettet token: {} med redirect-url: {}", token, redirectUri);
        Oauth2AccessTokenResponse oauthResponse = new Oauth2AccessTokenResponse(token);
        return Response.ok(oauthResponse).build();
    }

    private String createToken(List<String> audience, String ansattId, String tenant, String clientId, String nonce, String scope, String sid, String requestedIssuer) {
        var user = Optional.ofNullable(ANSATTE_INDEKS.findByCn(ansattId))
                .orElseThrow(() ->  new RuntimeException("Fant ikke NAV-ansatt med brukernavn " + ansattId));
        var additionalClaims = Map.of(
                "tid", tenant,
                "NAVident", ansattId,
                "azp_name", "vtp:teamforeldrepenger:" + clientId,
                "azp", clientId,
                "scp", scope,
                "name", user.displayName(),
                "preferred_username", user.cn());

        return AzureOidcTokenGenerator.azureUserToken(audience, ansattId, requestedIssuer, additionalClaims, nonce, sid);
    }

    private String createClientCredentialsToken(List<String> audience,  String tenant, String clientId, String scope, String requestedIssuer) {
        var subject = UUID.randomUUID().toString().substring(0,19);
        var additionalClaims = Map.of(
                "tid", tenant,
                "azp_name", "vtp:teamforeldrepenger:" + clientId,
                "azp", clientId,
                "scp", scope);

        return AzureOidcTokenGenerator.azureClientCredentialsToken(audience, subject, requestedIssuer, additionalClaims);
    }



    private static String audFromScope(String scope) {
        return scope.replace('.',':').split(":")[3].replace('/', ' ').trim();
    }


    public static void addQueryParamToRequestIfNotNullOrEmpty(UriBuilder uriBuilder, String name, String value) {
        if (value != null && !value.isBlank()) {
            uriBuilder.queryParam(name, value);
        }
    }

    public static Response authorizeHtmlPage(UriBuilder location) {
        var ansatte = ANSATTE_INDEKS.alleAnsatte();
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
        return req.getScheme() + "://vtp:" + req.getServerPort() + "/rest/for/AzureAd";
    }

    private static String getIssuer(HttpServletRequest req, String tenant) {
        return getBaseUrl(req) + "/" + tenant  + "/v2.0";
    }

    private JwtClaims getClaims(String auth) {
        if (Optional.ofNullable(auth).filter(a -> a.toLowerCase().startsWith("bearer ")).isPresent()) {
            try {
                var assertion = auth.substring("Bearer ".length());
                return UNVALIDATING_CONSUMER.processToClaims(assertion);
            } catch (Exception e) {
                throw new WebApplicationException("Bad mock access token; must be on format Bearer access:<userid>", Response.Status.FORBIDDEN);
            }
        }
        return null;
    }

    private String getUser(String auth, JwtClaims claims)  {
        if (Optional.ofNullable(auth).filter(a -> a.toLowerCase().startsWith("basic")).isPresent()) {
            return new String(Base64.getDecoder().decode(auth.substring("Basic".length()).trim())).split(":")[0];
        } else if (Optional.ofNullable(auth).filter(a -> a.toLowerCase().startsWith("bearer")).isPresent()) {
            try {
                return claims != null ? claims.getStringClaimValue("NAVident") : null;
            } catch (MalformedClaimException e) {
                return null;
            }
        } else {
            return null;
        }
    }

    private String getNavIdent(JwtClaims claims)  {
        try {
            return claims.getStringClaimValue("NAVident");
        } catch (MalformedClaimException e) {
            return null;
        }

    }

    private boolean harRiktigAudience(JwtClaims claims, String clientId)  {
        try {
            return claims.getAudience().contains(clientId);
        } catch (MalformedClaimException e) {
            return false;
        }

    }
}
