package no.nav.tjeneste.virksomhet.infotrygd.rest;

import java.util.List;
import java.util.Objects;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

@Path("/infotrygd")
// Endepunktene beholdes fordi abakus fortsatt kaller dem for både FP- og K9-flyter.
public class InfotrygdMock {
    @SuppressWarnings("unused")
    @POST
    @Path("/grunnlag/sykepenger")
    @Produces({"application/json"})
    public Response getSykepenger(PersonRequest personRequest) {
        Objects.requireNonNull(personRequest.fnr());
        return Response.ok(List.of()).build();
    }

    @SuppressWarnings("unused")
    @POST
    @Path("/grunnlag/paaroerende-sykdom")
    @Produces({"application/json"})
    public Response paaroerendeSykdomUsingPost(PersonRequest personRequest) {
        Objects.requireNonNull(personRequest.fnr());
        return Response.ok(List.of()).build();
    }
}
