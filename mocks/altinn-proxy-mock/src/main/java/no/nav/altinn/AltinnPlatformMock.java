package no.nav.altinn;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mock for Altinn 3 platform tjenester:
 * - Token exchange (Maskinporten → Altinn token)
 * - PDP authorization (sjekk om system har tilgang til organisasjon)
 */
@Path("/dummy/altinn-tre")
public class AltinnPlatformMock {
    private static final Logger LOG = LoggerFactory.getLogger(AltinnPlatformMock.class);

    @GET
    @Path("/authentication/api/v1/exchange/maskinporten")
    @Produces(MediaType.APPLICATION_JSON)
    public Response exchangeMaskinportenToken() {
        LOG.info("er dette den siste");
        LOG.info("Altinn mock: Veksler maskinporten-token til altinn-token");
        return Response.ok("\"vtp-altinn-mock-token\"").build();
    }

    @POST
    @Path("/authorization/api/v1/authorize")
    @Produces(MediaType.APPLICATION_JSON)
    public Response authorize(String test) {
        LOG.info("Altinn mock: PDP authorize kall mottatt");
        var permitResponse = "{\"response\":[{\"decision\":\"Permit\"}]}";
        return Response.ok(permitResponse).build();
    }
}
