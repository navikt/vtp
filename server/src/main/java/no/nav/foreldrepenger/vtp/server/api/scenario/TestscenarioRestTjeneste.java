package no.nav.foreldrepenger.vtp.server.api.scenario;

import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioTemplate;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioTemplateRepository;

@Api(tags = {"Testscenario"})
@Path("/api/testscenario")
public class TestscenarioRestTjeneste {

    private static final Logger LOG = LoggerFactory.getLogger(TestscenarioRestTjeneste.class);

    private static final String TEMPLATE_KEY = "key";

    @Context
    private TestscenarioTemplateRepository templateRepository;

    @Context
    private TestscenarioRepository testscenarioRepository;

    public void setTemplateRepository(TestscenarioTemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    public void setTestscenarioRepository(TestscenarioRepository testscenarioRepository) {
        this.testscenarioRepository = testscenarioRepository;
    }

    @GET
    @Path("/initialiserte")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "", notes = "Henter alle templates som er initiert i minnet til VTP", responseContainer = "List", response = TestscenarioDto.class)
    public List<TestscenarioDto> hentInitialiserteCaser() {
        Collection<Testscenario> testscenarios = testscenarioRepository.getTestscenarios();
        List<TestscenarioDto> testscenarioList = new ArrayList<>();

        testscenarios.forEach(testscenario -> {
            if (testscenario.getTemplateNavn() != null) {
                String templateKey = templateRepository.finnMedTemplatenavn(testscenario.getTemplateNavn()).getTemplateKey();
                testscenarioList.add(konverterTilTestscenarioDto(testscenario, templateKey, testscenario.getTemplateNavn()));
            } else {
                testscenarioList.add(konverterTilTestscenarioDto(testscenario, null, null));
            }
        });

        return testscenarioList;
    }

    @PUT
    @Path("/endrescenario/{id}")
    @ApiOperation(value="", notes="Patcher et testcase. Serialiserer uten sjekk, du må selv ha styr på typer")
    public Response endreScenario(TestscenarioDto testscenarioDto){
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @POST
    @Path("/{key}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "", notes = ("Initialiserer et test scenario basert på angitt template key i VTPs eksempel templates"), response = TestscenarioDto.class)
    public TestscenarioDto initialiserTestscenario(@PathParam(TEMPLATE_KEY) String templateKey, @Context UriInfo uriInfo) {

        TestscenarioTemplate template = templateRepository.finn(templateKey);
        Map<String, String> userSuppliedVariables = getUserSuppliedVariables(uriInfo.getQueryParameters(), TEMPLATE_KEY);
        Testscenario testscenario = testscenarioRepository.opprettTestscenario(template, userSuppliedVariables);

        return konverterTilTestscenarioDto(testscenario, templateKey);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "", notes = ("Initialiserer et testscenario basert på angitt json streng og returnerer det initialiserte objektet"), response = TestscenarioDto.class)
    public TestscenarioDto initialiserTestScenario(String testscenarioJson) {
        // getUserSuppliedVariables(uriInfo.getQueryParameters(), TEMPLATE_KEY) inkluderes ved behov.
        Testscenario testscenario = testscenarioRepository.opprettTestscenarioFraJsonString(testscenarioJson);
        return konverterTilTestscenarioDto(testscenario);
    }

    @DELETE
    @Path("/slettscenario/{id}")
    @ApiOperation(value = "", notes= "Sletter et initialisert testscenario")
    public Response slettScenario(@PathParam("id") String id) {
        if(testscenarioRepository.slettScenario(id)){
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }






    private TestscenarioDto konverterTilTestscenarioDto(Testscenario ts) {
        return konverterTilTestscenarioDto(ts, null, null);
    }

    private TestscenarioDto konverterTilTestscenarioDto(Testscenario ts, String templateKey) {
        String templateName = null;
        if (templateKey != null) {
            templateName = templateRepository.finn(templateKey).getTemplateNavn();
        }
        return konverterTilTestscenarioDto(ts, templateKey, templateName);
    }

    private TestscenarioDto konverterTilTestscenarioDto(Testscenario testscenario, String templateKey, String templateName) {
        String fnrSøker = testscenario.getPersonopplysninger().getSøker().getIdent();
        String fnrAnnenPart = testscenario.getPersonopplysninger().getAnnenPart().getIdent();
        String aktørIdSøker = testscenario.getPersonopplysninger().getSøker().getAktørIdent();
        String aktørIdAnnenPart = testscenario.getPersonopplysninger().getAnnenPart().getAktørIdent();
        Optional<LocalDate> fødselsdato = fødselsdatoBarn(testscenario);

        TestscenarioPersonopplysningDto scenarioPersonopplysninger = new TestscenarioPersonopplysningDto(fnrSøker, fnrAnnenPart, aktørIdSøker, aktørIdAnnenPart, fødselsdato.orElse(null));

        ArbeidsforholdModell arbeidsforholdModell = testscenario.getSøkerInntektYtelse().getArbeidsforholdModell();
        InntektskomponentModell inntektskomponentModell = testscenario.getSøkerInntektYtelse().getInntektskomponentModell();
        TestscenariodataDto scenariodata = new TestscenariodataDto(inntektskomponentModell, arbeidsforholdModell);

        TestscenariodataDto scenariodataAnnenpart = null;
        if (testscenario.getAnnenpartInntektYtelse() != null) {
            ArbeidsforholdModell arbeidsforholdModellAnnenpart = testscenario.getAnnenpartInntektYtelse().getArbeidsforholdModell();
            InntektskomponentModell inntektskomponentModellAnnenpart = testscenario.getAnnenpartInntektYtelse().getInntektskomponentModell();
            scenariodataAnnenpart = new TestscenariodataDto(inntektskomponentModellAnnenpart, arbeidsforholdModellAnnenpart);

        }

        return new TestscenarioDto(templateKey, templateName, testscenario.getId(), testscenario.getVariabelContainer().getVars(),
                scenarioPersonopplysninger, scenariodata, scenariodataAnnenpart);

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

    private int getTemplateKeyFromString(String s) {
        Matcher matcher = Pattern.compile("\\d+").matcher(s);
        matcher.find();
        int i = Integer.valueOf(matcher.group());

        return i;
    }
}
