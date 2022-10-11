package no.nav.foreldrepenger.vtp.server.auth.rest.sts;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static no.nav.foreldrepenger.vtp.server.auth.rest.Oauth2RequestParameterNames.GRANT_TYPE;
import static no.nav.foreldrepenger.vtp.server.auth.rest.Oauth2RequestParameterNames.SCOPE;
import static no.nav.foreldrepenger.vtp.server.auth.rest.Oauth2RequestParameterNames.SUBJECT_TOKEN;
import static no.nav.foreldrepenger.vtp.server.auth.rest.Oauth2RequestParameterNames.SUBJECT_TOKEN_TYPE;
import static no.nav.foreldrepenger.vtp.server.auth.rest.TokenClaims.stsTokenClaims;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;

import javax.enterprise.context.ApplicationScoped;
import javax.servlet.http.HttpServletRequest;
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
import javax.xml.bind.JAXB;

import org.apache.cxf.ws.security.sts.provider.model.RequestSecurityTokenResponseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.foreldrepenger.vtp.server.auth.rest.JsonWebKeyHelper;
import no.nav.foreldrepenger.vtp.server.auth.rest.Oauth2AccessTokenResponse;
import no.nav.foreldrepenger.vtp.server.auth.rest.Token;
import no.nav.foreldrepenger.vtp.server.auth.soap.sts.STSIssueResponseGenerator;

@ApplicationScoped
@Api(tags = {"Security Token Service"})
@Path("/v1/sts")
public class STSRestTjeneste {

    private static final Logger LOG = LoggerFactory.getLogger(STSRestTjeneste.class);
    private final STSIssueResponseGenerator generator = new STSIssueResponseGenerator();

    @SuppressWarnings("unused")
    @POST
    @Path("/token/exchange")
    @Produces({MediaType.APPLICATION_JSON})
    public SAMLResponse dummySaml(@QueryParam(GRANT_TYPE) String grant_type,
                                  @QueryParam(SUBJECT_TOKEN_TYPE) String issuedTokenType,
                                  @QueryParam(SUBJECT_TOKEN) String subject_token) {
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
    public Response getDummyToken(@QueryParam(GRANT_TYPE) String grant_type,
                                  @QueryParam(SCOPE) String scope,
                                  @Context HttpServletRequest req) {
        LOG.warn("Kall på deprecated GET /token endepunkt!");
        var username = getUsername(req);
        if (username != null) {
            var userToken = createTokenForUser(username, scope, req);
            return Response.ok(userToken, MediaType.APPLICATION_JSON).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @POST
    @Path("/token")
    @Produces({MediaType.APPLICATION_JSON})
    public Response dummyToken(@Context HttpServletRequest req,
                               @FormParam(GRANT_TYPE) String grant_type,
                               @FormParam(SCOPE) @DefaultValue("openid") String scope) {
        var username = getUsername(req);
        if (username != null) {
            var userToken = createTokenForUser(username, scope, req);
            LOG.info("STS token er utsedt {}", userToken.accessToken());
            return Response.ok(userToken, MediaType.APPLICATION_JSON).build();
        } else {
            LOG.warn("Request inneholder ikke Autorization basic header!");
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    private Oauth2AccessTokenResponse createTokenForUser(String username, String scope, HttpServletRequest request) {
        var claims = stsTokenClaims(username, getIssuer(request), scope);
        return new Oauth2AccessTokenResponse(Token.fra(claims.build()));
    }

    private String getUsername(HttpServletRequest req) {
        final String authorization = req.getHeader(AUTHORIZATION);
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
        var jwks = JsonWebKeyHelper.getJwks();
        LOG.info("JWKS: {}", jwks);
        return Response.ok(jwks).build();
    }

    @GET
    @Path("/.well-known/openid-configuration")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Discovery url", notes = ("Mock impl av discovery urlen. "))
    public Response wellKnown(@SuppressWarnings("unused") @Context HttpServletRequest req) {
        LOG.info("kall på /rest/v1/sts/.well-known/openid-configuration");
        return Response
                .ok(new STSWellKnownResponse(getIssuer(req)))
                .build();
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

    private String getBaseUrl(HttpServletRequest req) {
        return req.getScheme() + "://vtp:" + req.getServerPort();
    }

    private String getIssuer(HttpServletRequest req) {
        return getBaseUrl(req) + "/rest/v1/sts";
    }
}
