package no.nav.foreldrepenger.sokos.simulering;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/sokos/api/osSimulerBeregningApi")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SokosSimuleringMock {

    @POST
    @Path("/simulerBeregning")
    public SokosSimuleringRespons simulerBeregning(@Valid @NotNull SokosSimuleringRequest request) {
        return SokosSimuleringMapper.simuler(request);
    }
}
