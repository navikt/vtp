package no.nav.vtp.person;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import no.nav.vtp.PersonBuilder;
import no.nav.vtp.person.ident.Orgnummer;
import no.nav.vtp.person.næring.RegistrertNæringsvirksomhet;

class PersonRepositoryTest {

    @Test
    void registrertNæringsvirksomhetErTilgjengeligForOrganisasjonsoppslag() {
        var virksomhet = new RegistrertNæringsvirksomhet(
                "999999999", "VTP FISKE", "ENK", "Enkeltpersonforetak", "03.110", "Hav- og kystfiske");
        var person = PersonBuilder.lagSøker().tilBuilder()
                .medRegistrerteNæringsvirksomheter(List.of(virksomhet))
                .build();
        PersonRepository.leggTilPerson(person);

        var organisasjon = PersonRepository.hentInformasjonOmArbeidsforhold(
                new Orgnummer(virksomhet.organisasjonsnummer()));

        assertThat(organisasjon).get().satisfies(org -> {
            assertThat(org.orgnummer().value()).isEqualTo("999999999");
            assertThat(org.informasjon().navn()).isEqualTo("VTP FISKE");
        });
    }
}
