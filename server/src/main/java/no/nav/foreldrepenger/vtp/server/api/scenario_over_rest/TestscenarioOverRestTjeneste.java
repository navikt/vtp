package no.nav.foreldrepenger.vtp.server.api.scenario_over_rest;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.foreldrepenger.vtp.kontrakter.TestscenarioDto;
import no.nav.foreldrepenger.vtp.kontrakter.TestscenarioPersonopplysningDto;
import no.nav.foreldrepenger.vtp.kontrakter.TestscenariodataDto;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold.Arbeidsforhold;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold.ArbeidsforholdModell;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.inntektkomponent.InntektskomponentModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.BarnModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.Testscenario;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.time.LocalDate;
import java.util.Optional;

@Api(tags = {"Testscenario-test"})
@Path("/api/testscenario/test")
public class TestscenarioOverRestTjeneste {

    private static final Logger LOG = LoggerFactory.getLogger(TestscenarioOverRestTjeneste.class);

    @Context
    private TestscenarioRepository testscenarioRepository;


    // TODO(EW) getUserSuppliedVariables(uriInfo.getQueryParameters(), TEMPLATE_KEY) inkluderes ved behov.
    @POST
    @Path("/initialiser")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "", notes = ("Initialiserer et testscenario basert på angitt json streng"), response = TestscenarioDto.class)
    public TestscenarioDto initialiserTestScenario(String testscenarioJson) {
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

