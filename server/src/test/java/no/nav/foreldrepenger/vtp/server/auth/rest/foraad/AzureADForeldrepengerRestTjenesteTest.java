package no.nav.foreldrepenger.vtp.server.auth.rest.foraad;

import static org.assertj.core.api.Assertions.assertThat;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

class AzureADForeldrepengerRestTjenesteTest {

    private static AzureADForeldrepengerRestTjeneste aadRestTjeneste;

    @Mock
    private HttpServletRequest req;

    @BeforeAll
    public static void setup() {
        aadRestTjeneste = new AzureADForeldrepengerRestTjeneste();
    }

    @Test
    void verifiserRiktigWellKnownEndepunkt() {
        // Scope: api://localhost.<namespace>.<app-name>/.default
        // ClientID: localhost.<namespace>.<app-name>
        var clientIDFpsak = "localhost.teamforeldrepenger.fpsak";
        var clientIDFpabakus = "localhost.teamforeldrepenger.fpabakus";

        var scope = "OIDC api://" + clientIDFpsak +"/.default api://" + clientIDFpabakus +"/.default";
        var audience = AzureADForeldrepengerRestTjeneste.hentAudienceFra(scope);

        assertThat(audience).hasSize(3)
                .containsExactly("OIDC", clientIDFpsak, clientIDFpabakus);

    }
}
