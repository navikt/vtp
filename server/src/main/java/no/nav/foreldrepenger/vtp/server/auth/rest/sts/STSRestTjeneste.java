package no.nav.foreldrepenger.vtp.server.auth.rest.sts;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import no.nav.foreldrepenger.vtp.server.auth.rest.KeyStoreTool;
import no.nav.foreldrepenger.vtp.server.auth.rest.OidcTokenGenerator;

@Tag(name = "Security Token Service")
@Path("/v1/sts")
public class STSRestTjeneste {

    private static final Logger LOG = LoggerFactory.getLogger(STSRestTjeneste.class);

    @SuppressWarnings("unused")
    @POST
    @Path("/token/exchange")
    @Produces({MediaType.APPLICATION_JSON})
    public SAMLResponse dummySaml(@QueryParam("grant_type") String grant_type,
                                  @QueryParam("subject_token_type") String issuedTokenType,
                                  @QueryParam("subject_token") String subject_token) {
        throw new UnsupportedOperationException("/token/exchange - er ikke lenger supportert.");
    }

    @SuppressWarnings("unused")
    @Deprecated()
    @GET
    @Path("/token")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getDummyToken(@QueryParam("grant_type") String grant_type,
                                  @QueryParam("scope") String scope,
                                  @Context HttpServletRequest req) {
        LOG.warn("Kall på deprecated GET /token endepunkt!");
        var username = getUsername(req);
        if (username != null) {
            var userToken = createTokenForUser(username, req);
            return Response.ok(userToken, MediaType.APPLICATION_JSON).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @SuppressWarnings("unused")
    @POST
    @Path("/token")
    @Produces({MediaType.APPLICATION_JSON})
    public Response dummyToken(@FormParam("grant_type") String grant_type,
                               @FormParam("scope") String scope,
                               @Context HttpServletRequest req) {
        var username = getUsername(req);
        if (username != null) {
            var userToken = createTokenForUser(username, req);
            LOG.info("STS token er utsedt {}", userToken);
            return Response.ok(userToken, MediaType.APPLICATION_JSON).build();
        } else {
            LOG.warn("Request inneholder ikke Autorization basic header!");
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    private UserTokenResponse createTokenForUser(String username, HttpServletRequest request) {
        var tokenGenerator = new OidcTokenGenerator(username, null)
                .withIssuer(getIssuer(request));
        return new UserTokenResponse(tokenGenerator.create(), OidcTokenGenerator.EXPIRE_IN_SECONDS, "Bearer");
    }

    private String getUsername(HttpServletRequest req) {
        final String authorization = req.getHeader("Authorization");
        if (authorization != null && authorization.toLowerCase().startsWith("basic")) {
            // Authorization: Basic base64credentials
            String base64Credentials = authorization.substring("Basic".length()).trim();
            byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
            String credentials = new String(credDecoded, StandardCharsets.UTF_8);
            // credentials = username:password
            final String[] values = credentials.split(":", 2);
            return values[0];
        }
        return null;
    }

    @GET
    @Path("/jwks")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "oauth2/connect/jwk_uri")
    public Response authorize(@SuppressWarnings("unused") @Context HttpServletRequest req) {
        String jwks = KeyStoreTool.getJwks();
        LOG.info("JWKS: {}", jwks);
        return Response.ok(jwks).build();
    }

    @GET
    @Path("/.well-known/openid-configuration")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Discovery url")
    public Response wellKnown(@SuppressWarnings("unused") @Context HttpServletRequest req) {
        LOG.info("kall på /rest/v1/sts/.well-known/openid-configuration");

        String basePath = getIssuer(req);
        var wkr = new STSWellKnownResponse(basePath);

        wkr.setExchangeTokenEndpoint(basePath + "/token/exchange");
        wkr.setTokenEndpoint(basePath + "/token");
        wkr.setJwksUri(basePath + "/jwks");

        return Response.ok(wkr).build();
    }

    public static class SAMLResponse {

        private String access_token;
        private String issued_token_type;
        private String token_type;
        private String decodedToken;
        private LocalDateTime expires_in;

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public String getIssued_token_type() {
            return issued_token_type;
        }

        public void setIssued_token_type(String issued_token_type) {
            this.issued_token_type = issued_token_type;
        }

        public String getToken_type() {
            return token_type;
        }

        public void setToken_type(String token_type) {
            this.token_type = token_type;
        }

        public String getDecodedToken() {
            return decodedToken;
        }

        public void setDecodedToken(String decodedToken) {
            this.decodedToken = decodedToken;
        }

        public LocalDateTime getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(LocalDateTime expires_in) {
            this.expires_in = expires_in;
        }
    }

    public static class UserTokenResponse {
        private final LocalDateTime issuedTime = LocalDateTime.now();
        private String access_token;
        private int expires_in;
        private String token_type;

        @SuppressWarnings("unused")
        public UserTokenResponse() {
            // Required by Jackson when mapping json object
        }

        public UserTokenResponse(String access_token, int expires_in, String token_type) {
            this.access_token = access_token;
            this.expires_in = expires_in;
            this.token_type = token_type;
        }

        /**
         * @param expirationLeeway the amount of seconds to be subtracted from the expirationTime to avoid returning false positives
         * @return <code>true</code> if "now" is after the expirationtime(minus leeway), else returns <code>false</code>
         */
        public boolean isExpired(long expirationLeeway) {
            return LocalDateTime.now().isAfter(issuedTime.plusSeconds(expires_in).minusSeconds(expirationLeeway));
        }

        public String getAccess_token() {
            return access_token;
        }

        public int getExpires_in() {
            return expires_in;
        }

        public String getToken_type() {
            return token_type;
        }

        @Override
        public String toString() {
            return "UserTokenImpl{" +
                    "access_token='" + access_token + '\'' +
                    ", expires_in=" + expires_in +
                    ", token_type='" + token_type + '\'' +
                    '}';
        }
    }

    private String getBaseUrl(HttpServletRequest req) {
        return req.getScheme() + "://vtp:" + req.getServerPort();
    }

    private String getIssuer(HttpServletRequest req) {
        return getBaseUrl(req) + "/rest/v1/sts";
    }
}
