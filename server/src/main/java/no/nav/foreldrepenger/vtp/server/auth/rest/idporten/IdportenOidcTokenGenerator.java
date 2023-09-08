package no.nav.foreldrepenger.vtp.server.auth.rest.idporten;

import no.nav.foreldrepenger.vtp.server.auth.rest.KeyStoreTool;

import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.NumericDate;
import org.jose4j.lang.JoseException;


public final class IdportenOidcTokenGenerator {

    private IdportenOidcTokenGenerator() {
    }

    public static String idportenUserToken(String fnr, String issuer) {
        return createToken(createCommonClaims(fnr, issuer));
    }

    private static JwtClaims createCommonClaims(String pid, String issuer) {
        var issuedAt = NumericDate.now();
        JwtClaims claims = new JwtClaims();
        claims.setIssuer(issuer);
        claims.setExpirationTime(NumericDate.fromSeconds(issuedAt.getValue() + 3600 * 10));
        claims.setGeneratedJwtId();
        claims.setIssuedAt(issuedAt);
        claims.setNotBefore(issuedAt);
        claims.setSubject(pid);
        claims.setAudience("vtp");
        claims.setStringClaim("client_id", "vtp");
        claims.setStringClaim("pid", pid);
        claims.setStringClaim("acr", System.getProperty("idporten.acr.scope", "idporten-loa-high"));
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
            throw new IllegalStateException(e);
        }
    }

}
