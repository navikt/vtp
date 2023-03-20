package no.nav.foreldrepenger.vtp.server.auth.rest.aadfp;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.NumericDate;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;

import no.nav.foreldrepenger.vtp.server.auth.rest.KeyStoreTool;


public final class AzureOidcTokenGenerator {
    private AzureOidcTokenGenerator() {
    }

    private static final JwtConsumer UNVALIDATING_CONSUMER = new JwtConsumerBuilder()
            .setSkipAllValidators()
            .setDisableRequireSignature()
            .setSkipSignatureVerification()
            .build();

    static JwtClaims getClaimsFromAssertion(String assertion) {
        try {
            return UNVALIDATING_CONSUMER.processToClaims(assertion);
        } catch (Exception e) {
            throw new WebApplicationException("Bad mock access token; must be on format Bearer access:<userid>", Response.Status.FORBIDDEN);
        }
    }

    static String getNavIdent(JwtClaims claims)  {
        try {
            return claims.getStringClaimValue("NAVident");
        } catch (MalformedClaimException e) {
            return null;
        }
    }

    public static String azureUserToken(StandardBruker bruker, String issuer) {
        JwtClaims claims = createCommonClaims(bruker.getIdent(), issuer);
        claims.setStringClaim("NAVident", bruker.getIdent());
        claims.setStringListClaim("groups", bruker.getGrupper().stream().toList());

        return createToken(claims);
    }

    public static String azureClientCredentialsToken(String sub, String issuer) {
        JwtClaims claims = createCommonClaims(sub, issuer);

        claims.setClaim("oid", sub); // Konvensjon

        return createToken(claims);
    }

    private static JwtClaims createCommonClaims(String sub, String issuer) {
        var issuedAt = NumericDate.now();
        JwtClaims claims = new JwtClaims();
        claims.setIssuer(issuer);
        claims.setExpirationTime(NumericDate.fromSeconds(issuedAt.getValue() + 3600 * 10));
        claims.setGeneratedJwtId();
        claims.setIssuedAt(issuedAt);
        claims.setNotBefore(issuedAt);
        claims.setSubject(sub);
        claims.setStringClaim("acr", "Level4");
        claims.setAudience("vtp");
        claims.setStringClaim("azp_name", "vtp:teamforeldrepenger:vtp");
        claims.setStringClaim("azp", "vtp");
        claims.setStringClaim("scp", "api://vtp.teamforeldrepenger.vtp/.default");
        return claims;
    }

    private static String createToken(JwtClaims claims) {
        RsaJsonWebKey senderJwk = KeyStoreTool.getJsonWebKey();
        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setKeyIdHeaderValue(KeyStoreTool.getJsonWebKey().getKeyId());
        jws.setAlgorithmHeaderValue("RS256");
        jws.setKey(senderJwk.getPrivateKey());
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
        try {
            return jws.getCompactSerialization();
        } catch (JoseException e) {
            throw new RuntimeException(e);
        }
    }

}