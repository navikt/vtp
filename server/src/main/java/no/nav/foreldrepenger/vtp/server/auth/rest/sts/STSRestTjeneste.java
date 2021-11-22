package no.nav.foreldrepenger.vtp.server.auth.rest.sts;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;

import org.apache.cxf.ws.security.sts.provider.model.RequestSecurityTokenResponseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
import jakarta.xml.bind.JAXB;
import no.nav.foreldrepenger.vtp.server.auth.rest.KeyStoreTool;
import no.nav.foreldrepenger.vtp.server.auth.rest.OidcTokenGenerator;
import no.nav.foreldrepenger.vtp.server.auth.soap.sts.STSIssueResponseGenerator;

@Api(tags = {"Security Token Service"})
@Path("/v1/sts")
public class STSRestTjeneste {

    public static final String ISSUER = "https://vtp.local/issuer";
    private static final Logger log = LoggerFactory.getLogger(STSRestTjeneste.class);
    private final STSIssueResponseGenerator generator = new STSIssueResponseGenerator();

    @SuppressWarnings("unused")
    @POST
    @Path("/token/exchange")
    @Produces({MediaType.APPLICATION_JSON})
    public SAMLResponse dummySaml(@QueryParam("grant_type") String grant_type,
                                  @QueryParam("subject_token_type") String issuedTokenType,
                                  @QueryParam("subject_token") String subject_token) {
        RequestSecurityTokenResponseType token = generator.buildRequestSecurityTokenResponseType("urn:oasis:names:tc:SAML:2.0:assertion");
        StringWriter sw = new StringWriter();
        JAXB.marshal(token, sw);
        String xmlString = sw.toString();

        SAMLResponse response = new SAMLResponse();
        response.setAccess_token(Base64.getUrlEncoder().withoutPadding().encodeToString(xmlString.getBytes()));
        response.setDecodedToken(xmlString);
        response.setToken_type("Bearer");
        response.setIssued_token_type(issuedTokenType);
        response.setExpires_in(LocalDateTime.MAX);

        return response;
    }

    @SuppressWarnings("unused")
    @Deprecated()
    @GET
    @Path("/token")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getDummyToken(@QueryParam("grant_type") String grant_type,
                                  @QueryParam("scope") String scope,
                                  @Context HttpServletRequest req) {
        var username = getUsername(req);
        if (username != null) {
            return createTokenForUser(username);
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
            return createTokenForUser(username);
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    private Response createTokenForUser(String username) {
        OidcTokenGenerator tokenGenerator = new OidcTokenGenerator(username, null)
                .withIssuer(ISSUER);
        return Response.ok(new UserTokenResponse(tokenGenerator.create(), 600000L, "Bearer"), MediaType.APPLICATION_JSON)
                .build();
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
    @ApiOperation(value = "oauth2/connect/jwk_uri", notes = ("Mock impl av jwk_uri"))
    public Response authorize(@SuppressWarnings("unused") @Context HttpServletRequest req) {
        String jwks = KeyStoreTool.getJwks();
        log.info("JWKS: " + jwks);
        return Response.ok(jwks).build();
    }

    @GET
    @Path("/.well-known/openid-configuration")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Discovery url", notes = ("Mock impl av discovery urlen. "))
    public Response wellKnown(@SuppressWarnings("unused") @Context HttpServletRequest req) {
        log.info("kall på /rest/v1/sts/.well-known/openid-configuration");
        String baseUrl = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort();

        var wkr = new STSWellKnownResponse(ISSUER);
        String basePath = baseUrl + "/rest/v1/sts";

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
        private Long expires_in;
        private String token_type;

        @SuppressWarnings("unused")
        public UserTokenResponse() {
            // Required by Jackson when mapping json object
        }

        public UserTokenResponse(String access_token, Long expires_in, String token_type) {
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

        public Long getExpires_in() {
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
}
