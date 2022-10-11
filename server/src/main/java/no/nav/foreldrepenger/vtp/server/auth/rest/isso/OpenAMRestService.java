package no.nav.foreldrepenger.vtp.server.auth.rest.isso;

import static com.nimbusds.jwt.JWTClaimNames.ISSUER;
import static no.nav.foreldrepenger.vtp.server.auth.rest.Oauth2RequestParameterNames.CLIENT_ID;
import static no.nav.foreldrepenger.vtp.server.auth.rest.Oauth2RequestParameterNames.CODE;
import static no.nav.foreldrepenger.vtp.server.auth.rest.Oauth2RequestParameterNames.GRANT_TYPE;
import static no.nav.foreldrepenger.vtp.server.auth.rest.Oauth2RequestParameterNames.REDIRECT_URI;
import static no.nav.foreldrepenger.vtp.server.auth.rest.Oauth2RequestParameterNames.SCOPE;
import static no.nav.foreldrepenger.vtp.server.auth.rest.Oauth2RequestParameterNames.STATE;
import static no.nav.foreldrepenger.vtp.server.auth.rest.TokenClaims.NONCE;
import static no.nav.foreldrepenger.vtp.server.auth.rest.azureAD.AzureADRestTjeneste.ansattIDFra;
import static no.nav.foreldrepenger.vtp.server.auth.rest.azureAD.AzureADRestTjeneste.authorizeHtmlPage;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import no.nav.foreldrepenger.vtp.server.auth.rest.JsonWebKeyHelper;
import no.nav.foreldrepenger.vtp.server.auth.rest.Oauth2AccessTokenResponse;
import no.nav.foreldrepenger.vtp.server.auth.rest.Token;
import no.nav.foreldrepenger.vtp.server.auth.rest.TokenClaims;

@Deprecated
@ApplicationScoped
@Api(tags = {"Openam"})
@Path("/isso")
public class OpenAMRestService {
    private static final Logger LOG = LoggerFactory.getLogger(OpenAMRestService.class);
    private static final Map<String, String> nonceCache = new ConcurrentHashMap<>();
    private static final Map<String, String> clientIdCache = new ConcurrentHashMap<>();

    @GET
    @Path("/oauth2/authorize")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML})
    @ApiOperation(value = "oauth2/authorize", notes = ("Mock impl av Oauth2 authorize"))
    @SuppressWarnings("unused")
    public Response authorize(
            @Context HttpServletRequest req,
            @Context HttpServletResponse resp,
            @QueryParam("session") @DefaultValue("winssochain") String session,
            @QueryParam("authIndexType") @DefaultValue("service") String authIndexType,
            @QueryParam("authIndexValue") @DefaultValue("winssochain") String authIndexValue,
            @QueryParam("response_type") @DefaultValue(CODE) @NotNull String responseType,
            @QueryParam(SCOPE) @DefaultValue("openid") @NotNull String scope,
            @QueryParam(CLIENT_ID) @NotNull String clientId,
            @QueryParam(STATE) @NotNull String state,
            @QueryParam(REDIRECT_URI) @NotNull String redirectUri)
            throws Exception {
        LOG.info("kall mot oauth2/authorize med redirecturi {}", redirectUri);
        if (!Objects.equals(scope, "openid")) {
            throw new IllegalArgumentException("Unsupported scope [" + scope + "], should be 'openid'");
        }
        if (!Objects.equals(responseType, CODE)) {
            throw new IllegalArgumentException("Unsupported responseType [" + responseType + "], should be 'code'");
        }

        var redirectTo = UriBuilder.fromUri(redirectUri)
                .queryParam(STATE, state)
                .queryParam(CLIENT_ID, clientId)
                .queryParam(ISSUER, getIssuer(req))
                .queryParam(REDIRECT_URI, redirectUri);
        clientIdCache.put(state, clientId);
        if (req.getParameter(NONCE) != null && !req.getParameter(NONCE).isEmpty()) {
            nonceCache.put(state, req.getParameter(NONCE));
        }

        if (erAuthorizationEndepunktKaltFraBrowser(req)) {
            return authorizeHtmlPage(redirectTo, scope);
        } else {
            return authorizeRedirect(redirectTo);
        }
    }


    private boolean erAuthorizationEndepunktKaltFraBrowser(HttpServletRequest req) {
        var acceptHeader = req.getHeader("Accept-Header");
        var contentType = req.getContentType();
        return (null == contentType || contentType.equals(MediaType.TEXT_HTML)) && (acceptHeader == null || !acceptHeader.contains("json"));
    }

    private Response authorizeRedirect(UriBuilder location) {
        // SEND JSON RESPONSE TIL OPENAM HELPER
        location.queryParam(CODE, "im-just-a-fake-code");
        return Response.status(HttpServletResponse.SC_FOUND).location(location.build()).build();
    }

    @POST
    @Path("/oauth2/access_token")
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "oauth2/access_token", notes = ("Mock impl av Oauth2 access_token"))
    public Response accessToken(@Context HttpServletRequest req,
            @FormParam(GRANT_TYPE) String grantType,
            @FormParam("realm") String realm,
            @FormParam(CODE) String code,
            @FormParam(STATE) String state,
            @FormParam(REDIRECT_URI) String redirectUri) {
        var token = createIdToken(req, ansattIDFra(code), state);
        LOG.info("kall på /oauth2/access_token, opprettet token: {} med redirect-url: {}", token.value(), redirectUri);
        Oauth2AccessTokenResponse oauthResponse = new Oauth2AccessTokenResponse(token);
        return Response.ok(oauthResponse).build();
    }

    private Token createIdToken(HttpServletRequest req, String username, String state) {
        var issuer = getIssuer(req);
        if (state == null) {
            LOG.warn("State ikke funnet i form post!");
            state = req.getParameter(STATE);
        }
        String nonce = null;
        if (state != null) {
            nonce = nonceCache.get(state);
        }
        var claims = TokenClaims.openAmTokenClaims(username, issuer, nonce);
        if (state != null && clientIdCache.containsKey(state)) {
            var clientId = clientIdCache.get(state);
            claims.audience(clientId);
        }
        return Token.fra(claims.build());
    }

    @GET
    @Path("/isAlive.jsp")
    @Produces(MediaType.TEXT_HTML)
    public Response isAliveMock() {
        String isAlive = "Server is ALIVE";
        return Response.ok(isAlive).build();
    }

    @GET
    @Path("/oauth2/../isAlive.jsp")
    @Produces(MediaType.TEXT_HTML)
    public Response isAliveMockRassUrl() {
        String isAlive = "Server is ALIVE";
        return Response.ok(isAlive).build();
    }

    @GET
    @Path("/oauth2/connect/jwk_uri")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "oauth2/connect/jwk_uri", notes = ("Mock impl av Oauth2 jwk_uri"))
    public Response authorize(@SuppressWarnings("unused") @Context HttpServletRequest req) {
        var jwks = JsonWebKeyHelper.getJwks();
        LOG.info("kall på /oauth2/connect/jwk_uri. JWKS returnert: {}", jwks);
        return Response.ok(jwks).build();
    }

    /**
     * brukes til autentisere bruker slik at en slipper å autentisere senere. OpenAM mikk-makk .
     */
    @POST
    @Path("/json/authenticate")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "json/authenticate", notes = ("Mock impl av OpenAM autenticate for service bruker innlogging"))
    public Response serviceBrukerAuthenticate(@SuppressWarnings("unused") @Context HttpServletRequest req,
                                              @ApiParam("Liste over aksjonspunkt som skal bekreftes, inklusiv data som trengs for å løse de.") EndUserAuthenticateTemplate enduserTemplate) {
        LOG.info("kall på /json/authenticate");
        if (enduserTemplate == null) {
            var template = new EndUserAuthenticateTemplate();
            template.setAuthId(UUID.randomUUID().toString());
            template.setHeader("Sign in to VTP");
            template.setStage("DataStore1");
            template.setTemplate("");

            var namePrompt = new EndUserAuthenticateTemplate.Name("prompt", "User Name:");
            var usernameInput = new EndUserAuthenticateTemplate.Name("IDToken1", "");
            var nameCallback = new EndUserAuthenticateTemplate.Callback("NameCallback", namePrompt, usernameInput);

            var passwordPrompt = new EndUserAuthenticateTemplate.Name("prompt", "Password:");
            var passwordInput = new EndUserAuthenticateTemplate.Name("IDToken2", "");
            var passwordCallback = new EndUserAuthenticateTemplate.Callback("PasswordCallback", passwordPrompt, passwordInput);

            template.setCallbacks(Arrays.asList(nameCallback, passwordCallback));

            return Response.ok(template).build();
        } else {
            // generer token som brukes til å bekrefte innlogging ovenfor openam

            // TODO ingen validering av authId?
            // TODO generer unik session token?

            EndUserAuthenticateSuccess success = new EndUserAuthenticateSuccess("i-am-just-a-dummy-session-token-workaround", "/isso/console");
            return Response.ok(success).build();
        }

    }

    @GET
    @Path("/oauth2/.well-known/openid-configuration")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Discovery url", notes = ("Mock impl av discovery urlen. "))
    public Response wellKnown(@SuppressWarnings("unused") @Context HttpServletRequest req) {
        LOG.info("kall på /oauth2/.well-known/openid-configuration");
        String baseUrl = getBaseUrl(req);
        OpenAMWellKnownResponse wellKnownResponse = new OpenAMWellKnownResponse(baseUrl);
        return Response.ok(wellKnownResponse).build();
    }

    private String getBaseUrl(HttpServletRequest req) {
        return req.getScheme() + "://vtp:" + req.getServerPort();
    }

    private String getIssuer(HttpServletRequest req) {
        return getBaseUrl(req) + "/rest/isso/oauth2";
    }

}
