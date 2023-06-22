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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import no.nav.foreldrepenger.vtp.server.auth.rest.KeyStoreTool;
import no.nav.foreldrepenger.vtp.server.auth.rest.Oauth2AccessTokenResponse;

@Tag(name = "Forenklet AzureAd")
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
    @Operation(description = "Azure AD Discovery url")
    public Response wellKnown(@Context HttpServletRequest req) {
        LOG.info("Kall på well-known endepunkt");
        String baseUrl = getBaseUrl(req);
        var wellKnownResponse = new AADWellKnownResponse(ISSUER, baseUrl + "/authorize",
                baseUrl + "/jwks", baseUrl + "/token");
        return Response.ok(wellKnownResponse).build();
    }

    record AADWellKnownResponse(String issuer, String authorization_endpoint, String jwks_uri, String token_endpoint) { }

    @GET
    @Path("/jwks")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "azureAd/discovery/keys")
    public Response authorize() {
        String jwks = KeyStoreTool.getJwks();
        return Response.ok(jwks).build();
    }


    @POST
    @Path("/token")
    @Produces({MediaType.APPLICATION_JSON})
    @Operation(description = "azureAd/access_token")
    @SuppressWarnings("unused")
    public Response accessToken(@FormParam("grant_type") String grantType,
                                @FormParam("code") String code,
                                @FormParam("assertion") String assertion)  {
        String token;
        if ("client_credentials".equalsIgnoreCase(grantType)) {
            token = createClientCredentialsToken();
        } else if ("urn:ietf:params:oauth:grant-type:jwt-bearer".equalsIgnoreCase(grantType)) {
            Objects.requireNonNull(assertion);
            var claimsAssertion = AzureOidcTokenGenerator.getClaimsFromAssertion(assertion);
            var ansattId = Optional.ofNullable(claimsAssertion).map(AzureOidcTokenGenerator::getNavIdent).orElse(null);
            var bruker = Optional.ofNullable(ansattId).map(StandardBruker::finnIdent).orElseThrow();
            token = createToken(bruker);
        } else if ("authorization_code".equalsIgnoreCase(grantType)) {
            var ident = Optional.ofNullable(code).map(StandardBruker::finnIdent)
                    .orElseThrow(() -> new WebApplicationException("Bad code", Response.Status.BAD_REQUEST));
            token = createToken(ident);
        } else {
            LOG.warn("Ukjent / unsupported grant type {}", grantType);
            throw new WebApplicationException("Ukjent / unsupported grant type " + grantType, Response.Status.UNAUTHORIZED);
        }
        return Response.ok(new Oauth2AccessTokenResponse(token)).build();
    }

    @GET
    @Path("/bruker")
    @Produces({MediaType.APPLICATION_JSON})
    @Operation(description = "azureAd/access_token")
    public Response accessToken(@QueryParam("ident") @DefaultValue("saksbeh") String ident)  {
        var bruker = Optional.ofNullable(StandardBruker.finnIdent(ident)).orElseThrow();
        var token = createToken(bruker);
        return Response.ok(new Oauth2AccessTokenResponse(token)).build();
    }

    private String createToken(StandardBruker bruker) {
        return AzureOidcTokenGenerator.azureUserToken(bruker, ISSUER);
    }

    private String createClientCredentialsToken() {
        return AzureOidcTokenGenerator.azureClientCredentialsToken(UUID.randomUUID().toString().substring(0,19), ISSUER);
    }

    private static String getBaseUrl(HttpServletRequest req) {
        return req.getScheme() + "://vtp:" + req.getServerPort() + "/rest/aadfp";
    }

    @GET
    @Path("/authorize")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML})
    @Operation(description = "AzureAD/v2.0/authorize")
    @SuppressWarnings("unused")
    public Response authorize(@QueryParam("scope") @DefaultValue("openid") String scope,
                              @QueryParam("client_id") String clientId,
                              @QueryParam("redirect_uri") String redirectUri
    )
            throws Exception {
        LOG.info("kall mot AzureAD authorize");

        var uriBuilder = fromUri(redirectUri);
        addQueryParamToRequestIfNotNullOrEmpty(uriBuilder, "scope", scope);
        addQueryParamToRequestIfNotNullOrEmpty(uriBuilder, "client_id", clientId);
        addQueryParamToRequestIfNotNullOrEmpty(uriBuilder, "iss", ISSUER);
        addQueryParamToRequestIfNotNullOrEmpty(uriBuilder, "redirect_uri", redirectUri);

        return authorizeHtmlPage(uriBuilder);
    }

    public static void addQueryParamToRequestIfNotNullOrEmpty(UriBuilder uriBuilder, String name, String value) {
        Optional.ofNullable(value).filter(s -> !s.isBlank()).ifPresent(v -> uriBuilder.queryParam(name, v));
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
