package no.nav.vtp;

import java.util.List;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/dummy/brreg/autorisert-api/personer/rolleutskrift")
public class BrregMock {

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Rolleutskrift hentRolleutskrift() {
        return new Rolleutskrift(List.of());
    }

    public record Rolleutskrift(List<Object> enheter) {
    }
}
