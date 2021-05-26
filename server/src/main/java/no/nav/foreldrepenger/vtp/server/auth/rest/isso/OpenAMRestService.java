package no.nav.foreldrepenger.vtp.server.auth.rest.isso;

import java.net.URISyntaxException;
import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.SearchResult;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import no.nav.foreldrepenger.vtp.server.auth.rest.KeyStoreTool;
import no.nav.foreldrepenger.vtp.server.auth.rest.Oauth2AccessTokenResponse;
import no.nav.foreldrepenger.vtp.server.auth.rest.OidcTokenGenerator;
import no.nav.foreldrepenger.vtp.server.auth.rest.UserRepository;


@Api(tags = {"Openam"})
@Path("/isso")
public class OpenAMRestService {

    private static final Logger LOG = LoggerFactory.getLogger(OpenAMRestService.class);

    private static final Map<String, String> nonceCache = new ConcurrentHashMap<>();
    private static final Map<String, String> clientIdCache = new ConcurrentHashMap<>();

    private static final String DEFAULT_ISSUER = "https://vtp.openam/issuer";

    public static final String STATE = "state";
    public static final String CODE = "code";
    public static final String REDIRECT_URI = "redirect_uri";
    public static final String SCOPE = "scope";
    public static final String CLIENT_ID = "client_id";
    public static final String ISSUER_PARAM = "iss";
    public static final String NONCE = "nonce";

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
            @QueryParam("response_type") @DefaultValue(CODE) String responseType,
            @QueryParam(SCOPE) @DefaultValue("openid") String scope,
            @QueryParam(CLIENT_ID) String clientId,
            @QueryParam(STATE) String state,
            @QueryParam(REDIRECT_URI) String redirectUri)
            throws Exception {
        LOG.info("kall mot oauth2/authorize med redirecturi {}", redirectUri);
        Objects.requireNonNull(scope, SCOPE);
        if (!Objects.equals(scope, "openid")) {
            throw new IllegalArgumentException("Unsupported scope [" + scope + "], should be 'openid'");
        }
        Objects.requireNonNull(responseType, "responseType");
        if (!Objects.equals(responseType, CODE)) {
            throw new IllegalArgumentException("Unsupported responseType [" + responseType + "], should be 'code'");
        }

        Objects.requireNonNull(clientId, CLIENT_ID);
        Objects.requireNonNull(state, STATE);
        Objects.requireNonNull(redirectUri, "redirectUri");

        URIBuilder uriBuilder = new URIBuilder(redirectUri);
        uriBuilder.addParameter(SCOPE, scope);
        uriBuilder.addParameter(STATE, state);
        uriBuilder.addParameter(CLIENT_ID, clientId);
        uriBuilder.addParameter(ISSUER_PARAM, getIssuer());
        uriBuilder.addParameter(REDIRECT_URI, redirectUri);
        clientIdCache.put(state, clientId);
        if (req.getParameter(NONCE) != null && !req.getParameter(NONCE).isEmpty()) {
            nonceCache.put(state, req.getParameter(NONCE));
        }

        String acceptHeader = req.getHeader("Accept-Header");
        if ((null == req.getContentType() || req.getContentType().equals("text/html")) && (acceptHeader == null || !acceptHeader.contains("json"))) {
            return authorizeHtmlPage(uriBuilder);
        } else {
            return authorizeRedirect(uriBuilder);
        }
    }

    private Response authorizeRedirect(URIBuilder location) throws URISyntaxException {
        // SEND JSON RESPONSE TIL OPENAM HELPER
        location.addParameter(CODE, "im-just-a-fake-code");
        return Response.status(HttpServletResponse.SC_FOUND).location(location.build()).build();
    }

    private Response authorizeHtmlPage(URIBuilder location) throws URISyntaxException, NamingException {
        // LAG HTML SIDE
        List<Entry<String, String>> usernames = getUsernames();

        String html = "<!DOCTYPE html>\n"
                + "<html>\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
                "<title>Velg bruker</title>\n" +
                "</head>\n" +
                "    <body>\n" +
                "    <div style=\"text-align:center;width:100%;\">\n" +
                "       <caption><h3>Velg bruker:</h3></caption>\n" +
                "        <table>\r\n" +
                "            <tbody>\r\n" +
                usernames.stream().map(
                        username -> "<tr><a href=\"" + location.toString() + "&code=" + username.getKey() + "\"><h1>" + username.getValue() + "</h1></a></tr>\n")
                        .collect(Collectors.joining("\n"))
                +
                "            </tbody>\n" +
                "        </table>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";

        return Response.ok(html, MediaType.TEXT_HTML).build();
    }

    private List<Map.Entry<String, String>> getUsernames() throws NamingException {
        List<SearchResult> allUsers = UserRepository.getAllUsers();

        // Long story, økonomi forventer (per 2018-10-30) at alle interne brukere har max 8 bokstaver i bruker identen sin :-(
        // pass derfor på at CN er definert med maks 8 bokstaver.

        List<Map.Entry<String, String>> usernames = allUsers.stream()
                .map(u -> {
                    String cn = getAttribute(u, "cn");
                    String displayName = getAttribute(u, "displayName");
                    return new SimpleEntry<String, String>(cn, displayName);
                }).collect(Collectors.toList());
        return usernames;
    }

    private String getAttribute(SearchResult u, String attribName) {
        Attribute attribute = u.getAttributes().get(attribName);
        try {
            return (String) attribute.get();
        } catch (NamingException e) {
            throw new IllegalStateException(e);
        }
    }


    // TODO: Brukes av FP til å logge inn som saksbehandler i saksbehandlerløsningen
    @POST
    @Path("/oauth2/access_token")
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "oauth2/access_token", notes = ("Mock impl av Oauth2 access_token"))
    @SuppressWarnings("unused")
    public Response accessToken(
            @Context HttpServletRequest req,
            @FormParam("grant_type") String grantType,
            @FormParam("realm") String realm,
            @FormParam(CODE) String code,
            @FormParam(REDIRECT_URI) String redirectUri,
            @FormParam(STATE) String state) {
        // dummy sikkerhet, returnerer alltid en idToken/refresh_token
        String token = createIdToken(req, code, state);
        LOG.info("Fikk parametere: {}", req.getParameterMap().toString());
        LOG.info("kall på /oauth2/access_token, opprettet token: {} med redirect-url: {}", token, redirectUri);
        Oauth2AccessTokenResponse oauthResponse = new Oauth2AccessTokenResponse(token);
        return Response.ok(oauthResponse).build();
    }

    private String getIssuer() {
        if (null != System.getenv("ISSO_OAUTH2_ISSUER")) {
            return System.getenv("ISSO_OAUTH2_ISSUER");
        } else {
            return DEFAULT_ISSUER;
        }
    }

    private String createIdToken(HttpServletRequest req, String username, String state) {
        String issuer = getIssuer();
        if (state == null) {
            LOG.warn("State ikke funnet i form post!");
            state = req.getParameter(STATE);
        }
        String nonce = null;
        if (state != null) {
            nonce = nonceCache.get(state);
        }
        OidcTokenGenerator tokenGenerator = new OidcTokenGenerator(username, nonce).withIssuer(issuer);
        if (state != null && clientIdCache.containsKey(state)) {
            String clientId = clientIdCache.get(state);
            tokenGenerator.addAud(clientId);
        }
        return tokenGenerator.create();
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
        LOG.info("kall på /oauth2/connect/jwk_uri");
        String jwks = KeyStoreTool.getJwks();
        LOG.info("JWKS: " + jwks);
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
            EndUserAuthenticateTemplate template = new EndUserAuthenticateTemplate();
            template.setAuthId(UUID.randomUUID().toString());
            template.setHeader("Sign in to VTP");
            template.setStage("DataStore1");
            template.setTemplate("");

            EndUserAuthenticateTemplate.Name namePrompt = new EndUserAuthenticateTemplate.Name("prompt", "User Name:");
            EndUserAuthenticateTemplate.Name usernameInput = new EndUserAuthenticateTemplate.Name("IDToken1", "");
            EndUserAuthenticateTemplate.Callback nameCallback = new EndUserAuthenticateTemplate.Callback("NameCallback", namePrompt, usernameInput);

            EndUserAuthenticateTemplate.Name passwordPrompt = new EndUserAuthenticateTemplate.Name("prompt", "Password:");
            EndUserAuthenticateTemplate.Name passwordInput = new EndUserAuthenticateTemplate.Name("IDToken2", "");
            EndUserAuthenticateTemplate.Callback passwordCallback = new EndUserAuthenticateTemplate.Callback("PasswordCallback", passwordPrompt, passwordInput);

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
        String baseUrl = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort();
        OpenAMWellKnownResponse wellKnownResponse = new OpenAMWellKnownResponse(baseUrl, getIssuer());
        return Response.ok(wellKnownResponse).build();
    }

}
