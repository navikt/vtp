package no.nav.foreldrepenger.vtp.server.auth.rest.foraad;

import static javax.ws.rs.core.Response.ok;
import static javax.ws.rs.core.UriBuilder.fromUri;
import static no.nav.foreldrepenger.vtp.server.auth.rest.Oauth2RequestParameterNames.CLIENT_ID;
import static no.nav.foreldrepenger.vtp.server.auth.rest.Oauth2RequestParameterNames.CODE;
import static no.nav.foreldrepenger.vtp.server.auth.rest.Oauth2RequestParameterNames.GRANT_TYPE;
import static no.nav.foreldrepenger.vtp.server.auth.rest.Oauth2RequestParameterNames.SCOPE;
import static no.nav.foreldrepenger.vtp.server.auth.rest.Oauth2RequestParameterNames.STATE;
import static no.nav.foreldrepenger.vtp.server.auth.rest.TokenClaims.NONCE;
import static no.nav.foreldrepenger.vtp.server.auth.rest.TokenClaims.azureOidcTokenClaims;
import static no.nav.foreldrepenger.vtp.server.auth.rest.TokenClaims.azureSystemTokenClaims;

import java.net.URI;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nimbusds.jwt.JWTClaimsSet;
import com.unboundid.util.NotNull;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.foreldrepenger.vtp.server.auth.rest.JsonWebKeyHelper;
import no.nav.foreldrepenger.vtp.server.auth.rest.Oauth2AccessTokenResponse;
import no.nav.foreldrepenger.vtp.server.auth.rest.Token;
import no.nav.foreldrepenger.vtp.testmodell.ansatt.AnsatteIndeks;
import no.nav.foreldrepenger.vtp.testmodell.ansatt.NAVAnsatt;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;

@ApplicationScoped
@Api(tags = {"AzureAd"})
@Path("/for/AzureAd")
public class AzureADForeldrepengerRestTjeneste {
    private static final Logger LOG = LoggerFactory.getLogger(AzureADForeldrepengerRestTjeneste.class);
    private static final AnsatteIndeks ansattIndeks = BasisdataProviderFileImpl.getInstance().getAnsatteIndeks();
    private static final Map<String, String> nonceCache = new ConcurrentHashMap<>();

    public static final String TENANT = "tenant";

    @GET
    @Path("/isAlive")
    @Produces(MediaType.TEXT_HTML)
    public Response isAliveMock() {
        return ok("Azure AD is OK").build();
    }

    @GET
    @Path("/{tenant}/v2.0/.well-known/openid-configuration")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Azure AD Discovery url", notes = ("Mock impl av Azure AD discovery urlen. "))
    public Response wellKnown(@Context HttpServletRequest req, @PathParam(TENANT) String tenant) {
        LOG.info("Kall på well-known endepunkt");
        String baseUrl = getBaseUrl(req);
        AzureADWellKnownResponse wellKnownResponse = new AzureADWellKnownResponse(baseUrl, tenant);
        return ok(wellKnownResponse).build();
    }

    @GET
    @Path("/{tenant}/discovery/v2.0/keys")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "azureAd/discovery/keys", notes = ("Mock impl av Azure AD jwk_uri"))
    public Response keys(@SuppressWarnings("unused") @Context HttpServletRequest req, @PathParam(TENANT) String tenant) {
        LOG.info("kall på /{}/discovery/v2.0/keys", tenant);
        var jwks = JsonWebKeyHelper.getJwks();
        return ok(jwks).build();
    }


    /**
     * Scope er på følgende format: api://<cluster>.<namespace>.<app-name>/.default
     * Audience vil da være client id til applikasjonen i gitt cluster, namespace og appname
     * Azure AD har en unik UUID. Eksempel: e89006c5-7193-4ca3-8e26-d0990d9d981f.
     * I test forenkler vi dette litt.
     *      Scope: api://localhost.<namespace>.<app-name>/.default
     *      ClientID: localhost.<namespace>.<app-name>
     * @param tenant required
     * @param scope med format api://localhost.<namespace>.<app-name>/.default, eks: api://localhost.teamforeldrepenger.fpsak/.default
     * @param clientId med format localhost.<namespace>.<app-name>, eks: localhost.teamforeldrepenger.fpsak
     * @param clientSecret The client secret that you generated for your app in the Azure portal (URL-encoded)
     * @param grantType støtter client_credentials, urn:ietf:params:oauth:grant-type:jwt-bearer og refresh_token
     * @param assertion The access token that was sent to the middle-tier API. This token must have an audience (aud) claim of the app making this OBO request (the app denoted by the client-id field)
     * @param requested_token_use Specifies how the request should be processed. In the OBO flow, the value must be set to on_behalf_of
     * @param refresh_token brukes i refresh_code flow
     * @param code brukes i en authorization code flow
     * @return {@link Oauth2AccessTokenResponse}
     * @throws ParseException
     */

    @POST
    @Path("/{tenant}/oauth2/v2.0/token")
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "azureAd/access_token", notes = ("Mock impl av Azure AD access_token"))
    public Response accessToken(@Context HttpServletRequest req,
                                @PathParam(TENANT) @Valid @NotNull String tenant,
                                @FormParam(SCOPE) String scope,
                                @FormParam(CLIENT_ID) String clientId,
                                @FormParam("client_secret") String clientSecret,
                                @FormParam(GRANT_TYPE) String grantType,
                                @FormParam("assertion") @Valid Token assertion,
                                @FormParam("requested_token_use") String requested_token_use,
                                @FormParam("refresh_token") @Valid Token refresh_token,
                                @FormParam(CODE) String code) throws ParseException {
        LOG.info("Kall på {} med følgende grant {}", req.getRequestURI(), grantType);
        return switch (grantType) {
            case "authorization_code" -> {
                if (isNullOrBlank(code)) {
                    yield badRequest("Authorization code flow mangler parameteren 'code' på requesten!");
                }
                var ansattID = ansattIDFra(code);
                var scopes = scope != null ? scope : scopesFra(code);
                var audience = hentAudienceFra(scopes);
                var navAnsatt = ansattIndeks.findByCn(ansattID);
                var claim = azureOidcTokenClaims(navAnsatt, audience, clientId, getIssuer(req, tenant), scope, tenant)
                        .claim(NONCE, nonceCache.get(clientId)).build();
                var token = Token.fra(claim);
                yield ok(new Oauth2AccessTokenResponse(token)).build();
            }
            case "client_credentials" -> {
                if (isNullOrBlank(scope)) {
                    yield badRequest("Client credentials kall mangler scope!");
                }
                var accessToken = Token.fra(azureSystemTokenClaims(hentAudienceFra(scope),
                        "im-just-a-fake-code", clientId, getIssuer(req, tenant), tenant)
                        .build());
                yield ok(new Oauth2AccessTokenResponse(accessToken)).build();
            }
            case "urn:ietf:params:oauth:grant-type:jwt-bearer" -> {
                if (isNullOrBlank(scope)) {
                    yield badRequest("on_behalf_of kall manger scope satt");
                }
                if (assertion == null || isNullOrBlank(assertion.value())) {
                    yield badRequest("Mangler assertion/subject token for on_behalf_of flow");
                }
                var claimsSubjectToken = assertion.parseToken();
                var ansatt = ansattIndeks.findByCn(ansattIdFraSubjectClaims(claimsSubjectToken));
                var idToken = genererTokenFraRequest(ansatt, List.of(clientId), getIssuer(req, tenant), tenant, scope, clientId);
                var accessToken = genererTokenFraRequest(ansatt, hentAudienceFra(scope), getIssuer(req, tenant), tenant, scope, clientId);
                LOG.info("Access_token utstedt for {}: {}", ansatt.displayName(), accessToken.value());
                yield ok(new Oauth2AccessTokenResponse(idToken, accessToken)).build();
            }
            case "refresh_token" -> {
                if (refresh_token == null || refresh_token.value() == null) {
                    yield badRequest("Forsøk på refresh av token uten refresh_token lagt ved i requesten");
                }
                var claimsSubjectToken = refresh_token.parseToken();
                var ansatt = ansattIndeks.findByCn(ansattIdFraSubjectClaims(claimsSubjectToken));
                var token = genererTokenFraRequest(ansatt, List.of(clientId), getIssuer(req, tenant), tenant, scope, clientId);
                LOG.info("Token refreshet for {}: {}", ansatt.displayName(), token.value());
                yield ok(new Oauth2AccessTokenResponse(token)).build();
            }
            default -> throw new IllegalArgumentException("grant_type av følgende type er ikke støttet: " + grantType);
        };
    }

    @GET
    @Path("/{tenant}/v2.0/authorize")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML})
    @ApiOperation(value = "AzureAD/v2.0/authorize", notes = ("Mock impl av Azure AD authorize"))
    @SuppressWarnings("unused")
    public Response authorize(@Context HttpServletRequest req,
                              @PathParam("tenant") @NotNull String tenant,
                              @QueryParam("response_type") @DefaultValue(CODE) String responseType,
                              @QueryParam("scope") @DefaultValue("openid") String scope, // openid, offline_access eller https://graph.microsoft.com/mail.read permissions
                              @QueryParam("client_id") @NotNull String clientId,
                              @QueryParam("state") String state,
                              @QueryParam("nonce") String nonce,
                              @QueryParam("redirect_uri") @NotNull String redirectUri) throws Exception {
        LOG.info("kall mot authorize med scope {} og redirect_uri {}", scope, redirectUri);
        if (!Objects.equals(responseType, CODE)) {
            throw new IllegalArgumentException("Unsupported responseType [" + responseType + "], should be 'code'");
        }

        var location = fromUri(redirectUri);
        if (!isNullOrBlank(state)) {
            location.queryParam(STATE, state);
        }
        if (!isNullOrBlank(nonce)) {
            nonceCache.put(clientId, nonce);
        }
        return authorizeHtmlPage(location, scope);
    }

    public static Response authorizeHtmlPage(UriBuilder location, String scope) {
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
                """, leggTilRaderITabellMedRedirectTilInnloggingAvSamtligeAnsatte(ansatte, location, scope));
        return Response.ok(htmlSideForInnlogging, MediaType.TEXT_HTML).build();
    }

    private static String leggTilRaderITabellMedRedirectTilInnloggingAvSamtligeAnsatte(Collection<NAVAnsatt> ansatte, UriBuilder location, String scope) {
        return ansatte.stream()
                .map(ansatt -> leggTilRadITabell(location.build(), ansatt, scope))
                .collect(Collectors.joining("\n"));
    }

    private static String leggTilRadITabell(URI location, NAVAnsatt ansatt, String scope) {
        var redirectForInnloggingAvAnsatt = fromUri(location)
                .queryParam(CODE, ansatt.cn() + ";" + scope)
                .build();
        return String.format("<tr><a href=\"%s\"><h1>%s</h1></a></tr>", redirectForInnloggingAvAnsatt, ansatt.displayName());
    }

    private static String ansattIdFraSubjectClaims(JWTClaimsSet claimsSubjectToken) {
        return claimsSubjectToken.getSubject().split(":")[1];
    }


    private static String scopesFra(String code) {
        return code.split(";")[1];
    }

    public static String ansattIDFra(String code) {
        if (code == null) {
            return null;
        }
        return code.split(";")[0];
    }

    // api://localhost.<namespace>.<app-name>/.default openid api://12312312.<namespace>.<app-name>/.default
    // localhost.<namespace>.<app-name> openid 12312312.<namespace>.<app-name>
    static List<String> hentAudienceFra(String scope) {
        return Arrays.stream(scope.split(" "))
                .map(s -> s.replaceFirst("api://", ""))
                .map(s -> s.replace("/.default", ""))
                .toList();
    }

    private Token genererTokenFraRequest(NAVAnsatt ansatt, List<String> audience, String issuer, String tenant, String scope, String clientId) {
        var claims = azureOidcTokenClaims(ansatt, audience, clientId, issuer, scope, tenant).build();
        return Token.fra(claims);
    }

    private static Response badRequest(String message) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(message)
                .build();
    }

    private static boolean isNullOrBlank(String value) {
        return value == null || value.isBlank();
    }

    public static String getBaseUrl(HttpServletRequest req) {
        return req.getScheme() + "://vtp:" + req.getServerPort() + "/rest/AzureAd";
    }

    public static String getIssuer(HttpServletRequest req, String tenant) {
        return getBaseUrl(req) + "/" + tenant + "/v2.0";
    }
}
