package no.nav.foreldrepenger.fpmock2.server.api.scenario;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.arbeidsforhold.Arbeidsforhold;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.arbeidsforhold.ArbeidsforholdModell;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.inntektkomponent.InntektskomponentModell;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.Testscenario;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioRepository;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioTemplate;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioTemplateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Api(tags = { "Testscenario" })
@Path("/api/testscenario")
public class TestscenarioRestTjeneste {

    private static final Logger LOG = LoggerFactory.getLogger(TestscenarioRestTjeneste.class);

    private static final String TEMPLATE_KEY = "key";

    @Context
    private TestscenarioTemplateRepository templateRepository;

    @Context
    private TestscenarioRepository testscenarioRepository;

    @POST
    @Path("/{key}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "", notes = ("Initialiserer et test scenario basert på angitt template key"), response = TestscenarioDto.class)
    public TestscenarioDto initialiserTestscenario(@PathParam(TEMPLATE_KEY) String templateKey, @Context UriInfo uriInfo) {
        TestscenarioTemplate template = templateRepository.finn(templateKey);
        
        Map<String, String> userSuppliedVariables = getUserSuppliedVariables(uriInfo.getQueryParameters(), TEMPLATE_KEY);

        Testscenario testscenario = testscenarioRepository.opprettTestscenario(template, userSuppliedVariables);

        Map<String, String> variabler = testscenario.getVariabelContainer().getVars();
        
        String fnrSøker = testscenario.getPersonopplysninger().getSøker().getIdent();
        String fnrAnnenPart = testscenario.getPersonopplysninger().getAnnenPart().getIdent();
        String aktørIdSøker = testscenario.getPersonopplysninger().getSøker().getAktørIdent();
        TestscenarioPersonopplysningDto scenarioPersonopplysninger = new TestscenarioPersonopplysningDto(fnrSøker ,fnrAnnenPart, aktørIdSøker);

        LOG.info("Testscenario med templateKey: {}, for søker: {}, med aktørId: {}.", templateKey, fnrSøker, aktørIdSøker);

        ArbeidsforholdModell arbeidsforholdModell = testscenario.getSøkerInntektYtelse().getArbeidsforholdModell();
        InntektskomponentModell inntektskomponentModell = testscenario.getSøkerInntektYtelse().getInntektskomponentModell();
        TestscenariodataDto scenariodata = new TestscenariodataDto(inntektskomponentModell, arbeidsforholdModell);

        return new TestscenarioDto(template, testscenario.getId(), variabler, scenarioPersonopplysninger, scenariodata);
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
