package no.nav.nom;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/*
 * Tjeneste for å sjekke om person er skjermet.
 * Grensesnitt https://skjermede-personer-pip.<intern-standard-dev-uri>/swagger-ui/index.html#/
 */

@Tag(name = "nom")
@Path("/api/nom")
public class SkjermetPersonMock {

    @POST
    @Path("/skjermet")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Person skjermet")
    public Boolean personErSkjermet(SkjermetRequestDto request) {
        return false;
    }


    @POST
    @Path("/skjermetBulk")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Map<String, Boolean> isSkjermetBulk(SkjermetBulkRequestDto request) {
        Map<String, Boolean> response = new LinkedHashMap<>();
        request.personidenter().forEach(pi -> response.put(pi, false));
        return response;
    }

    record SkjermetRequestDto(String personident) { }

    record SkjermetBulkRequestDto(List<String> personidenter) { }
}
