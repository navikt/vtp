package no.nav.tjeneste.virksomhet.kelvin;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import no.nav.vtp.PersonBuilder;
import no.nav.vtp.person.Person;
import no.nav.vtp.person.PersonRepository;
import no.nav.vtp.person.ytelse.Ytelse;
import no.nav.vtp.person.ytelse.YtelseType;

class KelvinMockTest {

    private final KelvinMock kelvinMock = new KelvinMock();

    @Test
    void henterAapVedtakForYtelse() {
        var personBase = PersonBuilder.lagSøker();
        var aap = new Ytelse(YtelseType.ARBEIDSAVKLARINGSPENGER, LocalDate.of(2026, 1, 5), LocalDate.of(2026, 1, 9), 1500, 75);
        var person = new Person(personBase.personopplysninger(), personBase.arbeidsforhold(), personBase.inntekt(), List.of(aap), List.of());
        PersonRepository.leggTilPerson(person);

        var request = new KelvinMock.PersonRequest(person.personopplysninger().identifikator().value(), null, null);
        var respons = kelvinMock.postAAP(request);

        assertThat(respons.vedtak()).hasSize(1);
        var vedtak = respons.vedtak().getFirst();
        assertThat(vedtak.dagsats()).isEqualTo(1500);
        assertThat(vedtak.dagsatsEtterUføreReduksjon()).isEqualTo(1500);
        assertThat(vedtak.status()).isEqualTo("LØPENDE");
        assertThat(vedtak.kildesystem()).isEqualTo(ArbeidsavklaringspengerResponse.Kildesystem.KELVIN);
        assertThat(vedtak.utbetaling()).hasSize(1);
        var utbetaling = vedtak.utbetaling().getFirst();
        assertThat(utbetaling.utbetalingsgrad()).isEqualTo(75);
        assertThat(utbetaling.dagsats()).isEqualTo(1125);
        assertThat(utbetaling.belop()).isEqualTo(5_625);
    }

    @Test
    void ignorererAndreYtelsetyperEnnAap() {
        var personBase = PersonBuilder.lagSøker();
        var dagpenger = new Ytelse(YtelseType.DAGPENGER, LocalDate.now().minusMonths(2), LocalDate.now().plusMonths(1), 1000, null);
        var person = new Person(personBase.personopplysninger(), personBase.arbeidsforhold(), personBase.inntekt(), List.of(dagpenger), List.of());
        PersonRepository.leggTilPerson(person);

        var request = new KelvinMock.PersonRequest(person.personopplysninger().identifikator().value(), null, null);
        var respons = kelvinMock.postAAP(request);

        assertThat(respons.vedtak()).isEmpty();
    }

    @Test
    void begrenserVedtakTilForespurtPeriode() {
        var personBase = PersonBuilder.lagSøker();
        var aap = new Ytelse(YtelseType.ARBEIDSAVKLARINGSPENGER,
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 31), 1000, null);
        var person = new Person(personBase.personopplysninger(), personBase.arbeidsforhold(), personBase.inntekt(), List.of(aap), List.of());
        PersonRepository.leggTilPerson(person);

        var respons = kelvinMock.postAAP(new KelvinMock.PersonRequest(
                person.personopplysninger().identifikator().value(), LocalDate.of(2026, 1, 5), LocalDate.of(2026, 1, 9)));

        var vedtak = respons.vedtak().getFirst();
        assertThat(vedtak.periode()).isEqualTo(new ArbeidsavklaringspengerResponse.AAPPeriode(
                LocalDate.of(2026, 1, 5), LocalDate.of(2026, 1, 9)));
        assertThat(vedtak.utbetaling().getFirst().belop()).isEqualTo(5_000);
    }
}
