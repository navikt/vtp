package no.nav.foreldrepenger.vtp.kontrakter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true) 
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public record TestscenarioPersonopplysningDto(
                                              @JsonProperty("søkerIdent") String søkerIdent,
                                              @JsonProperty("søkerAktørIdent") String søkerAktørIdent,
                                              @JsonProperty("annenpartIdent") String annenpartIdent,
                                              @JsonProperty("annenpartAktørIdent") String annenpartAktørIdent,
                                              @JsonProperty("fødselsdato") LocalDate fødselsdato,
                                              @JsonProperty("barnIdenter") List<String> barnIdenter,
                                              @JsonProperty("barnIdentAktørId") Map<String, String> barnIdentAktørId) {
}
