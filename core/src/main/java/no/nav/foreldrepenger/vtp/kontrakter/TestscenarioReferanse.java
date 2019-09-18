package no.nav.foreldrepenger.vtp.kontrakter;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public class TestscenarioReferanse {

    @JsonProperty("templateKey")
    private String templateKey;

    @JsonProperty("templateNavn")
    private String templateNavn;

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

    public String getTemplateNavn() {
        return templateNavn;
    }

    public void setTemplateNavn(String templateNavn) {
        this.templateNavn = templateNavn;
    }
}
