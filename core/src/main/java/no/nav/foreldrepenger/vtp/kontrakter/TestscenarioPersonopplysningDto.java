package no.nav.foreldrepenger.vtp.kontrakter;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @JsonProperty("barnIdenter")
    @JsonIgnoreProperties(ignoreUnknown = true)
    private List<String> barnIdenter;


    public TestscenarioPersonopplysningDto() {
        // Trenger denne for deserialisering i AT
    }

    public TestscenarioPersonopplysningDto(String søkerIdent, String søkerAktørIdent, String annenpartIdent,
                                           String annenpartAktørIdent, LocalDate fødselsdato, List<String> barnIdenter) {
        this.søkerIdent = søkerIdent;
        this.søkerAktørIdent = søkerAktørIdent;
        this.annenpartIdent = annenpartIdent;
        this.annenpartAktørIdent = annenpartAktørIdent;
        this.fødselsdato = fødselsdato;
        this.barnIdenter = barnIdenter;
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

    public List<String> getBarnIdenter() {
        return barnIdenter;
    }
}
