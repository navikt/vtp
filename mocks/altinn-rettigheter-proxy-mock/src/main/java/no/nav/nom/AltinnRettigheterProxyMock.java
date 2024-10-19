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
 * Tjeneste for å sjekke om person har tilgang til en .
 * http://altinn-rettigheter-proxy.arbeidsgiver/altinn-rettigheter-proxy/ekstern/altinn/api/serviceowner/reportees
 */

@Tag(name = "altinn-rettigheter")
@Path("/altinn-rettigheter-proxy")
public class AltinnRettigheterProxyMock {

    @POST
    @Path("/ekstern/altinn/api/serviceowner/reportees")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Person skjermet")
    public Boolean hentTilganger(SkjermetRequestDto request) {
        return false;
    }

    record SkjermetRequestDto(String personident) { }

    record SkjermetBulkRequestDto(List<String> personidenter) { }
}
