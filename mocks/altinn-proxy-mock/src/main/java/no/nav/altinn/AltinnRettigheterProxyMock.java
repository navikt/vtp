package no.nav.altinn;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import no.nav.vtp.PersonRepository;
import no.nav.vtp.arbeidsforhold.Organisasjon;
import no.nav.vtp.ident.Orgnummer;

/*
 * Tjeneste for å sjekke om person har tilgang til en .
 * http://altinn-rettigheter-proxy.arbeidsgiver/altinn-rettigheter-proxy/ekstern/altinn/api/serviceowner/reportees
 */

@Path("/altinn-rettigheter-proxy")
public class AltinnRettigheterProxyMock {

    @Context
    private PersonRepository personRepository;

    @GET
    @Path("/ekstern/altinn/api/serviceowner/reportees")
    @Produces(MediaType.APPLICATION_JSON)
    public Response hentTilgangerTilVirksomheter() {
        var alleOrgnr = personRepository.alleRegistrerteOrganisasjoner().stream()
                .map(Organisasjon::orgnummer)
                .map(Orgnummer::value)
                .collect(Collectors.toSet());
        return Response.ok().entity(mapToResponse(alleOrgnr)).build();
    }

    @GET
    @Path("/ekstern/altinn/api/serviceowner/reportees/empty")
    @Produces(MediaType.APPLICATION_JSON)
    // Henter ingen virksomheter. Brukes for tingangsnekt testing.
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
