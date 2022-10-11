package no.nav.foreldrepenger.vtp.server.auth.rest.tokenx;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;

import org.jose4j.lang.JoseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;

import no.nav.foreldrepenger.vtp.server.auth.rest.Token;

@ExtendWith(MockitoExtension.class)
class TokenXTjenesteTest {

    private static TokenxRestTjeneste tokenxRestTjeneste;

    @Mock
    private HttpServletRequest req;

    @BeforeAll
    public static void setup() {
        System.setProperty("javax.net.ssl.keystore.test", "src/test/resources/.modig/keystore.jks");
        tokenxRestTjeneste = new TokenxRestTjeneste();
    }

    @Test
    void verifiserRiktigWellKnownEndepunkt() {
        var issuer = "http://vtp:8060/rest/tokenx";
        when(req.getScheme()).thenReturn("http");
        when(req.getServerPort()).thenReturn(8060);
        var response = tokenxRestTjeneste.wellKnown(req);
        assertThat(response.getEntity()).isInstanceOf(TokenXWellKnownResponse.class);
        var tokenXWellKnownResponse = (TokenXWellKnownResponse) response.getEntity();
        assertThat(tokenXWellKnownResponse.issuer()).isEqualTo(issuer);
        assertThat(tokenXWellKnownResponse.token_endpoint()).isEqualTo(issuer + "/token");
        assertThat(tokenXWellKnownResponse.jwks_uri()).isEqualTo(issuer + "/jwks");

    }


    @Test
    void verifisererTokenSomGenereresHarRiktigAudienceOgSubject() throws JoseException, ParseException {
        when(req.getScheme()).thenReturn("http");
        when(req.getServerPort()).thenReturn(8060);
        var subject_token = new Token("""
                eyJraWQiOiIxIiwiYWxnIjoiUlMyNTYifQ.eyJpc3MiOiJodHRwczovL2xvZ2luLm1pY3Jvc29mdG9ubGluZS5jb20vYWFkYjJjL3YyLjAiLCJleHAiOjE2MzU1MTIxNDMsImp0aSI6IllZNmh1T19reWJPTDFsWmxnZFp3T3ciLCJpYXQiOjE2MzU0OTA1NDMsInN1YiI6IjE3NDk4ODMyODU3IiwiYXVkIjoiT0lEQyIsImFjciI6IkxldmVsNCIsImF6cCI6Ik9JREMifQ.vM6sjTD5uRX25yqtNrUiue3DwWaXA-xkVW52ied3n2MHqfXB-pd76r9LyqDJ-sDZen-AhxihL6GkNLUrXlRm8Ij-FZaJsIQaOnj2CRRHlJfL4Zj-s0i2B81ov0qJclJKtZFhFELwWkqykqu1IB7W2s7_5d2k0C1jAQCs9gnzCoKv6H8EPgrRriWRvMv691DuqzluPLLsqMK7cwQmODkc3fN4gtoW4Wf6FqG2XDdR6Mr-OT-w_exn8i0uB8jPieSOfyby5CG7rXLW_BQT9KovIrDuwYCyEkU7hSCEYksu5CBSoBpupKs-07cUXIksLWouCZcLjGoMWfnG-rHeTq0w-g
                """);
        var audience = "fpfordel";
        var response = tokenxRestTjeneste.token(
                req,
                null,
                null,
                null,
                null,
                subject_token,
                audience);

        var tokenExchangeResponse = (TokenxRestTjeneste.TokenDingsResponsDto) response.getEntity();
        var jwt = jwt(tokenExchangeResponse.accessToken().value());
        var jwtClaimsSet = jwt.getJWTClaimsSet();
        assertThat(jwtClaimsSet.getSubject()).isEqualTo("17498832857");
        assertThat(jwtClaimsSet.getAudience())
                .hasSize(1)
                .contains(audience);
    }

    private JWT jwt(String access_token) {
        try {
            return JWTParser.parse(access_token);
        } catch (ParseException e) {
            throw new RuntimeException("Ikke gyldig access_token!");
        }
    }
}
