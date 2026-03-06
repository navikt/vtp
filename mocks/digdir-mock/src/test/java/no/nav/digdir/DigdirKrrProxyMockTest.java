package no.nav.digdir;

import no.nav.vtp.Person;
import no.nav.vtp.PersonBuilder;
import no.nav.vtp.PersonRepository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DigdirKrrProxyMockTest {

    private static DigdirKrrProxyMock digdirKrrProxyMock;
    private static final PersonRepository personRepository = new PersonRepository();
    private static final Person person = PersonBuilder.lagAnnenPart();

    @BeforeAll
    static void setup() {
        personRepository.leggTilPerson(person);
        digdirKrrProxyMock = new DigdirKrrProxyMock(personRepository);
    }

    @Test
    void hentSpråkFraDigdirKrrProxy() {
        var ident = person.personopplysninger().identifikator().value();
        var response = digdirKrrProxyMock.hentKontaktinformasjon(new DigdirKrrProxyMock.Personidenter(List.of(ident)));
        var kontaktinformasjon = (Kontaktinformasjoner) response.getEntity();

        assertThat(kontaktinformasjon).isNotNull();
        assertThat(kontaktinformasjon.personer()).hasSize(1);
        assertThat(kontaktinformasjon.personer().get(ident).spraak()).isEqualTo("NB");
    }


    @Test
    void hentSpråkFraDigdirKrrProxyNårPersonIkkeFinnesKaster404() {
        var identSomIkkeFinnes = "11111122222";
        var response = digdirKrrProxyMock.hentKontaktinformasjon(new DigdirKrrProxyMock.Personidenter(List.of(identSomIkkeFinnes)));
        var kontaktinformasjon = (Kontaktinformasjoner) response.getEntity();

        assertThat(kontaktinformasjon.personer()).isEmpty();
        assertThat(kontaktinformasjon.feil()).hasSize(1);
        assertThat(kontaktinformasjon.feil().get(identSomIkkeFinnes)).isEqualTo(Kontaktinformasjoner.FeilKode.person_ikke_funnet);

    }

}
