package no.nav.foreldrepenger.vtp.server.auth.rest.azureAD;

import static org.assertj.core.api.Assertions.assertThat;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

class AADRestTjenesteTest {

    private static AzureADRestTjeneste aadRestTjeneste;

    @Mock
    private HttpServletRequest req;

    @BeforeAll
    public static void setup() {
        aadRestTjeneste = new AzureADRestTjeneste();
    }

    @Test
    void verifiserRiktigWellKnownEndepunkt() {
        // Scope: api://localhost.<namespace>.<app-name>/.default
        // ClientID: localhost.<namespace>.<app-name>
        var clientIDFpsak = "localhost.teamforeldrepenger.fpsak";
        var clientIDFpabakus = "localhost.teamforeldrepenger.fpabakus";

        var scope = "OIDC api://" + clientIDFpsak +"/.default api://" + clientIDFpabakus +"/.default";
        var audience = AzureADRestTjeneste.hentAudienceFra(scope);

        assertThat(audience).hasSize(3)
                .containsExactly("OIDC", clientIDFpsak, clientIDFpabakus);

    }
}
