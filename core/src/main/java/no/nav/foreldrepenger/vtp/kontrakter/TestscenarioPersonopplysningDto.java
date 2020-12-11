package no.nav.foreldrepenger.vtp.kontrakter;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public record TestscenarioPersonopplysningDto(
        @JsonProperty("søkerIdent") String søkerIdent,
        @JsonProperty("søkerAktørIdent") String søkerAktørIdent,
        @JsonProperty("annenpartIdent") String annenpartIdent,
        @JsonProperty("annenpartAktørIdent") String annenpartAktørIdent,
        @JsonProperty("fødselsdato") LocalDate fødselsdato,
        @JsonProperty("barnIdenter") @JsonIgnoreProperties(ignoreUnknown = true) List<String> barnIdenter) {
}
