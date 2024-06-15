package no.nav.foreldrepenger.vtp.server.auth.rest.idporten;

import java.util.Objects;

import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.NumericDate;
import org.jose4j.lang.JoseException;

import no.nav.foreldrepenger.vtp.server.auth.rest.JsonWebKeyHelper;


public final class IdportenOidcTokenGenerator {

    private IdportenOidcTokenGenerator() {
    }

    public static String idportenUserToken(String fnr, String issuer, String nonce) {
        return createToken(createCommonClaims(fnr, issuer, nonce));
    }

    private static JwtClaims createCommonClaims(String pid, String issuer, String nonce) {
        var issuedAt = NumericDate.now();
        JwtClaims claims = new JwtClaims();
        if (Objects.nonNull(nonce) && !nonce.isBlank()) {
            claims.setClaim("nonce", nonce);
        }
        claims.setIssuer(issuer);
        claims.setExpirationTime(NumericDate.fromSeconds(issuedAt.getValue() + 3600 * 10));
        claims.setGeneratedJwtId();
        claims.setIssuedAt(issuedAt);
        claims.setNotBefore(issuedAt);
        claims.setSubject(pid);
        claims.setAudience("vtp");
        claims.setStringClaim("client_id", "vtp");
        claims.setStringClaim("pid", pid);
        claims.setStringClaim("acr", "idporten-loa-high");
        return claims;
    }

    private static String createToken(JwtClaims claims) {
        RsaJsonWebKey senderJwk = JsonWebKeyHelper.getJsonWebKey();
        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setKeyIdHeaderValue(JsonWebKeyHelper.getJsonWebKey().getKeyId());
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
