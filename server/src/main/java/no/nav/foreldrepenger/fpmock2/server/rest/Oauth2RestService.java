package no.nav.foreldrepenger.fpmock2.server.rest;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapName;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import no.nav.foreldrepenger.fpmock2.server.rest.EndUserAuthenticateTemplate.Callback;

@Api(tags = { "Openam" })
@Path("/isso")
public class Oauth2RestService {

    private static final Logger LOG = LoggerFactory.getLogger(Oauth2RestService.class);

    @GET
    @Path("/oauth2/authorize")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_HTML })
    @ApiOperation(value = "oauth2/authorize", notes = ("Mock impl av Oauth2 authorize"))
    @SuppressWarnings("unused")
    public Response authorize(
                              @Context HttpServletRequest req,
                              @Context HttpServletResponse resp,
                              @QueryParam("session") @DefaultValue("winssochain") String session,
                              @QueryParam("authIndexType") @DefaultValue("service") String authIndexType,
                              @QueryParam("authIndexValue") @DefaultValue("winssochain") String authIndexValue,
                              @QueryParam("response_type") @DefaultValue("code") String responseType,
                              @QueryParam("scope") @DefaultValue("openid") String scope,
                              @QueryParam("client_id") String clientId,
                              @QueryParam("state") String state,
                              @QueryParam("redirect_uri") String redirectUri)
            throws Exception {

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
        query.put("scope", scope);
        query.put("state", state);
        query.put("client_id", clientId);
        query.put("iss", new URI(req.getScheme(), req.getServerName(), "/isso/oauth2", null).toASCIIString());

        query.put("redirect_uri", redirectUri);

        String acceptHeader = req.getHeader("Accept-Header");
        if ((null == req.getContentType() || req.getContentType().equals("text/html")) && (acceptHeader == null || !acceptHeader.contains("json"))) {
            return authorizeHtmlPage(locationUri, query);
        } else {
            return authorizeRedirect(locationUri, query);
        }
    }

    private Response authorizeRedirect(URI locationUri, Map<String, String> query) throws URISyntaxException {
        // SEND JSON RESPONSE TIL OPENAM HELPER
        query.put("code", "im-just-a-fake-code");

        URI location = new URI(locationUri.getScheme(), null, locationUri.getHost(), locationUri.getPort(), locationUri.getPath(), formatQueryParams(query),
            null);
        return Response.status(HttpServletResponse.SC_FOUND).location(location).build();
    }

    private Response authorizeHtmlPage(URI locationUri, Map<String, String> query) throws URISyntaxException, NamingException {
        // LAG HTML SIDE
        URI location = new URI(locationUri.getScheme(), null, locationUri.getHost(), locationUri.getPort(), locationUri.getPath(), formatQueryParams(query),
            null);

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
        List<SearchResult> allUsers = getAllUsers();

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

    private List<SearchResult> getAllUsers() throws NamingException {
        Hashtable<String, String> props = new Hashtable<>();
        props.put(javax.naming.Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        props.put(javax.naming.Context.SECURITY_AUTHENTICATION, "none");
        props.put(javax.naming.Context.PROVIDER_URL, "ldaps://localhost:8636/");

        InitialLdapContext ctx = new InitialLdapContext(props, null);
        LdapName base = new LdapName("ou=NAV,ou=BusinessUnits,dc=test,dc=local");

        SearchControls controls = new SearchControls();
        controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        controls.setCountLimit(50);

        NamingEnumeration<SearchResult> result = ctx.search(base, "cn=*", controls);

        List<SearchResult> usernames = new ArrayList<>();

        while (result.hasMore()) {
            usernames.add(result.next());
        }
        return usernames;
    }

    // TODO (FC): Trengs denne fortsatt?
    @POST
    @Path("/oauth2/access_token")
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "oauth2/access_token", notes = ("Mock impl av Oauth2 access_token"))
    @SuppressWarnings("unused")
    public Response accessToken(
                                @Context HttpServletRequest req,
                                @FormParam("grant_type") String grantType,
                                @FormParam("realm") String realm,
                                @FormParam("code") String code,
                                @FormParam("redirect_uri") String redirectUri) {
        // dummy sikkerhet, returnerer alltid en idToken/refresh_token
        String token = createIdToken(req, code);
        Oauth2AccessTokenResponse oauthResponse = new Oauth2AccessTokenResponse(token, UUID.randomUUID().toString());
        return Response.ok(oauthResponse).build();
    }

    private String createIdToken(HttpServletRequest req, String username) {
        String issuer;
        if (null != System.getenv("AUTOTEST_OAUTH2_ISSUER_SCHEME")) {
            issuer = System.getenv("AUTOTEST_OAUTH2_ISSUER_SCHEME") + "://"
                + System.getenv("AUTOTEST_OAUTH2_ISSUER_URL") + ":"
                + System.getenv("AUTOTEST_OAUTH2_ISSUER_PORT")
                + System.getenv("AUTOTEST_OAUTH2_ISSUER_PATH");
            LOG.info("Setter issuer-url fra naisconfig: " + issuer);
        } else {
            issuer = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/isso/oauth2";
            LOG.info("Setter issuer-url fra implisit localhost: " + issuer);
        }
        String token = new OidcTokenGenerator(username).withIssuer(issuer).create();
        return token;
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
        String jwks = KeyStoreTool.getJwks();
        return Response.ok(jwks).build();
    }

    /** brukes til autentisere bruker slik at en slipper å autentisere senere. OpenAM mikk-makk . */
    @POST
    @Path("/json/authenticate")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "json/authenticate", notes = ("Mock impl av OpenAM autenticate for service bruker innlogging"))
    public Response serviceBrukerAuthenticate(@SuppressWarnings("unused") @Context HttpServletRequest req,
                                              @ApiParam("Liste over aksjonspunkt som skal bekreftes, inklusiv data som trengs for å løse de.") EndUserAuthenticateTemplate enduserTemplate) {

        if (enduserTemplate == null) {
            EndUserAuthenticateTemplate template = new EndUserAuthenticateTemplate();
            template.setAuthId(UUID.randomUUID().toString());
            template.setHeader("Sign in to VTP");
            template.setStage("DataStore1");
            template.setTemplate("");

            EndUserAuthenticateTemplate.Name namePrompt = new EndUserAuthenticateTemplate.Name("prompt", "User Name:");
            EndUserAuthenticateTemplate.Name usernameInput = new EndUserAuthenticateTemplate.Name("IDToken1", "");
            Callback nameCallback = new EndUserAuthenticateTemplate.Callback("NameCallback", namePrompt, usernameInput);

            EndUserAuthenticateTemplate.Name passwordPrompt = new EndUserAuthenticateTemplate.Name("prompt", "Password:");
            EndUserAuthenticateTemplate.Name passwordInput = new EndUserAuthenticateTemplate.Name("IDToken2", "");
            Callback passwordCallback = new EndUserAuthenticateTemplate.Callback("PasswordCallback", passwordPrompt, passwordInput);

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
