package no.nav.tjeneste.virksomhet.spokelse.rest;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import no.nav.vtp.PersonBuilder;
import no.nav.vtp.person.Person;
import no.nav.vtp.person.PersonRepository;
import no.nav.vtp.person.ytelse.Ytelse;
import no.nav.vtp.person.ytelse.YtelseType;

class SpøkelseMockTest {

    private final SpøkelseMock spøkelseMock = new SpøkelseMock();

    @Test
    void henterSykepengevedtakMedRiktigGradNaarSatt() {
        var personBase = PersonBuilder.lagSøker();
        var sykepenger = new Ytelse(YtelseType.SYKEPENGER, LocalDate.now().minusMonths(2), LocalDate.now().plusMonths(1), 1500, 50);
        var person = new Person(personBase.personopplysninger(), personBase.arbeidsforhold(), personBase.inntekt(), List.of(sykepenger), List.of());
        PersonRepository.leggTilPerson(person);

        var request = new SpøkelseMock.PersonRequest(person.personopplysninger().identifikator().value(), LocalDate.now().minusMonths(6));
        var vedtak = spøkelseMock.postSykepenger(request);

        assertThat(vedtak).hasSize(1);
        assertThat(vedtak[0].utbetalingerNonNull()).hasSize(1);
        assertThat(vedtak[0].utbetalingerNonNull().getFirst().grad()).isEqualByComparingTo("50");
    }

    @Test
    void bruker100ProsentGradSomDefaultNaarIkkeSatt() {
        var personBase = PersonBuilder.lagSøker();
        var sykepenger = new Ytelse(YtelseType.SYKEPENGER, LocalDate.now().minusMonths(2), LocalDate.now().plusMonths(1), 1500, null);
        var person = new Person(personBase.personopplysninger(), personBase.arbeidsforhold(), personBase.inntekt(), List.of(sykepenger), List.of());
        PersonRepository.leggTilPerson(person);

        var request = new SpøkelseMock.PersonRequest(person.personopplysninger().identifikator().value(), null);
        var vedtak = spøkelseMock.postSykepenger(request);

        assertThat(vedtak[0].utbetalingerNonNull().getFirst().grad()).isEqualByComparingTo("100");
    }

    @Test
    void filtrererBortYtelserSomErAvsluttetFoerFomIRequest() {
        var personBase = PersonBuilder.lagSøker();
        var gammelSykepengeperiode = new Ytelse(YtelseType.SYKEPENGER, LocalDate.now().minusYears(2), LocalDate.now().minusYears(1), 1500, 100);
        var person = new Person(personBase.personopplysninger(), personBase.arbeidsforhold(), personBase.inntekt(), List.of(gammelSykepengeperiode), List.of());
        PersonRepository.leggTilPerson(person);

        var request = new SpøkelseMock.PersonRequest(person.personopplysninger().identifikator().value(), LocalDate.now().minusMonths(6));
        var vedtak = spøkelseMock.postSykepenger(request);

        assertThat(vedtak).isEmpty();
    }

    @Test
    void ignorererAndreYtelsetyperEnnSykepenger() {
        var personBase = PersonBuilder.lagSøker();
        var dagpenger = new Ytelse(YtelseType.DAGPENGER, LocalDate.now().minusMonths(2), LocalDate.now().plusMonths(1), 1000, null);
        var person = new Person(personBase.personopplysninger(), personBase.arbeidsforhold(), personBase.inntekt(), List.of(dagpenger), List.of());
        PersonRepository.leggTilPerson(person);

        var request = new SpøkelseMock.PersonRequest(person.personopplysninger().identifikator().value(), null);
        var vedtak = spøkelseMock.postSykepenger(request);

        assertThat(vedtak).isEmpty();
    }
}
