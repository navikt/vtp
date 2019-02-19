package no.nav.foreldrepenger.fpmock2.kontrakter;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonProperty;

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

    }


    public TestscenarioPersonopplysningDto(String søkerIdent, String annenpartIdent, String søkerAktørIdent, String annenpartAktørIdent,LocalDate fødselsdato) {
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
