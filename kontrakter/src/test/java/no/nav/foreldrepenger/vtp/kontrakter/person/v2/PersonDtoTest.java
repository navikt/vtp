package no.nav.foreldrepenger.vtp.kontrakter.person.v2;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import tools.jackson.databind.json.JsonMapper;

class PersonDtoTest {

    private static final JsonMapper MAPPER = JsonMapper.builder().build();

    @Test
    void skalRundtrippeBrregData() {
        var virksomhet = new BrregDto.VirksomhetDto(
                "999999999",
                "VTP FISKE",
                "ENK",
                "Enkeltpersonforetak",
                "03.110",
                "Hav- og kystfiske");
        var person = PersonDto.builder()
                .brreg(new BrregDto(List.of(virksomhet)))
                .build();

        var json = MAPPER.writeValueAsString(person);
        var rundtrippet = MAPPER.readValue(json, PersonDto.class);

        assertThat(rundtrippet.brreg().virksomheter()).containsExactly(virksomhet);
    }

    @Test
    void skalGiTomBrregDataNårFeltetMangler() {
        var person = MAPPER.readValue("{}", PersonDto.class);

        assertThat(person.brreg().virksomheter()).isEmpty();
    }

    @Test
    void skalGiTomBrregDataNårFeltEllerListeErNull() {
        var personMedNullFelt = MAPPER.readValue("{\"brreg\":null}", PersonDto.class);
        var personMedNullListe = MAPPER.readValue("{\"brreg\":{\"virksomheter\":null}}", PersonDto.class);

        assertThat(personMedNullFelt.brreg().virksomheter()).isEmpty();
        assertThat(personMedNullListe.brreg().virksomheter()).isEmpty();
    }
}
