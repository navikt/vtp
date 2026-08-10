package no.nav.tjeneste.virksomhet.dpsak;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import no.nav.vtp.PersonBuilder;
import no.nav.vtp.person.Person;
import no.nav.vtp.person.PersonRepository;
import no.nav.vtp.person.ytelse.LegacyKilde;
import no.nav.vtp.person.ytelse.Ytelse;
import no.nav.vtp.person.ytelse.YtelseType;

class DagpengerDatadelingMockTest {

    private final DagpengerDatadelingMock dpsakMock = new DagpengerDatadelingMock();

    @Test
    void henterDagpengerPerioderMedDefaultKilde() {
        var personBase = PersonBuilder.lagSøker();
        var dagpenger = new Ytelse(YtelseType.DAGPENGER, LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 5), 1000, null, null, null, List.of());
        var person = new Person(personBase.personopplysninger(), personBase.arbeidsforhold(), personBase.inntekt(), List.of(dagpenger), List.of());
        PersonRepository.leggTilPerson(person);

        var request = new DagpengerDatadelingMock.PersonRequest(person.personopplysninger().identifikator().value(),
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 5));
        var respons = dpsakMock.postDagpengerPerioder(request);

        assertThat(respons.perioder()).hasSize(1);
        assertThat(respons.perioder().getFirst().kilde()).isEqualTo(DagpengerRettighetsperioderDto.DagpengerKilde.DP_SAK);
    }

    @Test
    void henterDagpengerBeregninger() {
        var personBase = PersonBuilder.lagSøker();
        var dagpenger = new Ytelse(YtelseType.DAGPENGER, LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 5), 1000, 5000, null, null, List.of());
        var person = new Person(personBase.personopplysninger(), personBase.arbeidsforhold(), personBase.inntekt(), List.of(dagpenger), List.of());
        PersonRepository.leggTilPerson(person);

        var request = new DagpengerDatadelingMock.PersonRequest(person.personopplysninger().identifikator().value(),
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 5));
        var respons = dpsakMock.postDagpengerBeregning(request);

        assertThat(respons).hasSize(5);
        assertThat(respons[0].sats()).isEqualTo(1000);
        assertThat(respons[0].utbetaltBeløp()).isEqualTo(1000);
        assertThat(respons[0].kilde()).isEqualTo(DagpengerUtbetalingDto.DagpengerKilde.DP_SAK);
    }

    @Test
    void beregnerUtbetaltBeløpFraDagsatsOgUtbetalingsgradNårUtbetaltErNull() {
        var personBase = PersonBuilder.lagSøker();
        var dagpenger = new Ytelse(YtelseType.DAGPENGER, LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 5), 1000, null, 50, null, List.of());
        var person = new Person(personBase.personopplysninger(), personBase.arbeidsforhold(), personBase.inntekt(), List.of(dagpenger), List.of());
        PersonRepository.leggTilPerson(person);

        var request = new DagpengerDatadelingMock.PersonRequest(person.personopplysninger().identifikator().value(), null, null);
        var respons = dpsakMock.postDagpengerBeregning(request);

        assertThat(respons).hasSize(5);
        assertThat(respons[0].sats()).isEqualTo(1000);
        assertThat(respons).extracting(DagpengerUtbetalingDto::utbetaltBeløp)
                .containsExactly(500, 500, 0, 0, 500);
        assertThat(respons[0].kilde()).isEqualTo(DagpengerUtbetalingDto.DagpengerKilde.DP_SAK);
    }

    @Test
    void filtrererBortYtelserMedEksplisittArenaKilde() {
        var personBase = PersonBuilder.lagSøker();
        var dagpenger = new Ytelse(YtelseType.DAGPENGER, LocalDate.now().minusMonths(2), LocalDate.now().plusMonths(1), 1000, 5000, null, LegacyKilde.ARENA, List.of());
        var person = new Person(personBase.personopplysninger(), personBase.arbeidsforhold(), personBase.inntekt(), List.of(dagpenger), List.of());
        PersonRepository.leggTilPerson(person);

        var request = new DagpengerDatadelingMock.PersonRequest(person.personopplysninger().identifikator().value(), null, null);
        var perioder = dpsakMock.postDagpengerPerioder(request);
        var beregninger = dpsakMock.postDagpengerBeregning(request);

        assertThat(perioder.perioder()).isEmpty();
        assertThat(beregninger).isEmpty();
    }

    @Test
    void ignorererAndreYtelsetyperEnnDagpenger() {
        var personBase = PersonBuilder.lagSøker();
        var aap = new Ytelse(YtelseType.ARBEIDSAVKLARINGSPENGER, LocalDate.now().minusMonths(2), LocalDate.now().plusMonths(1), 1500, null, null, null, List.of());
        var person = new Person(personBase.personopplysninger(), personBase.arbeidsforhold(), personBase.inntekt(), List.of(aap), List.of());
        PersonRepository.leggTilPerson(person);

        var request = new DagpengerDatadelingMock.PersonRequest(person.personopplysninger().identifikator().value(), null, null);
        var perioder = dpsakMock.postDagpengerPerioder(request);
        var beregninger = dpsakMock.postDagpengerBeregning(request);

        assertThat(perioder.perioder()).isEmpty();
        assertThat(beregninger).isEmpty();
    }

    @Test
    void begrenserPerioderOgBeregningerTilForespurtPeriode() {
        var personBase = PersonBuilder.lagSøker();
        var dagpenger = new Ytelse(YtelseType.DAGPENGER, LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 31), 1000, null, 50, null, List.of());
        var person = new Person(personBase.personopplysninger(), personBase.arbeidsforhold(), personBase.inntekt(), List.of(dagpenger), List.of());
        PersonRepository.leggTilPerson(person);

        var request = new DagpengerDatadelingMock.PersonRequest(person.personopplysninger().identifikator().value(),
                LocalDate.of(2026, 1, 10), LocalDate.of(2026, 1, 12));
        var perioder = dpsakMock.postDagpengerPerioder(request);
        var beregninger = dpsakMock.postDagpengerBeregning(request);

        assertThat(perioder.perioder()).singleElement().satisfies(periode -> {
            assertThat(periode.fraOgMedDato()).isEqualTo(LocalDate.of(2026, 1, 10));
            assertThat(periode.tilOgMedDato()).isEqualTo(LocalDate.of(2026, 1, 12));
        });
        assertThat(beregninger).hasSize(3);
        assertThat(beregninger).extracting(DagpengerUtbetalingDto::utbetaltBeløp)
                .containsExactly(0, 0, 500);
    }
}
