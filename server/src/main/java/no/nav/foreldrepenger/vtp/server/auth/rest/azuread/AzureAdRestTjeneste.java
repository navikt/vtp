package no.nav.foreldrepenger.vtp.server.auth.rest.azuread;

import static jakarta.ws.rs.core.Response.ok;
import static jakarta.ws.rs.core.UriBuilder.fromUri;
import static no.nav.foreldrepenger.vtp.server.auth.rest.Oauth2RequestParameterNames.CLIENT_ID;
import static no.nav.foreldrepenger.vtp.server.auth.rest.Oauth2RequestParameterNames.CODE;
import static no.nav.foreldrepenger.vtp.server.auth.rest.Oauth2RequestParameterNames.GRANT_TYPE;
import static no.nav.foreldrepenger.vtp.server.auth.rest.Oauth2RequestParameterNames.NONCE;
import static no.nav.foreldrepenger.vtp.server.auth.rest.Oauth2RequestParameterNames.REDIRECT_URI;
import static no.nav.foreldrepenger.vtp.server.auth.rest.Oauth2RequestParameterNames.SCOPE;
import static no.nav.foreldrepenger.vtp.server.auth.rest.Oauth2RequestParameterNames.STATE;
import static no.nav.foreldrepenger.vtp.server.auth.rest.azuread.AzureOidcTokenGenerator.azureClientCredentialsToken;

import java.net.URI;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import no.nav.foreldrepenger.vtp.server.MockServer;
import no.nav.foreldrepenger.vtp.server.auth.rest.JsonWebKeyHelper;
import no.nav.foreldrepenger.vtp.server.auth.rest.Oauth2AccessTokenResponse;
import no.nav.foreldrepenger.vtp.server.auth.rest.WellKnownResponse;
import no.nav.foreldrepenger.vtp.testmodell.ansatt.AnsatteIndeks;
import no.nav.foreldrepenger.vtp.testmodell.ansatt.NAVAnsatt;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;

@Tag(name = "AzureAd")
@Path(AzureAdRestTjeneste.TJENESTE_PATH)
public class AzureAdRestTjeneste {
    private static final Logger LOG = LoggerFactory.getLogger(AzureAdRestTjeneste.class);
    protected static final String TJENESTE_PATH = "/azuread"; //NOSONAR
    private static final String ISSUER = "http://vtp/rest/AzureAd";
    private static final Map<String, String> nonceCache = new ConcurrentHashMap<>();

    private static final AnsatteIndeks ANSATTE_INDEKS = BasisdataProviderFileImpl.getInstance().getAnsatteIndeks();

    @GET
    @Path("/isAlive")
    @Produces(MediaType.TEXT_HTML)
    public Response isAliveMock() {
        String isAlive = "Azure AD is OK";
        return ok(isAlive).build();
    }

    @GET
    @Path("/.well-known/openid-configuration")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Azure AD Discovery url")
    public Response wellKnown(@Context HttpServletRequest req) {
        LOG.info("Kall på well-known endepunkt");
        String baseUrl = getBaseUrl(req);
        var wellKnownResponse = new WellKnownResponse(ISSUER, baseUrl + "/authorize", baseUrl + "/jwks", baseUrl + "/token");
        return ok(wellKnownResponse).build();
    }

    @GET
    @Path("/jwks")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "azureAd/discovery/keys")
    public Response authorize() {
        String jwks = JsonWebKeyHelper.getJwks();
        return ok(jwks).build();
    }

    @POST
    @Path("/token")
    @Produces({MediaType.APPLICATION_JSON})
    @Operation(description = "azureAd/access_token")
    public Response accessToken(@FormParam(GRANT_TYPE) String grantType,
                                @FormParam(CODE) String code,
                                @FormParam("assertion") String assertion,
                                @FormParam("refresh_token") @Valid String refreshToken) {
        String token;
        var nonce = nonceCache.get(NONCE);

        return switch (grantType) {
            case "client_credentials" -> {
                token = azureClientCredentialsToken(UUID.randomUUID().toString().substring(0, 19), ISSUER);
                yield ok(new Oauth2AccessTokenResponse(token)).build();
            }
            case "urn:ietf:params:oauth:grant-type:jwt-bearer" -> {
                Objects.requireNonNull(assertion);
                var claimsAssertion = AzureOidcTokenGenerator.getClaimsFromAssertion(assertion);
                var ansattId = Optional.ofNullable(claimsAssertion).map(AzureOidcTokenGenerator::getNavIdent).orElse(null);
                var ansattIdent = Optional.ofNullable(ansattId).map(ANSATTE_INDEKS::findByIdent).orElseThrow();
                token = createToken(ansattIdent, nonce);
                yield ok(new Oauth2AccessTokenResponse(token)).build();
            }
            case "authorization_code" -> {
                var ansattIdent = Optional.ofNullable(code)
                        .map(ANSATTE_INDEKS::findByIdent)
                        .orElseThrow(() -> new WebApplicationException("Bad code", Response.Status.BAD_REQUEST));
                token = createToken(ansattIdent, nonce);
                yield ok(new Oauth2AccessTokenResponse(token)).build();
            }
            case "refresh_token" -> {
                if (refreshToken == null) {
                    yield badRequest();
                }
                token = createToken(ANSATTE_INDEKS.findByIdent("saksbeh"), nonce);
                yield ok(new Oauth2AccessTokenResponse(token)).build();
            }
            default -> {
                LOG.warn("Ukjent / unsupported grant type {}", grantType);
                throw new WebApplicationException("Ukjent / unsupported grant type " + grantType, Response.Status.UNAUTHORIZED);
            }
        };
    }

    private static Response badRequest() {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("Forsøk på refresh av token uten refresh_token lagt ved i requesten")
                .build();
    }

    public static boolean isNotNullAndBlank(String value) {
        return value != null && !value.isBlank();
    }

    @GET
    @Path("/bruker")
    @Produces({MediaType.APPLICATION_JSON})
    @Operation(description = "azureAd/access_token - brukes primært av autotest til å logge inn en saksbehandler programmatisk (uten interaksjon med GUI)")
    public Response accessToken(@QueryParam("ident") @DefaultValue("saksbeh") String ident) {
        var bruker = Optional.ofNullable(ANSATTE_INDEKS.findByIdent(ident)).orElseThrow();
        var token = createToken(bruker, nonceCache.get(NONCE));
        return ok(new Oauth2AccessTokenResponse(token)).build();
    }

    private String createToken(NAVAnsatt bruker, String nonce) {
        return AzureOidcTokenGenerator.azureUserToken(bruker, ISSUER, nonce);
    }

    private static String getBaseUrl(HttpServletRequest req) {
        return req.getScheme() + "://vtp:" + req.getServerPort() + MockServer.CONTEXT_PATH + TJENESTE_PATH;
    }

    @GET
    @Path("/authorize")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML})
    @Operation(description = "AzureAD/v2.0/authorize")
    @SuppressWarnings("unused")
    public Response authorize(@QueryParam("response_type") @DefaultValue(CODE) String responseType,
                              @QueryParam(SCOPE) @DefaultValue("openid") String scope,
                              // openid, offline_access eller https://graph.microsoft.com/mail.read permissions
                              @QueryParam(CLIENT_ID) @NotNull String clientId,
                              @QueryParam(STATE) String state,
                              @QueryParam(NONCE) String nonce,
                              @QueryParam(REDIRECT_URI) @NotNull String redirectUri) {
        LOG.info("kall mot AzureAD authorize");

        if (!Objects.equals(responseType, CODE)) {
            throw new IllegalArgumentException("Unsupported responseType [" + responseType + "], should be 'code'");
        }

        var location = fromUri(redirectUri);
        if (isNotNullAndBlank(state)) {
            location.queryParam(STATE, state);
        }
        if (isNotNullAndBlank(nonce)) {
            // Token som produseres ved innloging må ha riktig nonce verdi siden Wonderwall validerer verdien.
            nonceCache.put(NONCE, nonce);
            location.queryParam(NONCE, nonce);
        }
        return authorizeHtmlPage(location);
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
        return ANSATTE_INDEKS.alleAnsatte().stream()
                .map(ansatt -> leggTilRadITabell(location.build(), ansatt))
                .collect(Collectors.joining("\n"));
    }

    private static String leggTilRadITabell(URI location, NAVAnsatt ansatt) {
        var redirectForInnloggingAvAnsatt = fromUri(location).queryParam(CODE, ansatt.ident()).build();
        return String.format("<tr><a href=\"%s\"><h1>%s</h1></a></tr>", redirectForInnloggingAvAnsatt, ansatt.displayName());
    }
}
