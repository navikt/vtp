package no.nav.altinn;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import no.nav.vtp.Person;
import no.nav.vtp.PersonRepository;
import no.nav.vtp.arbeidsforhold.Arbeidsforhold;
import no.nav.vtp.ident.Identifikator;
import no.nav.vtp.ident.Orgnummer;
import no.nav.vtp.inntekt.Inntektsperiode;

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
        var alleOrgnr = hentAlleOrgnummre(personRepository.allePersoner());
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

    protected static Set<String> hentAlleOrgnummre(List<Person> personer) {
        var alleArbeidsforhold = personer.stream()
                .flatMap(p -> p.arbeidsforhold().stream())
                .map(Arbeidsforhold::identifikator)
                .filter(Orgnummer.class::isInstance)
                .map(Identifikator::ident)
                .collect(Collectors.toSet());
        var alleOrgnummerInntektopplysninger = personer.stream()
                .flatMap(p -> p.inntekt().stream())
                .map(Inntektsperiode::identifikator)
                .filter(Orgnummer.class::isInstance)
                .map(Identifikator::ident)
                .collect(Collectors.toSet());
        return Stream.concat(alleArbeidsforhold.stream(), alleOrgnummerInntektopplysninger.stream())
                .collect(Collectors.toSet());
    }
}
