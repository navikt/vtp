package no.nav.foreldrepenger.vtp.server.auth.rest.tokenx;

import static org.jose4j.jws.AlgorithmIdentifiers.RSA_USING_SHA256;

import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nimbusds.jwt.JWTParser;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import no.nav.foreldrepenger.vtp.server.auth.rest.KeyStoreTool;

@Api(tags = {"TokenX"})
@Path("/tokenx")
public class TokenxRestTjeneste {
    private static final Logger LOG = LoggerFactory.getLogger(TokenxRestTjeneste.class);
    public static final String ISSUER = "http://TokenDings";

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
    public Response wellKnown(@Context UriInfo uriInfo) {
        var baseUrl = uriInfo.getBaseUri().toString();
        var token_endpoint = baseUrl + "tokenx/token";
        var jwks_endpoint = baseUrl + "tokenx/jwks";
        var wellKnownResponse = new TokenXWellKnownResponse(ISSUER, token_endpoint, jwks_endpoint);
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
        var token = accessTokenForAudienceOgSubject(audience, subject);
        LOG.info("Henter token for subject [{}] som kan brukes til å kalle audience [{}]", subject, audience);
        LOG.trace("Token: {}", token);
        return Response.ok(new TokenExchangeResponse(token)).build();
    }


    private String accessTokenForAudienceOgSubject(String audience, String subject) throws JoseException {
        var jwtClaims = new JwtClaims();
        jwtClaims.setIssuer(ISSUER);
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

}
