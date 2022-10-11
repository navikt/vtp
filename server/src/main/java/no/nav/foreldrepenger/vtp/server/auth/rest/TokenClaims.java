package no.nav.foreldrepenger.vtp.server.auth.rest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.nimbusds.jwt.JWTClaimsSet;

import no.nav.foreldrepenger.vtp.testmodell.ansatt.NAVAnsatt;

public class TokenClaims {
    private static final int DEFAULT_EXPIRE_TIME_SECONDS = 3600;
    private static final int EXPIRE_TIME_SECONDS = tokenExpireTime();
    public static final String AZP = "azp";
    public static final String ACR = "acr";
    public static final String SCP = "scp";
    public static final String NONCE = "nonce";
    public static final String AZP_NAME = "azp_name";
    public static final String NAVIDENT = "NAVident";

    private TokenClaims() {
        // Statisk implementasjon
    }

    public static JWTClaimsSet.Builder stsTokenClaims(String sub, String issuer, String scope) {
        return defaultJwtClaims(sub, issuer, sub)
                .claim(SCP, scope)
                .claim(AZP, sub);
    }

    public static JWTClaimsSet.Builder openAmTokenClaims(String sub, String issuer, String nonce) {
        var claims = defaultJwtClaims(sub, issuer, "OIDC")
                .claim(AZP, "OIDC")
                .claim(ACR, "Level4");
        if (nonce !=  null) claims.claim(NONCE, nonce);
        return claims;
    }

    public static JWTClaimsSet.Builder azureOidcTokenClaims(NAVAnsatt ansatt,
                                                            List<String> aud,
                                                            String clientId,
                                                            String issuer,
                                                            String scope,
                                                            String tenant) {
        return defaultJwtClaims(clientId + ":" + ansatt.cn(), issuer, aud)
                .claim("oid", UUID.nameUUIDFromBytes(ansatt.cn().getBytes()).toString())
                .claim("tid", tenant)
                .claim("preferred_username", ansatt.email())
                .claim("ver", "2.0")
                .claim(NAVIDENT, ansatt.cn())
                .claim(SCP, scope)
                .claim(AZP, clientId)
                .claim(AZP_NAME, clientId);
    }

    public static JWTClaimsSet.Builder azureSystemTokenClaims(List<String> aud,
                                                              String sub,
                                                              String clientId,
                                                              String issuer,
                                                              String tenantId) {
        return defaultJwtClaims(sub, issuer, aud)
                .claim(AZP_NAME, clientId)
                .claim(AZP, clientId)
                .claim("oid", sub)
                .claim("tid", tenantId)
                .claim("ver", "2.0")
                .claim("roles", List.of("access_as_application"));
    }

    public static JWTClaimsSet.Builder tokenXClaims(String sub, String issuer, String aud) {
        return defaultJwtClaims(sub, issuer, aud)
                .claim("pid", sub)
                .claim(ACR, "Level4");
    }

    public static JWTClaimsSet.Builder defaultJwtClaims(String sub, String issuer, String audience) {
        return defaultJwtClaims(sub, issuer, List.of(audience));
    }

    public static JWTClaimsSet.Builder defaultJwtClaims(String sub, String issuer, List<String> audience) {
        var localDateTimeNow = LocalDateTime.now();
        var now = Date.from(localDateTimeNow.atZone(ZoneId.systemDefault()).toInstant());
        var expire = Date.from(localDateTimeNow.plusSeconds(EXPIRE_TIME_SECONDS).atZone(ZoneId.systemDefault()).toInstant());
        return new JWTClaimsSet.Builder()
                .audience(audience)
                .issuer(issuer)
                .subject(sub)
                .issueTime(now)
                .notBeforeTime(now)
                .expirationTime(expire)
                .jwtID(UUID.randomUUID().toString());
    }

    private static int tokenExpireTime() {
        var token_expire_time_seconds = System.getenv("OVERRIDE_TOKEN_EXPIRE_TIME_SECONDS");
        if (token_expire_time_seconds != null && kanBliParsetTilInteger(token_expire_time_seconds) ) {
            return Integer.parseInt(token_expire_time_seconds);
        }
        return DEFAULT_EXPIRE_TIME_SECONDS;
    }

    public static boolean kanBliParsetTilInteger(String streng) {
        if (streng == null) {
            return false;
        }
        try {
            Integer.parseInt(streng);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

}
