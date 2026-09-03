package no.nav.foreldrepenger.vtp.kontrakter.person.v2;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import tools.jackson.databind.json.JsonMapper;

class PersonDtoTest {

    private static final JsonMapper MAPPER = JsonMapper.builder().build();

    @Test
    void skalRundtrippeRegistrerteNæringsvirksomheter() {
        var virksomhet = new RegistrertNæringsvirksomhetDto(
                "999999999",
                "VTP FISKE",
                "ENK",
                "Enkeltpersonforetak",
                "03.110",
                "Hav- og kystfiske");
        var person = PersonDto.builder()
                .registrerteNæringsvirksomheter(List.of(virksomhet))
                .build();

        var json = MAPPER.writeValueAsString(person);
        var rundtrippet = MAPPER.readValue(json, PersonDto.class);

        assertThat(rundtrippet.registrerteNæringsvirksomheter()).containsExactly(virksomhet);
    }

    @Test
    void skalGiTomListeNårFeltetMangler() {
        var person = MAPPER.readValue("{}", PersonDto.class);

        assertThat(person.registrerteNæringsvirksomheter()).isEmpty();
    }

    @Test
    void skalGiTomListeNårFeltetErNull() {
        var person = MAPPER.readValue("{\"registrerteNæringsvirksomheter\":null}", PersonDto.class);

        assertThat(person.registrerteNæringsvirksomheter()).isEmpty();
    }

    @Test
    void beholderFemargumentskonstruktør() {
        var person = new PersonDto(null, null, null, null, null);

        assertThat(person.registrerteNæringsvirksomheter()).isEmpty();
    }
}
