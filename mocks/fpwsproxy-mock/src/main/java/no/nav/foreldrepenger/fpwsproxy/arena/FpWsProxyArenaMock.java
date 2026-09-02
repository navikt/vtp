package no.nav.foreldrepenger.fpwsproxy.arena;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import no.nav.foreldrepenger.kontrakter.fpwsproxy.arena.request.ArenaRequestDto;

@Path("/api/fpwsproxy/arena")
// Endepunktet beholdes fordi abakus fortsatt kaller det for både FP- og K9-flyter.
public class FpWsProxyArenaMock {
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response henterDagpengerOgAAP(@Valid ArenaRequestDto arenaRequestDto) {
        return Response.ok(List.of()).build();
    }
}
