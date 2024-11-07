package no.nav.altinn;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import no.nav.foreldrepenger.vtp.testmodell.repo.BasisdataProvider;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.TestscenarioRepositoryImpl;

/*
 * Tjeneste for å sjekke om person har tilgang til en .
 * http://altinn-rettigheter-proxy.arbeidsgiver/altinn-rettigheter-proxy/ekstern/altinn/api/serviceowner/reportees
 */

@Tag(name = "altinn-rettigheter")
@Path("/altinn-rettigheter-proxy")
public class AltinnRettigheterProxyMock {

    @Context
    private TestscenarioBuilderRepository scenarioRepository;

    @GET
    @Path("/ekstern/altinn/api/serviceowner/reportees")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Henter alle virksomheter.")
    public Response hentTilgangerTilVirksomheter() {
        var alleOrgnr = scenarioRepository.hentAlleOrganisasjonsnummer();
        return Response.ok().entity(mapToResponse(alleOrgnr)).build();
    }

    @GET
    @Path("/ekstern/altinn/api/serviceowner/reportees/empty")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Henter ingen virksomheter. Brukes for tingangsnekt testing.")
    public Response hentIngenTilganger() {
        return Response.ok().entity(List.of()).build();
    }

    private List<AltinnReportee> mapToResponse(Set<String> alleOrgnr) {
        return alleOrgnr.stream().map(AltinnReportee::new).toList();
    }

    @JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
    record AltinnReportee(String organizationNumber) {
    }
}
