package no.nav.foreldrepenger.vtp.server.auth.rest.tokenx;

import static org.jose4j.jws.AlgorithmIdentifiers.RSA_USING_SHA256;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nimbusds.jwt.JWTParser;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.foreldrepenger.vtp.server.auth.rest.KeyStoreTool;

@Api(tags = {"TokenX"})
@Path("/tokenx")
public class TokenxRestTjeneste {
    private static final Logger LOG = LoggerFactory.getLogger(TokenxRestTjeneste.class);

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
        var jwks = KeyStoreTool.getJwks();
        LOG.trace("Jwks er {}", jwks);
        return Response.ok(jwks).build();
    }

    @POST
    @Path("/token")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "TokenX public key set")
    public Response token(@Context HttpServletRequest req,
                          @FormParam("grant_type") @DefaultValue("urn:ietf:params:oauth:grant-type:token-exchange") String grant_type,
                          @FormParam("client_assertion_type") @DefaultValue("urn:ietf:params:oauth:grant-type:token-exchange") String client_assertion_type,
                          @FormParam("client_assertion") String client_assertion,
                          @FormParam("subject_token_type") @DefaultValue("urn:ietf:params:oauth:token-type:jwt") String subject_token_type,
                          @FormParam("subject_token") String subject_token,
                          @FormParam("audience") String audience) throws JoseException {
        var subject = hentSubjectFraJWT(subject_token);
        var token = accessTokenForAudienceOgSubject(req, audience, subject);
        LOG.info("Henter token for subject [{}] som kan brukes til å kalle audience [{}]", subject, audience);
        LOG.info("TokenX token: {}", token);
        return Response.ok(new TokenExchangeResponse(token)).build();
    }


    private String accessTokenForAudienceOgSubject(HttpServletRequest req, String audience, String subject) throws JoseException {
        var jwtClaims = new JwtClaims();
        jwtClaims.setIssuer(getIssuer(req));
        jwtClaims.setAudience(audience);
        jwtClaims.setSubject(subject);
        jwtClaims.setExpirationTimeMinutesInTheFuture(60F);
        jwtClaims.setGeneratedJwtId();
        jwtClaims.setIssuedAtToNow();
        jwtClaims.setClaim("acr", "Level4");
        jwtClaims.setNotBeforeMinutesInThePast(0F);

        var rsaJWK = KeyStoreTool.getJsonWebKey();
        var jws = new JsonWebSignature();
        jws.setPayload(jwtClaims.toJson());
        jws.setKeyIdHeaderValue(rsaJWK.getKeyId());
        jws.setAlgorithmHeaderValue("RS256");
        jws.setKey(rsaJWK.getPrivateKey());
        jws.setAlgorithmHeaderValue(RSA_USING_SHA256);
        return jws.getCompactSerialization();
    }

    private String hentSubjectFraJWT(String subject_token) {
        try {
            var jwt = JWTParser.parse(subject_token);
            return jwt.getJWTClaimsSet().getSubject();
        } catch (java.text.ParseException e) {
            throw new RuntimeException("Subjekt_token er ikke av typen JWT og vi kan derfor ikke hente ut sub i claims", e);
        }
    }

    private String getBaseUrl(HttpServletRequest req) {
        return req.getScheme() + "://vtp:" + req.getServerPort();
    }

    private String getIssuer(HttpServletRequest req) {
        return getBaseUrl(req) + "/rest/tokenx";
    }

}
