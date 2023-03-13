package no.nav.foreldrepenger.vtp.server.auth.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.jose4j.jwt.MalformedClaimException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import no.nav.foreldrepenger.vtp.server.auth.rest.fpaad.AzureADForeldrepengerRestTjeneste;
import no.nav.foreldrepenger.vtp.server.auth.rest.isso.OpenAMRestService;
import no.nav.foreldrepenger.vtp.server.auth.rest.tokenx.TokenxRestTjeneste;

@ExtendWith(MockitoExtension.class)
class TokenProviderTjenesteTest {

    private static TokenProviderTjeneste tokenProviderTjeneste;

    @Mock
    private HttpServletRequest req;

    @BeforeAll
    public static void setup() {
        System.setProperty("javax.net.ssl.keystore.test", "src/test/resources/.modig/keystore.jks");
        var aadRestTjeneste = new AzureADForeldrepengerRestTjeneste();
        var openAMRestService = new OpenAMRestService();
        var tokenxRestTjeneste = new TokenxRestTjeneste();
        tokenProviderTjeneste = new TokenProviderTjeneste(aadRestTjeneste, openAMRestService, tokenxRestTjeneste);
    }

    @Test
    void openAMTokenVerifisering() throws MalformedClaimException {
        when(req.getScheme()).thenReturn("http");
        when(req.getServerPort()).thenReturn(8060);
        when(req.getParameter("state")).thenReturn(UUID.randomUUID().toString());

        var ansattid = "saksbeh";
        Oauth2AccessTokenResponse oauth2AccessTokenResponse;
        try (var response = tokenProviderTjeneste.hentAnsattTokenOpenAm(req, ansattid)) {
            oauth2AccessTokenResponse = (Oauth2AccessTokenResponse) response.getEntity();
        }

        assertThat(oauth2AccessTokenResponse.accessToken()).isNotNull();
        var claims = oauth2AccessTokenResponse.accessToken().parseToken();

        assertThat(claims.getSubject()).isEqualTo(ansattid);
        assertThat(claims.getIssuer()).isNotNull();
    }

    @Test
    void azureADOnBehalfOfTest() throws ParseException, MalformedClaimException {
        when(req.getScheme()).thenReturn("http");
        when(req.getServerPort()).thenReturn(8060);

        var ansattId = "saksbeh";
        var tenant = "tenantVTP";
        var clientId = "test";
        Oauth2AccessTokenResponse oauth2AccessTokenResponse;
        try (var response = tokenProviderTjeneste.hentAnsattTokenAzureAd(req, ansattId, clientId, tenant,
                "api://localhost.teamforeldrepenger.fpsak/.default", "urn:ietf:params:oauth:grant-type:jwt-bearer")) {
            oauth2AccessTokenResponse = (Oauth2AccessTokenResponse) response.getEntity();
        }
        assertThat(oauth2AccessTokenResponse.accessToken()).isNotNull();
        var claims = oauth2AccessTokenResponse.accessToken().parseToken();


        assertThat(claims.getSubject()).isEqualTo(clientId + ":" + ansattId);
        assertThat(claims.getIssuer()).isNotNull();
        assertThat(claims.getClaimValueAsString("tid")).isEqualTo(tenant);
        assertThat(claims.getClaimValueAsString("preferred_username")).isNotNull();
        assertThat(claims.getClaimValueAsString("NAVident")).isEqualTo(ansattId);
    }


    @Test
    void azureClientCredentialTest() throws ParseException, MalformedClaimException {
        when(req.getScheme()).thenReturn("http");
        when(req.getServerPort()).thenReturn(8060);

        var ansattId = "saksbeh";
        var tenant = "tenantVTP";
        var clientId = "test";
        Oauth2AccessTokenResponse oauth2AccessTokenResponse;
        try (var response = tokenProviderTjeneste.hentAnsattTokenAzureAd(req, ansattId, clientId, tenant,
                "api://localhost.teamforeldrepenger.fpsak/.default", "client_credentials")) {
            oauth2AccessTokenResponse = (Oauth2AccessTokenResponse) response.getEntity();
        }
        assertThat(oauth2AccessTokenResponse.accessToken()).isNotNull();
        var claims = oauth2AccessTokenResponse.accessToken().parseToken();

        assertThat(claims.getSubject()).isNotNull();
        assertThat(claims.getIssuer()).isNotNull();
        assertThat(claims.getStringClaimValue("tid")).isEqualTo(tenant);
    }


    @Test
    void tokenDingsTokenExchange() throws ParseException, MalformedClaimException {
        when(req.getScheme()).thenReturn("http");
        when(req.getServerPort()).thenReturn(8060);

        var fnr = "1234567890";
        var audience = "audience de audience";
        TokenxRestTjeneste.TokenDingsResponsDto tokenDingsResponsDto;
        try (var response = tokenProviderTjeneste.hentTokenXToken(req,
                audience, fnr)) {
            tokenDingsResponsDto = (TokenxRestTjeneste.TokenDingsResponsDto) response.getEntity();
        }
        assertThat(tokenDingsResponsDto.accessToken()).isNotNull();
        var claims = tokenDingsResponsDto.accessToken().parseToken();

        assertThat(claims.getSubject()).isEqualTo(fnr);
        assertThat(claims.getAudience()).containsExactly(audience);
        assertThat(claims.getClaimValueAsString("pid")).isEqualTo(fnr);
    }

}
