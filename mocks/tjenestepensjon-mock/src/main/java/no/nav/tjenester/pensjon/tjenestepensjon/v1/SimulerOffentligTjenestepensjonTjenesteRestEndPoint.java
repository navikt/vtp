package no.nav.tjenester.pensjon.tjenestepensjon.v1;


import io.swagger.annotations.Api;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.TestscenarioRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Api(tags = {"Simuler tjenestepensjon"})
@Path("/tpregisteret/person")
public class SimulerOffentligTjenestepensjonTjenesteRestEndPoint {
    private static final Logger LOG = LoggerFactory.getLogger(SimulerOffentligTjenestepensjonTjenesteRestEndPoint.class);

    TestscenarioRepositoryImpl testscenarioRepository;

    public SimulerOffentligTjenestepensjonTjenesteRestEndPoint() {
        try {
            testscenarioRepository = TestscenarioRepositoryImpl.getInstance(BasisdataProviderFileImpl.getInstance());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }




    @GET
    @Path("/{fnr}/tpordninger")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buildPermitResponse(
             @NotNull @PathParam("fnr") Long unntakId
    ) {

        return Response.status(200).entity(null).build();
    }
}