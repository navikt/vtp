package no.nav.digdir;

import no.nav.vtp.PersonBuilder;
import no.nav.vtp.person.PersonRepository;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DigdirKrrProxyMockTest {

    private final DigdirKrrProxyMock digdirKrrProxyMock = new DigdirKrrProxyMock();

    @Test
    void hentSpråkFraDigdirKrrProxy() {
        var person = PersonBuilder.lagAnnenPart();
        PersonRepository.leggTilPerson(person);
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
