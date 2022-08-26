package no.nav.foreldrepenger.vtp.server.auth.rest.azureAD;

import java.net.URISyntaxException;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.SearchResult;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.foreldrepenger.vtp.server.auth.rest.AzureOidcTokenGenerator;
import no.nav.foreldrepenger.vtp.server.auth.rest.KeyStoreTool;
import no.nav.foreldrepenger.vtp.server.auth.rest.Oauth2AccessTokenResponse;
import no.nav.foreldrepenger.vtp.server.auth.rest.UserRepository;

@Api(tags = {"AzureAd"})
@Path("/AzureAd")
public class AADRestTjeneste {
    private static final Logger LOG = LoggerFactory.getLogger(AADRestTjeneste.class);
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

        URIBuilder uriBuilder = new URIBuilder(redirectUri);
        uriBuilder.addParameter("scope", scope);
        uriBuilder.addParameter("state", state);
        uriBuilder.addParameter("client_id", clientId);
        final String issuer = getIssuer(req, tenant);
        uriBuilder.addParameter("iss", issuer);
        uriBuilder.addParameter("redirect_uri", redirectUri);

        clientIdCache.put(state, clientId);
        if (!StringUtils.isEmpty(req.getParameter("nonce"))) {
            nonceCache.put(state, req.getParameter("nonce"));
        }

        return authorizeHtmlPage(uriBuilder);
    }

    private Response authorizeHtmlPage(URIBuilder location) throws URISyntaxException, NamingException {
        // LAG HTML SIDE
        List<Map.Entry<String, String>> usernames = getUsernames();

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
        List<Map.Entry<String, String>> usernames = allUsers.stream()
                .map(u -> {
                    String cn = getAttribute(u, "cn");
                    String displayName = getAttribute(u, "displayName");
                    return new AbstractMap.SimpleEntry<String, String>(cn, displayName);
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

    private static String getBaseUrl(HttpServletRequest req) {
        return req.getScheme() + "://vtp:" + req.getServerPort() + "/rest/AzureAd";
    }

    private static String getIssuer(HttpServletRequest req, String tenant) {
        return getBaseUrl(req) + "/" + tenant  + "/v2.0";
    }
}
