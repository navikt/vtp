package no.nav.foreldrepenger.vtp.kontrakter.person;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import tools.jackson.databind.json.JsonMapper;

class InntektYtelseModellDtoTest {

    private static final JsonMapper MAPPER = JsonMapper.builder().build();

    @Test
    void skalRundtrippeBrregData() {
        var virksomhet = new BrregDto.VirksomhetDto(
            "999999999",
            "VTP FISKE",
            "ENK",
            "Enkeltpersonforetak",
            "03.110",
            "Hav- og kystfiske"
        );
        var modell = InntektYtelseModellDto.builder()
            .brreg(new BrregDto(List.of(virksomhet)))
            .build();

        var json = MAPPER.writeValueAsString(modell);
        var rundtrippet = MAPPER.readValue(json, InntektYtelseModellDto.class);

        assertThat(rundtrippet.brreg().virksomheter()).containsExactly(virksomhet);
    }

    @Test
    void skalGiTomBrregDataNårFeltetMangler() {
        var modell = MAPPER.readValue("{}", InntektYtelseModellDto.class);

        assertThat(modell.brreg().virksomheter()).isEmpty();
    }

    @Test
    void skalGiTomBrregDataNårFeltEllerListeErNull() {
        var modellMedNullFelt = MAPPER.readValue("{\"brreg\":null}", InntektYtelseModellDto.class);
        var modellMedNullListe = MAPPER.readValue("{\"brreg\":{\"virksomheter\":null}}", InntektYtelseModellDto.class);

        assertThat(modellMedNullFelt.brreg().virksomheter()).isEmpty();
        assertThat(modellMedNullListe.brreg().virksomheter()).isEmpty();
    }
}
