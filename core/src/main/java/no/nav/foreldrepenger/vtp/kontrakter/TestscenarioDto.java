package no.nav.foreldrepenger.vtp.kontrakter;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.Map;

@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public class TestscenarioDto extends TestscenarioReferanse {

    @JsonProperty("personopplysninger")
    private TestscenarioPersonopplysningDto personopplysninger;

    @JsonInclude(content = Include.NON_EMPTY)
    @JsonProperty("variabler")
    private Map<String, String> variabler;

    @JsonProperty("scenariodata")
    private TestscenariodataDto scenariodataDto;

    @JsonInclude(content = Include.NON_EMPTY)
    @JsonProperty("scenariodataAnnenpart")
    private TestscenariodataDto scenariodataAnnenpartDto;

    public TestscenarioDto() {
        super(null, null);
    }

    public TestscenarioDto(
            String templateKey,
            String templateName,
            String testscenarioId,
            Map<String, String> variabler,
            TestscenarioPersonopplysningDto testscenarioPersonopplysningDto,
            TestscenariodataDto scenariodataDto,
            TestscenariodataDto scenariodataAnnenpartDto
    ) {
        super(testscenarioId, templateKey);
        this.personopplysninger = testscenarioPersonopplysningDto;
        this.variabler = variabler;
        this.scenariodataDto = scenariodataDto;
        this.scenariodataAnnenpartDto = scenariodataAnnenpartDto;
        this.setTemplateNavn(templateName);
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
