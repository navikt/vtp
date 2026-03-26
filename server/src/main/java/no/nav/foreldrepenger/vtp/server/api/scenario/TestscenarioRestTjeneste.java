package no.nav.foreldrepenger.vtp.server.api.scenario;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import no.nav.foreldrepenger.vtp.kontrakter.FødselsnummerGenerator;
import no.nav.foreldrepenger.vtp.kontrakter.person.PersonDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.TilordnetIdentDto;
import no.nav.vtp.person.Person;
import no.nav.vtp.person.PersonRepository;
import no.nav.vtp.person.ident.PersonIdent;


@Path("/api/testscenarios/v2")
public class TestscenarioRestTjeneste {

    @Context
    private PersonRepository personRepository;

    public TestscenarioRestTjeneste() {
        //CDI
    }

    public TestscenarioRestTjeneste(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @POST
    @Path("/opprett")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response initialiserTestScenarioFraDtoReturnerIdenter(List<PersonDto> personer) {
        var identer = personer.stream().collect(Collectors.toMap(PersonDto::uuid, p -> {
            var eksisterende = personRepository.hentPerson(p.uuid());
            return eksisterende.map(e -> new PersonIdent(e.personopplysninger().identifikator().value()))
                    .orElseGet(() -> genererUnikFødselsnummer(p));
        }));
        var personerMapped = personer.stream().map(p -> PersonMapper.tilPerson(p, identer))
                .toList();
        personRepository.leggTilPersoner(personerMapped);

        // Sikrer at identer er unike, så fremt VTP ikke kjøres opp og ned mellom sesjoner.
        var tilordnedeIdenter = identer.entrySet().stream()
                .map(e -> new TilordnetIdentDto(e.getKey(), e.getValue().fnr(), e.getValue().aktørId()))
                .toList();
        return Response.status(Response.Status.OK).entity(tilordnedeIdenter).build();
    }

    @GET
    @Path("/alleidenter")
    @Produces(MediaType.APPLICATION_JSON)
    public Response hentInitialiserteCaser() {
        var alleIdenter = personRepository.alleIdenter();
        return Response.status(Response.Status.OK)
                .entity(alleIdenter)
                .build();
    }


    private static PersonIdent genererUnikFødselsnummer(PersonDto p) {
        return new PersonIdent(new FødselsnummerGenerator.Builder().fodselsdato(p.fødselsdato()).buildAndGenerate());
    }
}
