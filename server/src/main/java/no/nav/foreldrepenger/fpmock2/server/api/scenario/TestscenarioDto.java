package no.nav.foreldrepenger.fpmock2.server.api.scenario;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioTemplate;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public class TestscenarioDto extends TestscenarioReferanse {

    @JsonProperty("personopplysninger")
    private TestscenarioPersonopplysningDto personopplysninger;

    @JsonInclude(content = Include.NON_EMPTY)
    @JsonProperty("variabler")
    private Map<String, String> variabler;

    @JsonIgnore
    @JsonProperty("template")
    private TestscenarioTemplate template;

    @JsonProperty("scenariodata")
    private TestscenariodataDto scenariodataDto;

    @JsonInclude(content = Include.NON_EMPTY)
    @JsonProperty("scenariodataAnnenpart")
    private TestscenariodataAnnenpartDto scenariodataAnnenpartDto;
    
    public TestscenarioDto() {
        super(null,  null);
    }

    public TestscenarioDto(TestscenarioTemplate template, String testscenarioId, Map<String, String> variabler,
                           TestscenarioPersonopplysningDto scenarioPersonopplysninger, TestscenariodataDto scenariodataDto,
                            TestscenariodataAnnenpartDto scenariodataAnnenpartDto) {
        super(testscenarioId, template.getTemplateKey());
        this.personopplysninger = scenarioPersonopplysninger;
        Objects.requireNonNull(variabler, "variabler");
        this.template = template;
        this.variabler = variabler;
        this.scenariodataDto = scenariodataDto;
        this.scenariodataAnnenpartDto = scenariodataAnnenpartDto;
    }

    public TestscenarioPersonopplysningDto getPersonopplysninger() {
        return personopplysninger;
    }

    public Map<String, String> getVariabler() {
        return Collections.unmodifiableMap(variabler);
    }

    public TestscenariodataDto getScenariodata() { return scenariodataDto;}

    public TestscenariodataAnnenpartDto getScenariodataAnnenpart() { return scenariodataAnnenpartDto;}
}
