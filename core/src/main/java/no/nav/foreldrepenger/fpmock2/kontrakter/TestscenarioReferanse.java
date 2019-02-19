package no.nav.foreldrepenger.fpmock2.kontrakter;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public class TestscenarioReferanse {

    @JsonProperty("templateKey")
    private String templateKey;

    @JsonProperty("id")
    private String testscenarioId;

    public TestscenarioReferanse(String testscenarioId, String templateKey) {
        this.testscenarioId = testscenarioId;
        this.templateKey = templateKey;
    }

    public String getTemplateKey() {
        return templateKey;
    }

    public String getTestscenarioId() {
        return testscenarioId;
    }

}
