package no.nav.foreldrepenger.vtp.kontrakter.person.v2;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PersonDtoTest {

    @Test
    void beholderFemargumentskonstruktør() {
        var person = new PersonDto(null, null, null, null, null);

        assertThat(person.registrerteNæringsvirksomheter()).isEmpty();
    }
}
