package no.nav.vtp.person;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import no.nav.vtp.person.næring.RegistrertNæringsvirksomhet;

class PersonTest {

    private static final RegistrertNæringsvirksomhet VIRKSOMHET = new RegistrertNæringsvirksomhet(
            "999999999", "VTP FISKE", "ENK", "Enkeltpersonforetak", "03.110", "Hav- og kystfiske");

    @Test
    void femArgumentKonstruktørGirTomListeMedRegistrerteNæringsvirksomheter() {
        var person = new Person(null, List.of(), List.of(), List.of(), List.of());

        assertThat(person.registrerteNæringsvirksomheter()).isEmpty();
    }

    @Test
    void builderBevarerRegistrerteNæringsvirksomheterVedAlleMutasjoner() {
        var person = new Person(null, List.of(), List.of(), List.of(), List.of(), List.of(VIRKSOMHET));

        var endret = person.tilBuilder()
                .medPersonopplysninger(null)
                .medArbeidsforhold(List.of())
                .medInntekt(List.of())
                .medYtelser(List.of())
                .medSkatteopplysninger(List.of())
                .build();

        assertThat(endret.registrerteNæringsvirksomheter()).containsExactly(VIRKSOMHET);
    }

    @Test
    void registrerteNæringsvirksomheterNormalisererNullTilTomListe() {
        var person = new Person(null, List.of(), List.of(), List.of(), List.of(), null);

        assertThat(person.registrerteNæringsvirksomheter()).isEmpty();
        assertThat(person.tilBuilder().medRegistrerteNæringsvirksomheter(null).build().registrerteNæringsvirksomheter()).isEmpty();
    }
}
