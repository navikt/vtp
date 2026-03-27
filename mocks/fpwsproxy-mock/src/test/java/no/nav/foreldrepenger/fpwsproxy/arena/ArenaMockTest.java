package no.nav.foreldrepenger.fpwsproxy.arena;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.kontrakter.fpwsproxy.arena.request.ArenaRequestDto;
import no.nav.vtp.person.Person;
import no.nav.vtp.PersonBuilder;
import no.nav.vtp.person.PersonRepository;
import no.nav.vtp.person.ytelse.Ytelse;
import no.nav.vtp.person.ytelse.YtelseType;

class ArenaMockTest {

    private final FpWsProxyArenaMock fpWsProxyArenaMock = new FpWsProxyArenaMock();
    private static final LocalDate now = LocalDate.now();

    @Test
    void henterDagpengerOgAAP_bareDAGPENGER() {
        // Arrange
        var personBase = PersonBuilder.lagSøker();
        var dagpenger = new Ytelse(YtelseType.DAGPENGER, now.minusYears(1), now.plusYears(1), 1000, 5000, List.of());
        var person = new Person(personBase.personopplysninger(), personBase.arbeidsforhold(), personBase.inntekt(), List.of(dagpenger), List.of());
        PersonRepository.leggTilPerson(person);

        // Act
        var request = new ArenaRequestDto(person.personopplysninger().identifikator().value(), now.minusMonths(6), now.plusMonths(6));
        var meldekort = fpWsProxyArenaMock.hentMeldekort(request);

        // Assert
        assertThat(meldekort).hasSize(1);
        assertThat(meldekort.getFirst().type()).isNotNull();
    }

    @Test
    void henterDagpengerOgAAP_bareAAP() {
        // Arrange
        var personBase = PersonBuilder.lagSøker();
        var aap = new Ytelse(YtelseType.ARBEIDSAVKLARINGSPENGER, now.minusYears(1), now.plusYears(1), 1500, 7000, List.of());
        var person = new Person(personBase.personopplysninger(), personBase.arbeidsforhold(), personBase.inntekt(), List.of(aap), List.of());
        PersonRepository.leggTilPerson(person);

        // Act
        var request = new ArenaRequestDto(person.personopplysninger().identifikator().value(), now.minusMonths(6), now.plusMonths(6));
        var meldekort = fpWsProxyArenaMock.hentMeldekort(request);

        // Assert
        assertThat(meldekort).hasSize(1);
        assertThat(meldekort.getFirst().type()).isNotNull();
    }

    @Test
    void henterDagpengerOgAAP_bådeDAGPENGERogAAP() {
        // Arrange
        var personBase = PersonBuilder.lagSøker();
        var dagpenger = new Ytelse(YtelseType.DAGPENGER, now.minusYears(1), now.plusYears(1), 1000, 5000, List.of());
        var aap = new Ytelse(YtelseType.ARBEIDSAVKLARINGSPENGER, now, now.plusYears(2), 1500, 7000, List.of());
        var person = new Person(personBase.personopplysninger(), personBase.arbeidsforhold(), personBase.inntekt(), List.of(dagpenger, aap), List.of());
        PersonRepository.leggTilPerson(person);

        // Act
        var request = new ArenaRequestDto(person.personopplysninger().identifikator().value(), now.minusMonths(6), now.plusMonths(6));
        var meldekort = fpWsProxyArenaMock.hentMeldekort(request);

        // Assert
        assertThat(meldekort).hasSize(2);
    }

    @Test
    void henterDagpengerOgAAP_ingenYtelser() {
        // Arrange
        var personBase = PersonBuilder.lagSøker();
        var person = new Person(personBase.personopplysninger(), personBase.arbeidsforhold(), personBase.inntekt(), List.of(), List.of());
        PersonRepository.leggTilPerson(person);

        // Act
        var request = new ArenaRequestDto(person.personopplysninger().identifikator().value(), now.minusMonths(6), now.plusMonths(6));
        var meldekort = fpWsProxyArenaMock.hentMeldekort(request);

        // Assert
        assertThat(meldekort).isEmpty();
    }

    @Test
    void lager_bare_meldekort_for_dagpenger_og_fjern_ytelser_som_ikke_er_relevante_eller_utenfor_requested_periode() {
        // Arrange
        var personBase = PersonBuilder.lagSøker();
        var dagpengerInnen = new Ytelse(YtelseType.DAGPENGER, now.minusYears(1), now.plusYears(1), 1000, 5000, List.of());
        var foreldrepenger = new Ytelse(YtelseType.FORELDREPENGER, now.minusYears(1), now.plusYears(1), 2000, 8000, List.of());
        var dagpengerUtenfor = new Ytelse(YtelseType.DAGPENGER, now.plusYears(2), now.plusYears(3), 1000, 5000, List.of());
        var person = new Person(personBase.personopplysninger(), personBase.arbeidsforhold(), personBase.inntekt(), List.of(dagpengerInnen, dagpengerUtenfor, foreldrepenger), List.of());
        PersonRepository.leggTilPerson(person);

        // Act
        var request = new ArenaRequestDto(person.personopplysninger().identifikator().value(), now.minusMonths(6), now.plusMonths(6));
        var meldekort = fpWsProxyArenaMock.hentMeldekort(request);

        // Assert
        assertThat(meldekort).hasSize(1);
    }
}
