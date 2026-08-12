package no.nav.foreldrepenger.vtp.server.api.scenario;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import no.nav.foreldrepenger.vtp.kontrakter.FødselsnummerGenerator;
import no.nav.foreldrepenger.vtp.kontrakter.organisasjon.NavAnsattDto;
import no.nav.foreldrepenger.vtp.kontrakter.organisasjon.NavGruppeDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.PersonDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.TilordnetIdentDto;
import no.nav.foreldrepenger.vtp.kontrakter.person.v2.PersonopplysningerDto;
import no.nav.vtp.ansatt.AnsatteIndeks;
import no.nav.vtp.person.PersonRepository;
import no.nav.vtp.person.ident.PersonIdent;


@Path("/api/testscenarios/v2")
public class TestscenarioRestTjeneste {

    public TestscenarioRestTjeneste() {
        //CDI
    }

    @POST
    @Path("/opprett")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response initialiserTestScenarioFraDtoReturnerIdenter(List<PersonDto> personer) {
        var eksisterendePersoner = personer.stream()
                .collect(Collectors.toMap(PersonDto::uuid, p -> PersonRepository.hentPerson(p.uuid())));
        var identer = personer.stream().collect(Collectors.toMap(PersonDto::uuid, (PersonDto p) ->
                eksisterendePersoner.get(p.uuid())
                        .map(e -> new PersonIdent(e.personopplysninger().identifikator().value()))
                        .orElseGet(() -> genererUnikFødselsnummer(p))
        ));
        var personerMapped = personer.stream().map(p -> PersonMapper.tilPerson(p, identer, eksisterendePersoner.get(p.uuid())))
                .toList();
        PersonRepository.leggTilPersoner(personerMapped);

        // Sikrer at identer er unike, så fremt VTP ikke kjøres opp og ned mellom sesjoner.
        var tilordnedeIdenter = identer.entrySet().stream()
                .map(e -> new TilordnetIdentDto(e.getKey(), e.getValue().fnr(), e.getValue().aktørId()))
                .toList();
        return Response.status(Response.Status.OK).entity(tilordnedeIdenter).build();
    }

    @POST
    @Path("/personer")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response initialiserTestScenarioFraDtoV2ReturnerIdenter(List<no.nav.foreldrepenger.vtp.kontrakter.person.v2.PersonDto> personer) {
        var eksisterendePersoner = personer.stream()
                .collect(Collectors.toMap(p -> p.personopplysninger().uuid(),
                        p -> PersonRepository.hentPerson(p.personopplysninger().uuid())));
        var identer = personer.stream().collect(Collectors.toMap(p -> p.personopplysninger().uuid(), p ->
                eksisterendePersoner.get(p.personopplysninger().uuid())
                        .map(e -> new PersonIdent(e.personopplysninger().identifikator().value()))
                        .orElseGet(() -> genererUnikFødselsnummer(p.personopplysninger()))
        ));
        var personerMapped = personer.stream()
                .map(p -> PersonMapperV2.tilPerson(p, identer, eksisterendePersoner.get(p.personopplysninger().uuid())))
                .toList();
        PersonRepository.leggTilPersoner(personerMapped);

        var tilordnedeIdenter = identer.entrySet().stream()
                .map(e -> new TilordnetIdentDto(e.getKey(), e.getValue().fnr(), e.getValue().aktørId()))
                .toList();
        return Response.status(Response.Status.OK).entity(tilordnedeIdenter).build();
    }

    @POST
    @Path("/grupper")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response initialiserGrupper(List<NavGruppeDto> grupper) {
        AnsatteIndeks.leggTilGrupper(grupper, false);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @POST
    @Path("/ansatte")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response initialiserAnsatte(List<NavAnsattDto> ansatte) {
        AnsatteIndeks.leggTilAnsatte(ansatte, false);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @GET
    @Path("/alleidenter")
    @Produces(MediaType.APPLICATION_JSON)
    public Response hentInitialiserteCaser() {
        var alleIdenter = PersonRepository.alleIdenter();
        return Response.status(Response.Status.OK)
                .entity(alleIdenter)
                .build();
    }


    private static PersonIdent genererUnikFødselsnummer(PersonDto p) {
        return new PersonIdent(new FødselsnummerGenerator.Builder()
                .kjønn(p.kjønn())
                .fødselsdato(p.fødselsdato()).buildAndGenerate());
    }

    private static PersonIdent genererUnikFødselsnummer(PersonopplysningerDto p) {
        return new PersonIdent(new FødselsnummerGenerator.Builder()
                .kjønn(p.kjønn())
                .fødselsdato(p.fødselsdato()).buildAndGenerate());
    }
}
