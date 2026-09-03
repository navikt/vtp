package no.nav.vtp.brreg;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import no.nav.vtp.PersonBuilder;
import no.nav.vtp.person.PersonRepository;
import no.nav.vtp.person.næring.RegistrertNæringsvirksomhet;

class BrregMockTest {

    private static final RegistrertNæringsvirksomhet VIRKSOMHET = new RegistrertNæringsvirksomhet(
            "999999999", "VTP FISKE", "ENK", "Enkeltpersonforetak", "03.110", "Hav- og kystfiske");

    @Test
    void skalHaBakoverkompatibelRequestsignatur() throws NoSuchMethodException {
        assertThat(BrregMock.class.getAnnotation(Path.class).value()).isEqualTo("/dummy/brreg");

        var metode = BrregMock.class.getMethod("hentRolleutskrift", String.class, UriInfo.class);
        assertThat(metode.getAnnotation(Path.class).value()).isEqualTo("/autorisert-api/personer/rolleutskrift");
        assertThat(metode.getAnnotation(Consumes.class).value()).containsExactly("text/plain");
    }

    @Test
    void skalReturnereRolleutskriftFraPersonensRegistrerteNæringsvirksomhet() {
        var person = PersonBuilder.lagSøker().tilBuilder()
                .medRegistrerteNæringsvirksomheter(List.of(VIRKSOMHET))
                .build();
        PersonRepository.leggTilPerson(person);
        var uriInfo = mock(UriInfo.class);
        when(uriInfo.getBaseUriBuilder()).thenReturn(UriBuilder.fromUri("http://localhost:8060/rest/"));

        var rolleutskrift = new BrregMock().hentRolleutskrift(
                person.personopplysninger().identifikator().value(), uriInfo);

        assertThat(rolleutskrift.enheter()).singleElement().satisfies(enhet -> {
            assertThat(enhet.organisasjonsnummer()).isEqualTo("999999999");
            assertThat(enhet.navn()).isEqualTo("VTP FISKE");
            assertThat(enhet.roller()).singleElement().satisfies(rolle -> {
                assertThat(rolle.fratraadt()).isFalse();
                assertThat(rolle.avregistrert()).isFalse();
                assertThat(rolle.type().kode()).isEqualTo("INNH");
            });
            assertThat(enhet._links().enhet().href())
                .hasToString("http://localhost:8060/rest/dummy/brreg/enheter/999999999");
        });
    }

    @Test
    void skalReturnereTomRolleutskriftForPersonUtenRegistrertNæringsvirksomhet() {
        var person = PersonBuilder.lagSøker();
        PersonRepository.leggTilPerson(person);

        var rolleutskrift = new BrregMock().hentRolleutskrift(
                person.personopplysninger().identifikator().value(), mock(UriInfo.class));

        assertThat(rolleutskrift.enheter()).isEmpty();
    }

    @Test
    void skalReturnereTomRolleutskriftForUkjentPerson() {
        var rolleutskrift = new BrregMock().hentRolleutskrift("12345678910", mock(UriInfo.class));

        assertThat(rolleutskrift.enheter()).isEmpty();
    }

    @Test
    void skalReturnereDynamiskEnhet() {
        var person = PersonBuilder.lagSøker().tilBuilder()
                .medRegistrerteNæringsvirksomheter(List.of(VIRKSOMHET))
                .build();
        PersonRepository.leggTilPerson(person);

        var enhet = new BrregMock().hentEnhet(VIRKSOMHET.organisasjonsnummer());

        assertThat(enhet.organisasjonsnummer()).isEqualTo("999999999");
        assertThat(enhet.navn()).isEqualTo("VTP FISKE");
        assertThat(enhet.organisasjonsform().kode()).isEqualTo("ENK");
        assertThat(enhet.organisasjonsform().beskrivelse()).isEqualTo("Enkeltpersonforetak");
        assertThat(enhet.naeringskode1().kode()).isEqualTo("03.110");
        assertThat(enhet.naeringskode1().beskrivelse()).isEqualTo("Hav- og kystfiske");
        assertThat(enhet.underAvvikling()).isFalse();
    }

    @Test
    void skalOppdatereEnhetNårOrganisasjonsnummerGjenbrukes() {
        var opprinneligVirksomhet = new RegistrertNæringsvirksomhet(
                "888888888", "OPPRINNELIG NAVN", "ENK", "Enkeltpersonforetak",
                "03.110", "Hav- og kystfiske");
        var oppdatertVirksomhet = new RegistrertNæringsvirksomhet(
                opprinneligVirksomhet.organisasjonsnummer(), "OPPDATERT NAVN", "ENK", "Enkeltpersonforetak",
                "03.110", "Hav- og kystfiske");
        var person = PersonBuilder.lagSøker().tilBuilder()
                .medRegistrerteNæringsvirksomheter(List.of(opprinneligVirksomhet))
                .build();
        PersonRepository.leggTilPerson(person);
        PersonRepository.leggTilPerson(person.tilBuilder()
                .medRegistrerteNæringsvirksomheter(List.of(oppdatertVirksomhet))
                .build());

        var enhet = new BrregMock().hentEnhet(opprinneligVirksomhet.organisasjonsnummer());

        assertThat(enhet.navn()).isEqualTo("OPPDATERT NAVN");
    }

    @Test
    void skalReturnere404ForUkjentOrganisasjon() {
        assertThatThrownBy(() -> new BrregMock().hentEnhet("111111111"))
                .isInstanceOf(NotFoundException.class)
                .satisfies(feil -> assertThat(((NotFoundException) feil).getResponse().getStatus()).isEqualTo(404));
    }
}
