package no.nav.foreldrepenger.vtp.server.auth.rest.idporten;

import static jakarta.ws.rs.core.UriBuilder.fromUri;
import static no.nav.foreldrepenger.vtp.server.auth.rest.Oauth2RequestParameterNames.NONCE;
import static no.nav.foreldrepenger.vtp.server.auth.rest.Oauth2RequestParameterNames.STATE;
import static no.nav.foreldrepenger.vtp.server.auth.rest.azuread.AzureAdRestTjeneste.isNotNullAndBlank;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import no.nav.foreldrepenger.vtp.server.MockServer;
import no.nav.foreldrepenger.vtp.server.auth.rest.JsonWebKeyHelper;
import no.nav.foreldrepenger.vtp.server.auth.rest.Oauth2AccessTokenResponse;

@Tag(name = "ID-Porten")
@Path(IdportenLoginTjeneste.TJENESTE_PATH)
public class IdportenLoginTjeneste {
    private static final Logger LOG = LoggerFactory.getLogger(IdportenLoginTjeneste.class);
    protected static final String TJENESTE_PATH = "/idporten"; //NOSONAR
    private static final String ISSUER = "http://vtp:8060/rest/idporten";

    private static final Map<String, String> nonceCache = new ConcurrentHashMap<>();

    @GET
    @Path("/.well-known/openid-configuration")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Idporten Discovery url")
    public Response wellKnown(@SuppressWarnings("unused") @Context HttpServletRequest req) {
        LOG.info("Kall på well-known endepunkt");
        String baseUrl = getBaseUrl(req);
        var wellKnownResponse = new WellKnownResponse(
                ISSUER,
                baseUrl + "/authorize",
                baseUrl + "/jwks",
                baseUrl + "/token",
                List.of("idporten-loa-high"),
                List.of("nb")
                );
        return Response.ok(wellKnownResponse).build();
    }

    @GET
    @Path("/isAlive")
    @Produces(MediaType.TEXT_HTML)
    public Response isAliveMock() {
        String isAlive = "idporten AD is OK";
        return Response.ok(isAlive).build();
    }

    @GET
    @Path("/jwks")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "idporten/discovery/keys")
    public Response authorize() {
        String jwks = JsonWebKeyHelper.getJwks();
        return Response.ok(jwks).build();
    }

    @GET
    @Path("/authorize")
    @Produces(MediaType.TEXT_HTML)
    public Response autentiserMedRedirectTilInnloggingViaHTML(@QueryParam(NONCE) String nonce, @QueryParam(STATE) String state, @QueryParam("redirect_uri") URI redirectUri) {
        var location = fromUri(redirectUri);

        if (isNotNullAndBlank(state)) {
            location.queryParam(STATE, state);
        }
        if (isNotNullAndBlank(nonce)) {
            // Token som produseres ved innloging må ha riktig nonce verdi siden Wonderwall validerer verdien.
            nonceCache.put(NONCE, nonce);
            location.queryParam(NONCE, nonce);
        }

        location.build();
        String html = """
                    <!DOCTYPE html>
                    <html>
                    <head>
                    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
                    <title>Velg bruker</title>
                    </head>
                        <body>
                        <div style="text-align:center;width:100%%;">
                           <caption><h3>Fødselsnummer:</h3></caption>
                            <form action="%s" method="get">
                              <input type="hidden" name="state" value="%s" />
                              <input type="hidden" name="nounce" value="%s" />
                              <input type="text" name="code" />
                              <input type="submit" value="Token, takk!" />
                            </form>
                        </div>
                    </body>
                    </html>
                """;

        return Response.ok(String.format(html, location, state, nonce), MediaType.TEXT_HTML).build();
    }

    @POST
    @Path("/access_token")
    @Produces({MediaType.APPLICATION_JSON})
    @Operation(description = "idporten/access_token")
    @SuppressWarnings("unused")
    public Response tokenEndpoint(@Context HttpServletRequest req,
                                @FormParam("code") String code,
                                @FormParam("redirect_uri") URI redirectUri) {

        var nonce = nonceCache.get(NONCE);
        var token = IdportenOidcTokenGenerator.idportenUserToken(code, ISSUER, nonce);
        LOG.info("Kall på idporten /token endepunkt nytt token {}", token);
        return Response.ok(new Oauth2AccessTokenResponse(token)).build();
    }




    // SPESIAL INNLOGGING
    @GET
    @Path("/login")
    @Produces(MediaType.TEXT_HTML)
    public Response hent(@QueryParam("redirect") URI redirectUri) {
        String tmpl = """
                    <!DOCTYPE html>
                    <html>
                    <head>
                    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
                    <title>Velg bruker</title>
                    </head>
                        <body>
                        <div style="text-align:center;width:100%%;">
                           <caption><h3>Fødselsnummer:</h3></caption>
                            <form action="/rest/idporten/token" method="post">
                              <input type="hidden" name="redirect" value="%s" />
                              <input type="text" name="fnr" />
                              <input type="submit" value="Token, takk!" />
                            </form>
                        </div>
                    </body>
                    </html>
                """;
        return Response.ok(String.format(tmpl, redirectUri), MediaType.TEXT_HTML).build();
    }

    @POST
    @Path("/token")
    @Produces({MediaType.APPLICATION_JSON})
    @Operation(description = "idporten/access_token")
    @SuppressWarnings("unused")
    public Response seeOtherSetCookie(@Context HttpServletRequest req,
                                    @FormParam("fnr") String fnr,
                                    @FormParam("redirect") URI redirectUri) {
        var token = IdportenOidcTokenGenerator.idportenUserToken(fnr, ISSUER, null);
        var cookieTemplate = "selvbetjening-idtoken=%s;Path=/";
        return Response.seeOther(redirectUri).header("Set-Cookie", String.format(cookieTemplate, token)).build();
    }

    @GET
    @Path("/bruker")
    @Produces({MediaType.APPLICATION_JSON})
    @Operation(description = "idporten/access_token - brukes primært av autotest til å logge inn en bruker programmatisk (uten interaksjon med GUI)")
    public Response accessToken(@QueryParam("fnr") String fnr) {
        var token = IdportenOidcTokenGenerator.idportenUserToken(fnr, ISSUER, null);
        return Response.ok(new Oauth2AccessTokenResponse(token)).build();
    }

    private static String getBaseUrl(HttpServletRequest req) {
        return req.getScheme() + "://vtp:" + req.getServerPort() + MockServer.CONTEXT_PATH + TJENESTE_PATH;
    }
}
