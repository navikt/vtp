package no.nav.foreldrepenger.fpmock2.server.api.scenario;

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
import no.nav.foreldrepenger.fpmock2.kontrakter.TestscenarioDto;
import no.nav.foreldrepenger.fpmock2.kontrakter.TestscenarioPersonopplysningDto;
import no.nav.foreldrepenger.fpmock2.kontrakter.TestscenariodataDto;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.arbeidsforhold.ArbeidsforholdModell;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.inntektkomponent.InntektskomponentModell;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.BarnModell;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.PersonModell;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.Testscenario;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioRepository;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioTemplate;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioTemplateRepository;

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


    @PUT
    @Path("/endrescenario/{id}")
    @ApiOperation(value="", notes="Patcher et testcase. Serialiserer uten sjekk, du må selv ha styr på typer")
    public Response endreScenario(TestscenarioDto testscenarioDto){


        return Response.ok().build();
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

    @GET
    @Path("/initialiserte")
    @ApiOperation(value = "", notes = "Henter alle templates som er initiert i minnet til VTP", responseContainer = "List", response = TestscenarioDto.class)
    public List<TestscenarioDto> hentInitialiserteCaser() {

        Collection<Testscenario> testscenarios = this.testscenarioRepository.getTestscenarios();
        List<TestscenarioDto> testList = new ArrayList<>();

        testscenarios.stream().forEach(ts -> {
            testList.add(konverterTilTestscenarioDto(ts));

        });

        return testList;
    }


    @POST
    @Path("/{key}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "", notes = ("Initialiserer et test scenario basert på angitt template key"), response = TestscenarioDto.class)
    public TestscenarioDto initialiserTestscenario(@PathParam(TEMPLATE_KEY) String templateKey, @Context UriInfo uriInfo) {

        TestscenarioTemplate template = templateRepository.finn(templateKey);
        Map<String, String> userSuppliedVariables = getUserSuppliedVariables(uriInfo.getQueryParameters(), TEMPLATE_KEY);
        Testscenario testscenario = testscenarioRepository.opprettTestscenario(template, userSuppliedVariables);

        return konverterTilTestscenarioDto(testscenario, templateKey);
    }

    private TestscenarioDto konverterTilTestscenarioDto(Testscenario ts) {
        return konverterTilTestscenarioDto(ts, null);
    }

    private TestscenarioDto konverterTilTestscenarioDto(Testscenario ts, String templateKey) {
        TestscenarioTemplate template;
        if (templateKey == null) {
            template = templateRepository.finnMedTemplatenavn(ts.getTemplateNavn());
        } else {
            template = templateRepository.finn(templateKey);
        }

        String fnrSøker = ts.getPersonopplysninger().getSøker().getIdent();
        String fnrAnnenPart = ts.getPersonopplysninger().getAnnenPart().getIdent();
        String aktørIdSøker = ts.getPersonopplysninger().getSøker().getAktørIdent();
        String aktørIdAnnenPart = ts.getPersonopplysninger().getAnnenPart().getAktørIdent();
        Optional<LocalDate> fødselsdato = fødselsdatoBarn(ts);

        TestscenarioPersonopplysningDto scenarioPersonopplysninger = new TestscenarioPersonopplysningDto(fnrSøker, fnrAnnenPart, aktørIdSøker, aktørIdAnnenPart, fødselsdato.orElse(null));

        ArbeidsforholdModell arbeidsforholdModell = ts.getSøkerInntektYtelse().getArbeidsforholdModell();
        InntektskomponentModell inntektskomponentModell = ts.getSøkerInntektYtelse().getInntektskomponentModell();
        TestscenariodataDto scenariodata = new TestscenariodataDto(inntektskomponentModell, arbeidsforholdModell);

        TestscenariodataDto scenariodataAnnenpart = null;
        if (ts.getAnnenpartInntektYtelse() != null) {
            ArbeidsforholdModell arbeidsforholdModellAnnenpart = ts.getAnnenpartInntektYtelse().getArbeidsforholdModell();
            InntektskomponentModell inntektskomponentModellAnnenpart = ts.getAnnenpartInntektYtelse().getInntektskomponentModell();
            scenariodataAnnenpart = new TestscenariodataDto(inntektskomponentModellAnnenpart, arbeidsforholdModellAnnenpart);

        }

        return new TestscenarioDto(template, ts.getId(), ts.getVariabelContainer().getVars(), scenarioPersonopplysninger, scenariodata, scenariodataAnnenpart);

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
