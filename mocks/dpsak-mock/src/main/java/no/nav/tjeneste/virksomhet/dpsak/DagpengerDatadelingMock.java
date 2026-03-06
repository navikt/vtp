package no.nav.tjeneste.virksomhet.dpsak;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import no.nav.vtp.PersonRepository;


@Tag(name = "Dagpengermock")
@Path("/dagpenger/datadeling/v1")
public class DagpengerDatadelingMock {

    @Context
    private PersonRepository personRepository;

    @SuppressWarnings("unused")
    @POST
    @Path("/perioder")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Returnerer perioder med dagpenger fra DPsak og Arena")
    public DagpengerPerioderDto postDagpengerPerioder(PersonRequest personRequest) {
        // TODO: Utvide IAY-modell med Dp-datadeling-Dagpenger og populere response med data fra testscenario
        var tomRespons = new DagpengerPerioderDto(personRequest.personIdent(), List.of());
        return tomRespons;
    }

    @POST
    @Path("/beregninger")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Returnerer utbetalte dagpenger fra DPsak")
    public DagpengerBeregningerDto[] postDagpengerBeregning(PersonRequest personRequest) {
        // TODO: Utvide IAY-modell med Dp-datadeling-Dagpenger og populere response med data fra testscenario
        List<DagpengerBeregningerDto> tomrespons = new ArrayList<>();
        return tomrespons.toArray(DagpengerBeregningerDto[]::new);
    }


    public record PersonRequest(String personIdent, LocalDate fraOgMedDato, LocalDate tilOgMedDato) { }

}
