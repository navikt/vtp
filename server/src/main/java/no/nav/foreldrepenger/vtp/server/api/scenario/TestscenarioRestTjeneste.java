package no.nav.foreldrepenger.vtp.server.api.scenario;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import no.nav.foreldrepenger.vtp.kontrakter.TestscenarioDto;
import no.nav.foreldrepenger.vtp.kontrakter.TestscenarioPersonopplysningDto;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.BarnModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.BrukerModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.FamilierelasjonModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.Testscenario;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioRepository;
import no.nav.foreldrepenger.vtp.testmodell.util.JacksonObjectMapperTestscenario;
import no.nav.foreldrepenger.vtp.testmodell.util.JacksonWrapper;

@Tag(name = "Testscenario")
@Path("/api/testscenarios")
public class TestscenarioRestTjeneste {
    private static final Logger logger = LoggerFactory.getLogger(TestscenarioRestTjeneste.class);
    private static final JacksonWrapper mapper = new JacksonWrapper(JacksonObjectMapperTestscenario.getObjectMapper());

    private static final String TEMPLATE_KEY = "key";
    private static final String SCENARIO_ID = "id";

    @Context
    private TestscenarioRepository testscenarioRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Henter alle testcaser som er initiert i minnet til VTP", responses = {
            @ApiResponse(responseCode = "OK", content = @Content(schema = @Schema(implementation  = TestscenarioDto[].class))),
    })
    public Response hentInitialiserteCaser() {
        Map<String, Testscenario> testscenarios = testscenarioRepository.getTestscenarios();
        List<TestscenarioDto> testscenarioList = new ArrayList<>();

        testscenarios.forEach((key, testscenario) -> {
            if (testscenario.getTemplateNavn() != null) {
                testscenarioList.add(konverterTilTestscenarioDto(testscenario, testscenario.getTemplateNavn()));
            } else {
                testscenarioList.add(konverterTilTestscenarioDto(testscenario));
            }
        });

        return Response.status(Response.Status.OK)
            .entity(mapper.writeValueAsString(testscenarioList))
            .build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Returnerer testscenario som matcher id", responses = {
            @ApiResponse(responseCode = "OK", content = @Content(schema = @Schema(implementation  = TestscenarioDto.class))),
    })
    public Response hentScenario(@PathParam(SCENARIO_ID) String id) {
        if (testscenarioRepository.getTestscenario(id) != null) {
            Testscenario testscenario = testscenarioRepository.getTestscenario(id);
            return Response.status(Response.Status.OK)
                .entity(mapper.writeValueAsString(konverterTilTestscenarioDto(testscenario, testscenario.getTemplateNavn())))
                .build();
        } else {
            return Response.status(Response.Status.NO_CONTENT).build();
        }

    }

    @SuppressWarnings("unused")
    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Oppdaterer hele scenario som matcher id", responses = {
            @ApiResponse(responseCode = "OK", content = @Content(schema = @Schema(implementation  = TestscenarioDto.class))),
    })
    public Response oppdaterHeleScenario(@PathParam(SCENARIO_ID) String id, String testscenarioJson, @Context UriInfo uriInfo) {
        Map<String, String> userSuppliedVariables = getUserSuppliedVariables(uriInfo.getQueryParameters(), TEMPLATE_KEY);
        Testscenario testscenario = testscenarioRepository.oppdaterTestscenario(id, testscenarioJson, userSuppliedVariables);
        logger.info("Oppdaterer testscenario med id {} med ekstern testdatadefinisjon.", testscenario.getId());
        return Response.status(Response.Status.OK)
                .entity(mapper.writeValueAsString(konverterTilTestscenarioDto(testscenario, testscenario.getTemplateNavn())))
                .build();
    }

    @SuppressWarnings("unused")
    @PATCH
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Oppdaterer deler av et scenario som matcher id", responses = {
            @ApiResponse(responseCode = "OK", content = @Content(schema = @Schema(implementation  = TestscenarioDto.class))),
    })
    public Response endreScenario(@PathParam(SCENARIO_ID) String id, String patchArray) {
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Initialiserer et testscenario basert på angitt json streng og returnerer det initialiserte objektet", responses = {
            @ApiResponse(responseCode = "OK", content = @Content(schema = @Schema(implementation  = TestscenarioDto.class))),
    })
    public Response initialiserTestScenario(String testscenarioJson, @Context UriInfo uriInfo) {
        Map<String, String> userSuppliedVariables = getUserSuppliedVariables(uriInfo.getQueryParameters(), TEMPLATE_KEY);
        Testscenario testscenario = testscenarioRepository.opprettTestscenario(testscenarioJson, userSuppliedVariables);
        logger.info("Initialiserer testscenario med ekstern testdatadefinisjon. Opprettet med id: [{}] ", testscenario.getId());
        return Response.status(Response.Status.CREATED)
            .entity(mapper.writeValueAsString(konverterTilTestscenarioDto(testscenario, testscenario.getTemplateNavn())))
            .build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(description = "Sletter et initialisert testscenario som matcher id")
    public Response slettScenario(@PathParam(SCENARIO_ID) String id) {
        logger.info("Sletter testscenario med id: [{}]", id);
        if (Boolean.TRUE.equals(testscenarioRepository.slettScenario(id))) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    public static TestscenarioDto konverterTilTestscenarioDto(Testscenario testscenario) {
        return konverterTilTestscenarioDto(testscenario, null, null);
    }

    private static TestscenarioDto konverterTilTestscenarioDto(Testscenario testscenario, String templateNavn) {
        String templateKey = null;
        if (templateNavn != null) {
            templateKey = templateNavn.replaceFirst("[-_].+$", "");
        }
        return konverterTilTestscenarioDto(testscenario, templateKey, templateNavn);
    }

    private static TestscenarioDto konverterTilTestscenarioDto(Testscenario testscenario, String templateKey, String templateName) {
        String fnrSøker = testscenario.getPersonopplysninger().getSøker().getIdent();
        String fnrAnnenPart = null;
        String aktørIdAnnenPart = null;
        BrukerModell.Kjønn kjønnAnnenpart = null;
        if (testscenario.getPersonopplysninger().getAnnenPart() != null) {
            fnrAnnenPart = testscenario.getPersonopplysninger().getAnnenPart().getIdent();
            aktørIdAnnenPart = testscenario.getPersonopplysninger().getAnnenPart().getAktørIdent();
            kjønnAnnenpart = testscenario.getPersonopplysninger().getAnnenPart().getKjønn();
        }
        String aktørIdSøker = testscenario.getPersonopplysninger().getSøker().getAktørIdent();
        Optional<LocalDate> fødselsdato = fødselsdatoBarn(testscenario);
        var barnIdentTilAktørId = barnidenter(testscenario);

        // TODO: fjern fnrBarn, bruk bare barnIdentTilAktørId
        List<String> fnrBarn = List.copyOf(barnIdentTilAktørId.keySet());

        TestscenarioPersonopplysningDto scenarioPersonopplysninger = new TestscenarioPersonopplysningDto(
            fnrSøker,
            aktørIdSøker,
            testscenario.getPersonopplysninger().getSøker().getKjønn(),
            fnrAnnenPart,
            aktørIdAnnenPart,
            kjønnAnnenpart,
            fødselsdato.orElse(null),
            fnrBarn,
            barnIdentTilAktørId);

        InntektYtelseModell søkerInntektYtelse = testscenario.getSøkerInntektYtelse();
        InntektYtelseModell annenpartInntektYtelse = testscenario.getAnnenpartInntektYtelse();

        return new TestscenarioDto(
            templateKey,
            templateName,
            testscenario.getId(),
            testscenario.getVariabelContainer().getVars(),
            scenarioPersonopplysninger,
            søkerInntektYtelse,
            annenpartInntektYtelse);
    }

    private static Optional<LocalDate> fødselsdatoBarn(Testscenario testscenario) {
        Optional<BarnModell> barnModell = testscenario.getPersonopplysninger().getFamilierelasjoner()
            .stream()
            .filter(modell -> modell.getTil() instanceof BarnModell)
            .map(modell -> ((BarnModell) modell.getTil()))
            .findFirst();

        return barnModell.map(PersonModell::getFødselsdato);
    }

    private static Map<String, String> barnidenter(Testscenario testscenario) {
        return testscenario.getPersonopplysninger().getFamilierelasjoner()
            .stream()
            .filter(modell -> modell.getTil() instanceof BarnModell)
            .map(FamilierelasjonModell::getTil)
            .sorted(Comparator.comparing(BrukerModell::getIdent))
            .map(p -> Map.entry(p.getIdent(), p.getAktørIdent()))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    private Map<String, String> getUserSuppliedVariables(MultivaluedMap<String, String> queryParameters, String... skipKeys) {
        Set<String> skipTheseKeys = new HashSet<>(Arrays.asList(skipKeys));
        Map<String, String> result = new LinkedHashMap<>();
        for (Map.Entry<String, List<String>> e : queryParameters.entrySet()) {
            if (skipTheseKeys.contains(e.getKey()) || e.getValue().size() != 1) {
                continue; // tar inn som egen nøkkel, skipper her OG støtter ikke multi-value eller tomme
            }
            result.put(e.getKey(), e.getValue().get(0));
        }
        return result;
    }

}
