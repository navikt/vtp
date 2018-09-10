package no.nav.foreldrepenger.fpmock2.server.api.scenario;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public class TestscenarioPersonopplysningDto {

    @JsonProperty("søkerIdent")
    private String søkerIdent;

    @JsonProperty("annenpartIdent")
    private String annenpartIdent;

    public TestscenarioPersonopplysningDto(String søkerIdent, String annenpartIdent) {
        this.søkerIdent = søkerIdent;
        this.annenpartIdent = annenpartIdent;
    }

    public String getSøkerIdent() {
        return søkerIdent;
    }

    public String getAnnenpartIdent() {
        return annenpartIdent;
    }

}
