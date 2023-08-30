package no.nav.vtp;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.OPTIONS;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Utilities")
@Path("/dummy/{var:.+}")
public class DummyRestTjeneste {

    private static final Logger LOG = LoggerFactory.getLogger(DummyRestTjeneste.class);

    public DummyRestTjeneste() {

    }

    @GET
    @Operation(description = "Returnerer ett blankt resultat")
    public Response get() {
        LOG.info("Fikk en forespørsel på DummyRestTjeneste");
        return Response.ok().build();
    }

    @POST
    @Operation(description = "Returnerer ett blankt resultat")
    public Response post() {
        LOG.info("Lager en forespørsel på DummyRestTjeneste");
        return Response.ok().build();
    }

    @OPTIONS
    @Operation(description = "Returnerer ett blankt resultat")
    public Response options() {
        return Response.ok().build();
    }
}
