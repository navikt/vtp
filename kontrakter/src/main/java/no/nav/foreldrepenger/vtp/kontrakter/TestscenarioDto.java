package no.nav.foreldrepenger.vtp.kontrakter;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.InntektYtelseModell;

@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public record TestscenarioDto(
        String templateKey,
        String templateNavn,
        @JsonProperty("id") String testscenarioId,
        @JsonInclude(content = Include.NON_EMPTY)  Map<String, String> variabler,
        TestscenarioPersonopplysningDto personopplysninger,
        @JsonProperty("scenariodata") InntektYtelseModell scenariodataDto,
        @JsonProperty("scenariodataAnnenpart") @JsonInclude(content = Include.NON_EMPTY) InntektYtelseModell scenariodataAnnenpartDto) {

}
