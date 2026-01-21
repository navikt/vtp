package no.nav.altinn;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;

/*
 * Tjeneste for å sjekke om person har tilgang til en .
 * https://arbeidsgiver-altinn-tilganger.intern.dev.nav.no/swagger-ui#/altinn-tilganger
 */

@Tag(name = "altinn-rettigheter")
@Path("/arbeidsgiver-altinn-tilganger")
public class ArbeidsgiverAltinnTilgangerMock {

    @Context
    private TestscenarioBuilderRepository scenarioRepository;

    @POST
    @Path("/altinn-tilganger")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Henter alle tilganger en bruker har for de angitte i requesten ressurser.")
    public Response hentTilganger(ArbeidsgiverAltinnTilgangerRequest request) {
        var resurser = hentRessurser(request.filter());
        var alleOrgnr = scenarioRepository.hentAlleOrganisasjonsnummer();
        return Response.ok().entity(lagPositivRespons(resurser, alleOrgnr)).build();
    }

    @POST
    @Path("/altinn-tilganger/empty")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Leverer et svar uten noen tilganger.")
    public Response hentIngenTilganger(ArbeidsgiverAltinnTilgangerRequest request) {
        return Response.ok().entity(lagNegativRespons()).build();
    }

    private ArbeidsgiverAltinnTilgangerResponse lagPositivRespons(Set<String> resurser, Set<String> alleOrgnr) {
        var tilgangTilOrgNr = toMap(resurser, alleOrgnr);
        var orgNrTilTilganger = toMap(alleOrgnr, resurser);
        return new ArbeidsgiverAltinnTilgangerResponse(false, List.of(), orgNrTilTilganger, tilgangTilOrgNr);
    }

    private Map<String, List<String>> toMap(Set<String> keys, Set<String> values) {
        return keys.stream().collect(Collectors.toMap(Function.identity(), _ -> new ArrayList<>(values)));
    }

    private ArbeidsgiverAltinnTilgangerResponse lagNegativRespons() {
        return new ArbeidsgiverAltinnTilgangerResponse(false, List.of(), Map.of(), Map.of());
    }

    private Set<String> hentRessurser(ArbeidsgiverAltinnTilgangerRequest.FilterCriteria filter) {
        return Stream.concat(filter.altinn2Tilganger().stream(), filter.altinn3Tilganger().stream()).collect(Collectors.toSet());
    }

    public record ArbeidsgiverAltinnTilgangerRequest(FilterCriteria filter) {
        public record FilterCriteria(@JsonProperty("altinn2Tilganger") List<String> altinn2Tilganger,
                                     @JsonProperty("altinn3Tilganger") List<String> altinn3Tilganger) {
        }
    }

    public record ArbeidsgiverAltinnTilgangerResponse(boolean isError,
                                                      List<Organisasjon> hierarki,
                                                      Map<String, List<String>> orgNrTilTilganger,
                                                      Map<String, List<String>> tilgangTilOrgNr) {

        public record Organisasjon(String orgnr,
                                   @JsonProperty("altinn3Tilganger") List<String> altinn3Tilganger,
                                   @JsonProperty("altinn2Tilganger") List<String> altinn2Tilganger,
                                   List<Organisasjon> underenheter,
                                   String navn,
                                   String organisasjonsform,
                                   boolean erSlettet) {
        }
    }
}
