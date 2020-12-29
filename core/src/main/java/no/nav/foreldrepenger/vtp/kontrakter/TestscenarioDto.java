package no.nav.foreldrepenger.vtp.kontrakter;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.InntektYtelseModell;

public record TestscenarioDto(
        String templateKey,
        String templateNavn,
        @JsonProperty("id") String testscenarioId,
        Map<String, String> variabler,
        TestscenarioPersonopplysningDto personopplysninger,
        @JsonProperty("scenariodata") InntektYtelseModell scenariodataDto,
        @JsonProperty("scenariodataAnnenpart") InntektYtelseModell scenariodataAnnenpartDto) {
}
