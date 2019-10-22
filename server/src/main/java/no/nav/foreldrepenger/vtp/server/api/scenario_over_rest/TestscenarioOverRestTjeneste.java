package no.nav.foreldrepenger.vtp.server.api.scenario_over_rest;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.foreldrepenger.vtp.kontrakter.TestscenarioDto;
import no.nav.foreldrepenger.vtp.kontrakter.TestscenarioPersonopplysningDto;
import no.nav.foreldrepenger.vtp.kontrakter.TestscenariodataDto;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold.ArbeidsforholdModell;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.inntektkomponent.InntektskomponentModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.BarnModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.Testscenario;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Api(tags = {"Testscenario-test"})
@Path("/api/testscenario/test")
public class TestscenarioOverRestTjeneste {

    private static final Logger LOG = LoggerFactory.getLogger(TestscenarioOverRestTjeneste.class);

    @Context
    private TestscenarioRepository testscenarioRepository;


    @PUT
    @Path("/endrescenario/{id}")
    @ApiOperation(value="", notes="Patcher et testcase. Serialiserer uten sjekk, du må selv ha styr på typer")
    public Response endreScenario(TestscenarioDto testscenarioDto) {

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @DELETE
    @Path("/slettscenario/{id}")
    @ApiOperation(value="", notes="Sletter initialisert testscenario som matcher id")
    public Response slettScenario(@PathParam("id") String id) {
        if(testscenarioRepository.slettScenario(id)) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @GET
    @Path("/initialiserte")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value="", notes = "Henter alle initialiserte scenarior i minnet til VTP", response = TestscenarioDto.class)
    public List<TestscenarioDto> hentInitialiserteScenario() {
        Collection<Testscenario> testscenarios = testscenarioRepository.getTestscenarios();
        List<TestscenarioDto> testscenarioList = new ArrayList<>();

        testscenarios.forEach(testscenario -> {
            testscenarioList.add(konverterTilTestscenarioDto(testscenario));
        });

        return testscenarioList;
    }

    @POST
    @Path("/initialiser")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "", notes = ("Initialiserer et testscenario basert på angitt json streng og returnerer det initialiserte objektet"), response = TestscenarioDto.class)
    public TestscenarioDto initialiserTestScenario(String testscenarioJson) {
        // TODO(EW) getUserSuppliedVariables(uriInfo.getQueryParameters(), TEMPLATE_KEY) inkluderes ved behov.
        Testscenario testscenario = testscenarioRepository.opprettTestscenarioFraJsonString(testscenarioJson);
        return konverterTilTestscenarioDto(testscenario);
    }


    private TestscenarioDto konverterTilTestscenarioDto(Testscenario scenario) {
        String fnrSøker = scenario.getPersonopplysninger().getSøker().getIdent();
        String fnrAnnenPart = scenario.getPersonopplysninger().getAnnenPart().getIdent();
        String aktørIdSøker = scenario.getPersonopplysninger().getSøker().getAktørIdent();
        String aktørIdAnnenPart = scenario.getPersonopplysninger().getAnnenPart().getAktørIdent();
        Optional<LocalDate> fødselsdato = fødselsdatoBarn(scenario);
        TestscenarioPersonopplysningDto testscenarioPersonopplysning = new TestscenarioPersonopplysningDto(
                fnrSøker,
                fnrAnnenPart,
                aktørIdSøker,
                aktørIdAnnenPart,
                fødselsdato.orElse(null));

        InntektskomponentModell inntektskomponentModell = scenario.getSøkerInntektYtelse().getInntektskomponentModell();
        ArbeidsforholdModell arbeidsforholdModell = scenario.getSøkerInntektYtelse().getArbeidsforholdModell();
        TestscenariodataDto scenariodataSøker = new TestscenariodataDto(inntektskomponentModell, arbeidsforholdModell);

        TestscenariodataDto scenariodataAnnenpart = null;
        if (scenario.getAnnenpartInntektYtelse() != null) {
            InntektskomponentModell inntektskomponentModellAnnenpart = scenario.getAnnenpartInntektYtelse().getInntektskomponentModell();
            ArbeidsforholdModell arbeidsforholdModellAnnenpart = scenario.getAnnenpartInntektYtelse().getArbeidsforholdModell();
            scenariodataAnnenpart = new TestscenariodataDto(inntektskomponentModellAnnenpart, arbeidsforholdModellAnnenpart);
        }

        return new TestscenarioDto(scenario.getId(), scenario.getVariabelContainer().getVars(),
                testscenarioPersonopplysning, scenariodataSøker, scenariodataAnnenpart);
    }

    private Optional<LocalDate> fødselsdatoBarn(Testscenario testscenario) {
        Optional<BarnModell> barnModell = testscenario.getPersonopplysninger().getFamilierelasjoner()
                .stream()
                .filter(modell -> modell.getTil() instanceof BarnModell)
                .map(modell -> ((BarnModell) modell.getTil()))
                .findFirst();

        return barnModell.map(PersonModell::getFødselsdato);
    }


}

