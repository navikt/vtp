package no.nav.foreldrepenger.vtp.server.auth.rest;

import static org.jose4j.jws.AlgorithmIdentifiers.RSA_USING_SHA256;

import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.NumericDate;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;

import com.fasterxml.jackson.annotation.JsonValue;

public record Token(@JsonValue String value, Integer expiresIn) {

    public Token(String value) {
        this(value, null);
    }


    public JwtClaims parseToken() {
        try {
            return UNVALIDATING_CONSUMER.processToClaims(value);
        } catch (InvalidJwtException e) {
            throw new RuntimeException("Token er ikke gylding og kunne derfor ikke hente claims", e);
        }
    }

    public static Token fra(JwtClaims claims) {
        var jwk = JsonWebKeyHelper.getJsonWebKey();
        var jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setKeyIdHeaderValue(jwk.getKeyId());
        jws.setKey(jwk.getPrivateKey());
        jws.setAlgorithmHeaderValue(RSA_USING_SHA256);
        try {
            return new Token(jws.getCompactSerialization(), expiresInSeconds(claims));
        } catch (JoseException e) {
            throw new RuntimeException("Noe gikk galt ved seralisering av JWT", e);
        }
    }

    private static int expiresInSeconds(JwtClaims claims) {
        try {
            var expirationTime = claims.getExpirationTime();
            return (int) (expirationTime.getValueInMillis() - NumericDate.now().getValueInMillis())/1000;
        } catch (MalformedClaimException e) {
            throw new RuntimeException(e);
        }


    }

    public static final JwtConsumer UNVALIDATING_CONSUMER = new JwtConsumerBuilder()
            .setSkipAllValidators()
            .setDisableRequireSignature()
            .setSkipSignatureVerification()
            .build();


    @Override
    public String value() {
        return value;
    }
}
