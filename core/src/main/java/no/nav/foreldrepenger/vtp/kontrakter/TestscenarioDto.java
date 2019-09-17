package no.nav.foreldrepenger.vtp.kontrakter;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioTemplate;

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
    private TestscenariodataDto scenariodataAnnenpartDto;

    public TestscenarioDto() {
        super(null, null);
    }

    public TestscenarioDto(
            TestscenarioTemplate template,
            String testscenarioId,
            Map<String, String> variabler,
            TestscenarioPersonopplysningDto scenarioPersonopplysninger,
            TestscenariodataDto scenariodataDto,
            TestscenariodataDto scenariodataAnnenpartDto
    ) {
        super(testscenarioId, template.getTemplateKey());
        this.personopplysninger = scenarioPersonopplysninger;
        Objects.requireNonNull(variabler, "variabler");
        this.template = template;
        this.variabler = variabler;
        this.scenariodataDto = scenariodataDto;
        this.scenariodataAnnenpartDto = scenariodataAnnenpartDto;
        this.setTemplateNavn(template.getTemplateNavn());
    }

    public TestscenarioPersonopplysningDto getPersonopplysninger() {
        return personopplysninger;
    }

    public Map<String, String> getVariabler() {
        return Collections.unmodifiableMap(variabler);
    }

    public TestscenariodataDto getScenariodata() {
        return scenariodataDto;
    }

    public TestscenariodataDto getScenariodataAnnenpart() {
        return scenariodataAnnenpartDto;
    }
}
