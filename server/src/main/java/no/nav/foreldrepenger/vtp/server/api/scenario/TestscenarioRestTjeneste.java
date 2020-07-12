package no.nav.foreldrepenger.vtp.server.api.scenario;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.foreldrepenger.vtp.kontrakter.TestscenarioDto;
import no.nav.foreldrepenger.vtp.kontrakter.TestscenarioPersonopplysningDto;
import no.nav.foreldrepenger.vtp.kontrakter.TestscenariodataDto;
import no.nav.foreldrepenger.vtp.server.api.pensjon_testdata.PensjonTestdataService;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold.ArbeidsforholdModell;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.inntektkomponent.InntektskomponentModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.BarnModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.time.LocalDate;
import java.util.*;

@Api(tags = {"Testscenario"})
@Path("/api/testscenarios")
public class TestscenarioRestTjeneste {
    private static final Logger logger = LoggerFactory.getLogger(TestscenarioRestTjeneste.class);

    private static final String TEMPLATE_KEY = "key";
    private static final String SCENARIO_ID = "id";

    @Context
    private TestscenarioTemplateRepository templateRepository;

    @Context
    private TestscenarioRepository testscenarioRepository;

    @Context
    private PensjonTestdataService pensjonTestdataService;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "", notes = "Henter alle templates som er initiert i minnet til VTP", responseContainer = "List", response = TestscenarioDto.class)
    public List<TestscenarioDto> hentInitialiserteCaser() {
        Map<String, Testscenario> testscenarios = testscenarioRepository.getTestscenarios();
        List<TestscenarioDto> testscenarioList = new ArrayList<>();

        testscenarios.forEach((key, testscenario) -> {
            if (testscenario.getTemplateNavn() != null) {
                testscenarioList.add(konverterTilTestscenarioDto(testscenario, testscenario.getTemplateNavn()));
            } else {
                testscenarioList.add(konverterTilTestscenarioDto(testscenario));
            }
        });

        return testscenarioList;
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "", notes = "Returnerer testscenario som matcher id", response = TestscenarioDto.class)
    public Response hentScenario(@PathParam(SCENARIO_ID) String id){
        if (testscenarioRepository.getTestscenario(id) != null) {
            Testscenario testscenario = testscenarioRepository.getTestscenario(id);
            return Response
                    .status(Response.Status.OK)
                    .entity(konverterTilTestscenarioDto(testscenario, testscenario.getTemplateNavn()))
                    .build();
        } else {
            return Response.status(Response.Status.NO_CONTENT).build();
        }

    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value="", notes="Oppdaterer hele scenario som matcher id", response = TestscenarioDto.class)
    public Response oppdaterHeleScenario(@PathParam(SCENARIO_ID) String id, TestscenarioDto testscenarioDto){
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @PATCH
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value="", notes="Oppdaterer deler av et scenario som matcher id", response = TestscenarioDto.class)
    public Response endreScenario(@PathParam(SCENARIO_ID) String id, String patchArray){
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @POST
    @Path("/{key}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "", notes = ("Initialiserer et test scenario basert på angitt template key i VTPs eksempel templates"), response = TestscenarioDto.class)
    public Response initialiserTestscenario(@PathParam(TEMPLATE_KEY) String templateKey, @Context UriInfo uriInfo) {


        TestscenarioTemplate template = templateRepository.finn(templateKey);
        Map<String, String> userSuppliedVariables = getUserSuppliedVariables(uriInfo.getQueryParameters(), TEMPLATE_KEY);
        Testscenario testscenario = testscenarioRepository.opprettTestscenario(template, userSuppliedVariables);
        logger.info("Initialiserer testscenario i VTP fra template: [{}] med id: [{}] ", templateKey, testscenario.getId());

        pensjonTestdataService.opprettData(testscenario);

        return Response
                .status(Response.Status.CREATED)
                .entity(konverterTilTestscenarioDto(testscenario, testscenario.getTemplateNavn()))
                .build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "", notes = ("Initialiserer et testscenario basert på angitt json streng og returnerer det initialiserte objektet"), response = TestscenarioDto.class)
    public Response initialiserTestScenario(String testscenarioJson,  @Context UriInfo uriInfo) {
        Map<String, String> userSuppliedVariables = getUserSuppliedVariables(uriInfo.getQueryParameters(), TEMPLATE_KEY);
        Testscenario testscenario = testscenarioRepository.opprettTestscenarioFraJsonString(testscenarioJson, userSuppliedVariables);
        logger.info("Initialiserer testscenario med ekstern testdatadefinisjon. Opprettet med id: [{}] ", testscenario.getId());
        return Response
                .status(Response.Status.CREATED)
                .entity(konverterTilTestscenarioDto(testscenario, testscenario.getTemplateNavn()))
                .build();
    }

    @DELETE
    @Path("/{id}")
    @ApiOperation(value = "", notes= "Sletter et initialisert testscenario som matcher id")
    public Response slettScenario(@PathParam(SCENARIO_ID) String id) {
        logger.info("Sletter testscenario med id: [{}]");
        if(testscenarioRepository.slettScenario(id)){
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }


    private TestscenarioDto konverterTilTestscenarioDto(Testscenario testscenario) {
        return konverterTilTestscenarioDto(testscenario, null, null);
    }

    private TestscenarioDto konverterTilTestscenarioDto(Testscenario testscenario, String templateNavn) {
        String templateKey = null;
        if (templateNavn != null) {
            templateKey = templateNavn.replaceFirst("[-_].+$", "");
        }
        return konverterTilTestscenarioDto(testscenario, templateKey, templateNavn);
    }

    private TestscenarioDto konverterTilTestscenarioDto(Testscenario testscenario, String templateKey, String templateName) {
        String fnrSøker = testscenario.getPersonopplysninger().getSøker().getIdent();
        String fnrAnnenPart = null;
        String aktørIdAnnenPart = null;
        if(testscenario.getPersonopplysninger().getAnnenPart() != null) {
            fnrAnnenPart = testscenario.getPersonopplysninger().getAnnenPart().getIdent();
            aktørIdAnnenPart = testscenario.getPersonopplysninger().getAnnenPart().getAktørIdent();

        }
        String aktørIdSøker = testscenario.getPersonopplysninger().getSøker().getAktørIdent();
        Optional<LocalDate> fødselsdato = fødselsdatoBarn(testscenario);
        TestscenarioPersonopplysningDto scenarioPersonopplysninger = new TestscenarioPersonopplysningDto(
                fnrSøker,
                fnrAnnenPart,
                aktørIdSøker,
                aktørIdAnnenPart,
                fødselsdato.orElse(null));

        TestscenariodataDto scenariodata = new TestscenariodataDto();
        if(testscenario.getSøkerInntektYtelse() != null) {
            ArbeidsforholdModell arbeidsforholdModell = testscenario.getSøkerInntektYtelse().getArbeidsforholdModell();
            InntektskomponentModell inntektskomponentModell = testscenario.getSøkerInntektYtelse().getInntektskomponentModell();
            scenariodata = new TestscenariodataDto(inntektskomponentModell, arbeidsforholdModell);
        }

        TestscenariodataDto scenariodataAnnenpart = null;
        if (testscenario.getAnnenpartInntektYtelse() != null) {
            ArbeidsforholdModell arbeidsforholdModellAnnenpart = testscenario.getAnnenpartInntektYtelse().getArbeidsforholdModell();
            InntektskomponentModell inntektskomponentModellAnnenpart = testscenario.getAnnenpartInntektYtelse().getInntektskomponentModell();
            scenariodataAnnenpart = new TestscenariodataDto(inntektskomponentModellAnnenpart, arbeidsforholdModellAnnenpart);
        }

        return new TestscenarioDto(
                templateKey,
                templateName,
                testscenario.getId(),
                testscenario.getVariabelContainer().getVars(),
                scenarioPersonopplysninger,
                scenariodata,
                scenariodataAnnenpart);

    }

    private Optional<LocalDate> fødselsdatoBarn(Testscenario testscenario) {
        Optional<BarnModell> barnModell = testscenario.getPersonopplysninger().getFamilierelasjoner()
                .stream()
                .filter(modell -> modell.getTil() instanceof BarnModell)
                .map(modell -> ((BarnModell) modell.getTil()))
                .findFirst();

        return barnModell.map(PersonModell::getFødselsdato);
    }

    private Map<String, String> getUserSuppliedVariables(MultivaluedMap<String, String> queryParameters, String... skipKeys) {
        Set<String> skipTheseKeys = new HashSet<>(Arrays.asList(skipKeys));
        Map<String, String> result = new LinkedHashMap<>();
        for (Map.Entry<String, List<String>> e : queryParameters.entrySet()) {
            if (skipTheseKeys.contains(e.getKey())) {
                continue; // tar inn som egen nøkkel, skipper her
            } else if (e.getValue().size() > 1 || e.getValue().isEmpty()) {
                continue; // støtter ikke multi-value eller tomme
            }
            result.put(e.getKey(), e.getValue().get(0));
        }
        return result;
    }

}
