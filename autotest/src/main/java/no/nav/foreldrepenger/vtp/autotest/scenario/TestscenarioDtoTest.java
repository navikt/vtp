package no.nav.foreldrepenger.vtp.autotest.scenario;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.foreldrepenger.vtp.testmodell.organisasjon.OrganisasjonModeller;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.Personopplysninger;
import no.nav.foreldrepenger.vtp.testmodell.virksomhet.ScenarioVirksomheter;

import java.util.Map;

@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public class TestscenarioDtoTest {

    @JsonProperty("templateNavn")
    private final String templateNavn;

    /** Unik testscenario id. */
    @JsonProperty("id")
    private String id;

    @JsonProperty("personopplysninger")
    private Personopplysninger personopplysninger;

    @JsonProperty("søkerInntektYtelse")
    private InntektYtelseModell søkerInntektYtelse;

    @JsonInclude(content = JsonInclude.Include.NON_EMPTY)
    @JsonProperty("annenpartInntektYtelse")
    private InntektYtelseModell annenpartInntektYtelse;

    @JsonProperty("organisasjonModeller")
    private OrganisasjonModeller organisasjonModeller;


    @JsonInclude(content = JsonInclude.Include.NON_EMPTY)
    @JsonProperty("variabler")
    private Map<String, String> variabler;

    public TestscenarioDtoTest(String templateNavn,
                               Personopplysninger personopplysninger,
                               InntektYtelseModell søkerInntektYtelse,
                               InntektYtelseModell annenpartInntektYtelse,
                               ScenarioVirksomheter scenarioVirksomheter,
                               String id,
                               Map<String, String> variabler) {
        this.templateNavn = templateNavn;
        this.personopplysninger = personopplysninger;
        this.søkerInntektYtelse = søkerInntektYtelse;
        this.annenpartInntektYtelse = annenpartInntektYtelse;
        this.id = id;
        this.variabler = variabler;
    }
}
