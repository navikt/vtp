package no.nav.foreldrepenger.vtp.autotest.scenario;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public class TestscenarioPersonopplysningDto {

    @JsonProperty("søkerIdent")
    private String søkerIdent;

    @JsonProperty("søkerAktørIdent")
    private String søkerAktørIdent;

    @JsonProperty("annenpartIdent")
    private String annenpartIdent;

    @JsonProperty("annenpartAktørIdent")
    private String annenpartAktørIdent;

    @JsonProperty("fødselsdato")
    private LocalDate fødselsdato;


    public TestscenarioPersonopplysningDto() {
        // Trenger denne for deserialisering i AT
    }

    public TestscenarioPersonopplysningDto(String søkerIdent, String annenpartIdent, String søkerAktørIdent, String annenpartAktørIdent, LocalDate fødselsdato) {
        this.søkerIdent = søkerIdent;
        this.annenpartIdent = annenpartIdent;
        this.søkerAktørIdent = søkerAktørIdent;
        this.annenpartAktørIdent = annenpartAktørIdent;
        this.fødselsdato = fødselsdato;
    }

    public String getSøkerIdent() {
        return søkerIdent;
    }

    public String getAnnenpartIdent() {
        return annenpartIdent;
    }

    public String getSøkerAktørIdent() {
        return søkerAktørIdent;
    }

    public String getAnnenPartAktørIdent() {
        return annenpartAktørIdent;
    }

    public LocalDate getFødselsdato() {
        return fødselsdato;
    }
}
