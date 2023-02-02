package no.nav.foreldrepenger.vtp.kontrakter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import no.nav.foreldrepenger.vtp.testmodell.personopplysning.BrukerModell;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public record TestscenarioPersonopplysningDto(String søkerIdent,
                                              String søkerAktørIdent,
                                              BrukerModell.Kjønn søkerKjønn,
                                              String annenpartIdent,
                                              String annenpartAktørIdent,
                                              BrukerModell.Kjønn annenpartKjønn,
                                              LocalDate fødselsdato,
                                              List<String> barnIdenter,
                                              Map<String, String> barnIdentAktørId) {
}
