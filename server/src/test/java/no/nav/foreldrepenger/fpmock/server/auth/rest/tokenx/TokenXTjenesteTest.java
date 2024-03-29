package no.nav.foreldrepenger.fpmock.server.auth.rest.tokenx;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;

import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.lang.JoseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import no.nav.foreldrepenger.vtp.server.auth.rest.tokenx.TokenExchangeResponse;
import no.nav.foreldrepenger.vtp.server.auth.rest.tokenx.TokenXWellKnownResponse;
import no.nav.foreldrepenger.vtp.server.auth.rest.tokenx.TokenxRestTjeneste;

@ExtendWith(MockitoExtension.class)
class TokenXTjenesteTest {

    private static TokenxRestTjeneste tokenxRestTjeneste;

    @Mock
    private HttpServletRequest req;

    @BeforeAll
    public static void setup() {
        tokenxRestTjeneste = new TokenxRestTjeneste();
    }

    @Test
    public void verifiserRiktigWellKnownEndepunkt() {
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
    @Disabled // Mangler keystore i ´pipe... Legge denne til eller fjerne test? TODO
    public void verifisererTokenSomGenereresHarRiktigAudienceOgSubject() throws JoseException, MalformedClaimException {
        when(req.getScheme()).thenReturn("http");
        when(req.getServerPort()).thenReturn(8060);
        var subject_token = """
                eyJraWQiOiIxIiwiYWxnIjoiUlMyNTYifQ.eyJpc3MiOiJodHRwczovL2xvZ2luLm1pY3Jvc29mdG9ubGluZS5jb20vYWFkYjJjL3YyLjAiLCJleHAiOjE2MzU1MTIxNDMsImp0aSI6IllZNmh1T19reWJPTDFsWmxnZFp3T3ciLCJpYXQiOjE2MzU0OTA1NDMsInN1YiI6IjE3NDk4ODMyODU3IiwiYXVkIjoiT0lEQyIsImFjciI6IkxldmVsNCIsImF6cCI6Ik9JREMifQ.vM6sjTD5uRX25yqtNrUiue3DwWaXA-xkVW52ied3n2MHqfXB-pd76r9LyqDJ-sDZen-AhxihL6GkNLUrXlRm8Ij-FZaJsIQaOnj2CRRHlJfL4Zj-s0i2B81ov0qJclJKtZFhFELwWkqykqu1IB7W2s7_5d2k0C1jAQCs9gnzCoKv6H8EPgrRriWRvMv691DuqzluPLLsqMK7cwQmODkc3fN4gtoW4Wf6FqG2XDdR6Mr-OT-w_exn8i0uB8jPieSOfyby5CG7rXLW_BQT9KovIrDuwYCyEkU7hSCEYksu5CBSoBpupKs-07cUXIksLWouCZcLjGoMWfnG-rHeTq0w-g
                """;
        var audience = "fpfordel";
        var response = tokenxRestTjeneste.token(
                req,
                null,
                null,
                null,
                null,
                subject_token,
                audience);

        var tokenExchangeResponse = (TokenExchangeResponse) response.getEntity();
        var jwtClaims = jwt(tokenExchangeResponse.access_token());
        assertThat(jwtClaims.getSubject()).isEqualTo("17498832857");
        assertThat(jwtClaims.getAudience())
                .hasSize(1)
                .contains(audience);
    }

    private JwtClaims jwt(String access_token) {
        try {
            return TokenxRestTjeneste.UNVALIDATING_CONSUMER.processToClaims(access_token);
        } catch (InvalidJwtException e) {
            throw new RuntimeException("Ikke gyldig access_token!");
        }
    }
}
