package no.nav.tjeneste.virksomhet.spokelse.rest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/spokelse")
public class SpøkelseMock {

    @SuppressWarnings("unused")
    @POST
    @Path("/grunnlag")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public SykepengeVedtak[] postSykepenger(PersonRequest personRequest) {
        // TODO: Utvide IAY-modell med Spøkelse-SP og populere response med data fra testscenario
        List<SykepengeVedtak> tomrespons = new ArrayList<>();
        return tomrespons.toArray(SykepengeVedtak[]::new);
    }

    public record PersonRequest(String fodselsnummer, LocalDate fom) { }

}
