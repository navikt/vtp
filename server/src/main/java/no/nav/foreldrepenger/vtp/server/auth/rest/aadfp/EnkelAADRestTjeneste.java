package no.nav.foreldrepenger.vtp.server.auth.rest.aadfp;

import static javax.ws.rs.core.UriBuilder.fromUri;
import static no.nav.foreldrepenger.vtp.server.auth.rest.isso.OpenAMRestService.CODE;

import java.net.URI;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.foreldrepenger.vtp.server.auth.rest.KeyStoreTool;
import no.nav.foreldrepenger.vtp.server.auth.rest.Oauth2AccessTokenResponse;

@Api(tags = {"Forenklet AzureAd"})
@Path("/aadfp")
public class EnkelAADRestTjeneste {
    private static final Logger LOG = LoggerFactory.getLogger(EnkelAADRestTjeneste.class);
    private static final String ISSUER = "http://vtp/rest/aadfp";

    @GET
    @Path("/isAlive")
    @Produces(MediaType.TEXT_HTML)
    public Response isAliveMock() {
        String isAlive = "Azure AD is OK";
        return Response.ok(isAlive).build();
    }

    @GET
    @Path("/.well-known/openid-configuration")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Azure AD Discovery url", notes = ("Mock impl av Azure AD discovery urlen. "))
    public Response wellKnown(@Context HttpServletRequest req) {
        LOG.info("Kall på well-known endepunkt");
        String baseUrl = getBaseUrl(req);
        var wellKnownResponse = new AADWellKnownResponse(getIssuer(), baseUrl + "/authorize",
                baseUrl + "/keys", baseUrl + "/token");
        return Response.ok(wellKnownResponse).build();
    }

    record AADWellKnownResponse(String issuer, String authorization_endpoint, String jwks_uri, String token_endpoint) { }

    @GET
    @Path("/keys")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "azureAd/discovery/keys", notes = ("Mock impl av Azure AD jwk_uri"))
    public Response authorize() {
        LOG.info("kall på /oauth2/connect/jwk_uri");
        String jwks = KeyStoreTool.getJwks();
        LOG.info("JWKS: {}", jwks);
        return Response.ok(jwks).build();
    }


    @POST
    @Path("/token")
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "azureAd/access_token", notes = ("Mock impl av Azure AD access_token"))
    @SuppressWarnings("unused")
    public Response accessToken(@FormParam("grant_type") String grantType,
                                @FormParam("code") String code,
                                @FormParam("assertion") String assertion)  {
        String token;
        if ("client_credentials".equalsIgnoreCase(grantType)) {
            var issuer = getIssuer();
            token = createClientCredentialsToken(issuer);
        } else if ("urn:ietf:params:oauth:grant-type:jwt-bearer".equalsIgnoreCase(grantType)) {
            Objects.requireNonNull(assertion);
            var claimsAssertion = AzureOidcTokenGenerator.getClaimsFromAssertion(assertion);
            var ansattId = Optional.ofNullable(claimsAssertion).map(AzureOidcTokenGenerator::getNavIdent).orElse(null);
            var bruker = Optional.ofNullable(ansattId).map(StandardBruker::finnIdent).orElseThrow();
            var issuer = getIssuer();
            token = createToken(bruker, issuer);
        } else if ("authorization_code".equalsIgnoreCase(grantType)) {
            var ident = Optional.ofNullable(code).map(StandardBruker::finnIdent)
                    .orElseThrow(() -> new WebApplicationException("Bad code", Response.Status.BAD_REQUEST));
            var issuer = getIssuer();
            token = createToken(ident, issuer);
        } else {
            LOG.warn("Ukjent / unsupported grant type {}", grantType);
            throw new WebApplicationException("Ukjent / unsupported grant type " + grantType, Response.Status.UNAUTHORIZED);
        }
        return Response.ok(new Oauth2AccessTokenResponse(token)).build();
    }

    @GET
    @Path("/bruker")
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "azureAd/access_token", notes = ("Mock impl av Azure AD access_token"))
    public Response accessToken(@QueryParam("ident") @DefaultValue("saksbeh") String ident)  {
        var bruker = Optional.ofNullable(StandardBruker.finnIdent(ident)).orElseThrow();
        var issuer = getIssuer();
        var token = createToken(bruker, issuer);
        return Response.ok(new Oauth2AccessTokenResponse(token)).build();
    }

    private String createToken(StandardBruker bruker, String requestedIssuer) {
        return AzureOidcTokenGenerator.azureUserToken(bruker, requestedIssuer);
    }

    private String createClientCredentialsToken(String requestedIssuer) {
        return AzureOidcTokenGenerator.azureClientCredentialsToken(UUID.randomUUID().toString().substring(0,19), requestedIssuer);
    }

    private static String getBaseUrl(HttpServletRequest req) {
        return req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/rest/aadfp";
    }

    private static String getIssuer() {
        return ISSUER;
    }

    @GET
    @Path("/authorize")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML})
    @ApiOperation(value = "AzureAD/v2.0/authorize", notes = ("Mock impl av Azure AD authorize"))
    @SuppressWarnings("unused")
    public Response authorize(@QueryParam("scope") @DefaultValue("openid") String scope,
                              @QueryParam("client_id") String clientId,
                              @QueryParam("redirect_uri") String redirectUri
    )
            throws Exception {
        LOG.info("kall mot AzureAD authorize med redirecturi {}", redirectUri);

        var uriBuilder = fromUri(redirectUri);
        addQueryParamToRequestIfNotNullOrEmpty(uriBuilder, "scope", scope);
        addQueryParamToRequestIfNotNullOrEmpty(uriBuilder, "client_id", clientId);
        final String issuer = getIssuer();
        addQueryParamToRequestIfNotNullOrEmpty(uriBuilder, "iss", issuer);
        addQueryParamToRequestIfNotNullOrEmpty(uriBuilder, "redirect_uri", redirectUri);

        return authorizeHtmlPage(uriBuilder);
    }

    public static void addQueryParamToRequestIfNotNullOrEmpty(UriBuilder uriBuilder, String name, String value) {
        if (value != null && !value.isBlank()) {
            uriBuilder.queryParam(name, value);
        }
    }

    public static Response authorizeHtmlPage(UriBuilder location) {
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
                 """, leggTilRaderITabellMedRedirectTilInnloggingAvSamtligeAnsatte(location));
        return Response.ok(htmlSideForInnlogging, MediaType.TEXT_HTML).build();
    }

    private static String leggTilRaderITabellMedRedirectTilInnloggingAvSamtligeAnsatte(UriBuilder location) {
        return Arrays.stream(StandardBruker.values())
                .map(ansatt -> leggTilRadITabell(location.build(), ansatt))
                .collect(Collectors.joining("\n"));
    }

    private static String leggTilRadITabell(URI location, StandardBruker ansatt) {
        var redirectForInnloggingAvAnsatt = fromUri(location)
                .queryParam(CODE, ansatt.getIdent())
                .build();
        return String.format("<tr><a href=\"%s\"><h1>%s</h1></a></tr>", redirectForInnloggingAvAnsatt, ansatt.getNavn());
    }

}
