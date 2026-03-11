package no.nav.medl2.rest.api.v1;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;

@Path("medl2/api/v1")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MedlemskapsunntakMock {

    @Context
    private TestscenarioBuilderRepository scenarioRepository;

    @POST
    @Path("/periode/soek")
    public List<Medlemskapsunntak> hentMedlemsperioder(@NotNull MedlemRequest request) {
        return new MedlemskapsunntakAdapter(scenarioRepository).finnMedlemsunntak(request.personident());
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record MedlemRequest(@NotNull String personident) { }

}

