package no.nav.foreldrepenger.vtp.server.auth.rest.tokenx;

import static no.nav.foreldrepenger.vtp.server.auth.rest.Oauth2RequestParameterNames.AUDIENCE;
import static no.nav.foreldrepenger.vtp.server.auth.rest.Oauth2RequestParameterNames.GRANT_TYPE;
import static no.nav.foreldrepenger.vtp.server.auth.rest.Oauth2RequestParameterNames.SUBJECT_TOKEN;
import static no.nav.foreldrepenger.vtp.server.auth.rest.Oauth2RequestParameterNames.SUBJECT_TOKEN_TYPE;
import static no.nav.foreldrepenger.vtp.server.auth.rest.TokenClaims.tokenXClaims;

import java.text.ParseException;

import javax.enterprise.context.ApplicationScoped;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.foreldrepenger.vtp.server.auth.rest.JsonWebKeyHelper;
import no.nav.foreldrepenger.vtp.server.auth.rest.Token;

@ApplicationScoped
@Api(tags = {"TokenX"})
@Path("/tokenx")
public class TokenxRestTjeneste {
    private static final Logger LOG = LoggerFactory.getLogger(TokenxRestTjeneste.class);

    public static final JwtConsumer UNVALIDATING_CONSUMER = new JwtConsumerBuilder()
            .setSkipAllValidators()
            .setDisableRequireSignature()
            .setSkipSignatureVerification()
            .build();

    @GET
    @Path("/isAlive")
    @Produces(MediaType.TEXT_HTML)
    public Response isAliveMock() {
        String isAlive = "TokenX mock OK";
        return Response.ok(isAlive).build();
    }

    @GET
    @Path("/.well-known/oauth-authorization-server")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "TokenX Discovery url", notes = ("Mock impl av TokenX discovery urlen"))
    public Response wellKnown(@Context HttpServletRequest req) {
        LOG.info("Kall på well-known endepunkt");
        var issuer = getIssuer(req);
        var token_endpoint = issuer + "/token";
        var jwks_endpoint = issuer + "/jwks";
        var wellKnownResponse = new TokenXWellKnownResponse(issuer, token_endpoint, jwks_endpoint);
        return Response.ok(wellKnownResponse).build();
    }

    @GET
    @Path("/jwks")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "TokenX public key set")
    public Response jwks(@Context HttpServletRequest req) {
        LOG.info("Kall på /tokenx/jwks");
        var jwks = JsonWebKeyHelper.getJwks();
        LOG.trace("Jwks er {}", jwks);
        return Response.ok(jwks).build();
    }

    @POST
    @Path("/token")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "TokenX public key set")
    public Response token(@Context HttpServletRequest req,
                          @FormParam(GRANT_TYPE) @DefaultValue("urn:ietf:params:oauth:grant-type:token-exchange") String grant_type,
                          @FormParam("client_assertion_type") @DefaultValue("urn:ietf:params:oauth:grant-type:token-exchange") String client_assertion_type,
                          @FormParam("client_assertion") @Valid Token client_assertion,
                          @FormParam(SUBJECT_TOKEN_TYPE) @DefaultValue("urn:ietf:params:oauth:token-type:jwt") String subject_token_type,
                          @FormParam(SUBJECT_TOKEN) @Valid Token subject_token,
                          @FormParam(AUDIENCE) String audience) throws ParseException {
        var subject = hentSubjectFraJWT(subject_token);;
        var accessToken = Token.fra(tokenXClaims(subject, getIssuer(req), audience));
        LOG.info("Henter accessToken for subject [{}] som kan brukes til å kalle audience [{}]: {}", subject, audience, accessToken.value());
        return Response.ok(new TokenDingsResponsDto(accessToken)).build();
    }

    private String hentSubjectFraJWT(Token subject_token) {
        try {
            var claims = UNVALIDATING_CONSUMER.processToClaims(subject_token.value());
            return claims.getSubject();
        } catch (InvalidJwtException | MalformedClaimException e) {
            throw new RuntimeException("Subjekt_token er ikke av typen JWT og vi kan derfor ikke hente ut sub i claims", e);
        }
    }

    private static String getBaseUrl(HttpServletRequest req) {
        return req.getScheme() + "://vtp:" + req.getServerPort();
    }

    public static String getIssuer(HttpServletRequest req) {
        return getBaseUrl(req) + "/rest/tokenx";
    }

    public record TokenDingsResponsDto(@JsonProperty("access_token") @Valid Token accessToken,
                                       @JsonProperty("issued_token_type") String issuedTokenType,
                                       @JsonProperty("token_type") String tokenType,
                                       @JsonProperty("expires_in") Integer expiresIn) {

        public TokenDingsResponsDto(Token accessToken) {
            this(accessToken, "urn:ietf:params:oauth:token-type:access_token", "Bearer", accessToken.expiresIn());
        }
    }

}
