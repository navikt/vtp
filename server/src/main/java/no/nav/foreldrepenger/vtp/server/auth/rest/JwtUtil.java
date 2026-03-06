package no.nav.foreldrepenger.vtp.server.auth.rest;

import java.time.Instant;
import java.util.List;

import org.jose4j.base64url.Base64;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;

public class JwtUtil {

    private JwtUtil() {
    }

    private static final JwtConsumer unvalidatingConsumer = new JwtConsumerBuilder().setSkipAllValidators()
        .setDisableRequireSignature()
        .setSkipSignatureVerification()
        .build();

    public static String getJwtBody(String jwt) {
        try {
            var jsonObjects = unvalidatingConsumer.process(jwt).getJoseObjects();
            var jwtBody = ((JsonWebSignature) jsonObjects.getFirst()).getUnverifiedPayloadBytes();
            return Base64.encode(jwtBody);
        } catch (InvalidJwtException e) {
            throw ugyldigJwt(e);
        }
    }

    public static JwtClaims getClaims(String jwt) {
        try {
            return unvalidatingConsumer.processToClaims(jwt);
        } catch (InvalidJwtException e) {
            throw ugyldigJwt(e);
        }
    }

    public static String getIssuer(JwtClaims claims) {
        try {
            return claims.getIssuer();
        } catch (MalformedClaimException e) {
            throw ugyldigJwt(e);
        }
    }

    public static String getSubject(JwtClaims claims) {
        try {
            return claims.getSubject();
        } catch (MalformedClaimException e) {
            throw ugyldigJwt(e);
        }
    }

    public static List<String> getAudience(JwtClaims claims) {
        try {
            return claims.getAudience();
        } catch (MalformedClaimException e) {
            throw ugyldigJwt(e);
        }
    }

    public static Instant getExpirationTime(JwtClaims claims) {
        try {
            long expirationTime = claims.getExpirationTime().getValue();
            return Instant.ofEpochSecond(expirationTime);
        } catch (MalformedClaimException e) {
            throw ugyldigJwt(e);
        }
    }

    public static long getExpirationTimeRaw(JwtClaims claims) {
        try {
            return claims.getExpirationTime().getValue();
        } catch (MalformedClaimException e) {
            throw ugyldigJwt(e);
        }
    }

    public static String getClientName(JwtClaims claims) {
        try {
            String azp = claims.getStringClaimValue("azp");
            if (azp != null) {
                return azp;
            }
            List<String> audience = claims.getAudience();
            if (audience.size() == 1) {
                return audience.getFirst();
            }
            throw new IllegalStateException("F-026678: Kan ikke utlede clientName siden 'azp' ikke er satt og 'aud' er %s".formatted(audience));
        } catch (MalformedClaimException e) {
            throw ugyldigJwt(e);
        }

    }

    public static String getStringClaim(JwtClaims claims, String key) {
        try {
            return claims.getStringClaimValue(key);
        } catch (MalformedClaimException e) {
            throw ugyldigJwt(e);
        }
    }

    public static List<String> getStringListClaim(JwtClaims claims, String key) {
        try {
            return claims.getStringListClaimValue(key);
        } catch (MalformedClaimException e) {
            throw ugyldigJwt(e);
        }
    }

    private static IllegalStateException ugyldigJwt(Exception e) {
        return new IllegalStateException("F-026968: Feil ved parsing av JWT", e);
    }

}
