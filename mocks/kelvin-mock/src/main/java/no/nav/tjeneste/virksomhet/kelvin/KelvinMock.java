package no.nav.tjeneste.virksomhet.kelvin;

import java.time.LocalDate;
import java.util.List;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/kelvin")
public class KelvinMock {

    @POST
    @Path("/maksimum")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ArbeidsavklaringspengerResponse postAAP(PersonRequest personRequest) {
        // TODO: Utvide IAY-modell med Kelvin-AAP og populere response med data fra testscenario
        return new ArbeidsavklaringspengerResponse(List.of());
    }


    @POST
    @Path("/kelvin/maksimumUtenUtbetaling")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ArbeidsavklaringspengerResponse postKelvinVedtakUtenUtbetaling(PersonRequest personRequest) {
        // Beholdes tom - finnes for samhandlingsformål.
        return new ArbeidsavklaringspengerResponse(List.of());
    }

    public record PersonRequest(String personidentifikator, LocalDate fraOgMedDato, LocalDate tilOgMedDato) { }

}
