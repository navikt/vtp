package no.nav.foreldrepenger.fpwsproxy.oppdrag;

import static no.nav.foreldrepenger.fpwsproxy.oppdrag.OppdragskontrollTilBeregingMapper.dateTimeFormatter;
import static no.nav.foreldrepenger.fpwsproxy.oppdrag.OppdragskontrollTilBeregingMapper.nesteMåned;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.kontrakter.fpwsproxy.simulering.request.KodeEndring;
import no.nav.foreldrepenger.kontrakter.fpwsproxy.simulering.request.KodeEndringLinje;
import no.nav.foreldrepenger.kontrakter.fpwsproxy.simulering.request.KodeFagområde;
import no.nav.foreldrepenger.kontrakter.fpwsproxy.simulering.request.KodeKlassifik;
import no.nav.foreldrepenger.kontrakter.fpwsproxy.simulering.request.LukketPeriode;
import no.nav.foreldrepenger.kontrakter.fpwsproxy.simulering.request.Ompostering116Dto;
import no.nav.foreldrepenger.kontrakter.fpwsproxy.simulering.request.Oppdrag110Dto;
import no.nav.foreldrepenger.kontrakter.fpwsproxy.simulering.request.OppdragskontrollDto;
import no.nav.foreldrepenger.kontrakter.fpwsproxy.simulering.request.Oppdragslinje150Dto;
import no.nav.foreldrepenger.kontrakter.fpwsproxy.simulering.request.SatsDto;
import no.nav.foreldrepenger.kontrakter.fpwsproxy.simulering.request.TypeSats;
import no.nav.foreldrepenger.kontrakter.fpwsproxy.simulering.request.UtbetalingsgradDto;
import no.nav.foreldrepenger.kontrakter.fpwsproxy.simulering.respons.BeregningDto;

// TODO: Rydd opp i test
class OppdragKontrollTilBeregningMapperTest {

    private static final String FAKE_FNR = "12499845829";
    private static final LocalDate NOW = LocalDate.now();
    private static final YearMonth SISTE_MÅNED = nesteMåned();


    @Test
    void simuleringFPMedEndring() {
        var oppdrag110Dto = lagOppdrag110(KodeFagområde.FP,
                new Ompostering116Dto(true, LocalDate.of(2022, 10, 22), "2022-11-20-12.50.52.243"),
                lagOppdragslinje150(NOW.plusMonths(3), NOW.plusMonths(6).minusDays(11), KodeEndringLinje.ENDR, KodeKlassifik.FPF_ARBEIDSTAKER, TypeSats.DAG, SatsDto.valueOf(1846), UtbetalingsgradDto.valueOf(100), 225100103L),
                lagOppdragslinje150(NOW.plusYears(1).plusMonths(6), NOW.plusYears(1).plusMonths(7), KodeEndringLinje.ENDR, KodeKlassifik.FERIEPENGER_BRUKER, TypeSats.DAG, SatsDto.valueOf(1846), UtbetalingsgradDto.valueOf(100), 225100105L),
                lagOppdragslinje150(NOW.minusMonths(1), NOW.minusWeeks(2), KodeEndringLinje.NY, KodeKlassifik.FPF_ARBEIDSTAKER, TypeSats.DAG, SatsDto.valueOf(1846), UtbetalingsgradDto.valueOf(100), 121100106L),
                lagOppdragslinje150(NOW.minusWeeks(2).plusDays(3), NOW.plusMonths(1), KodeEndringLinje.NY, KodeKlassifik.FPF_ARBEIDSTAKER, TypeSats.DAG, SatsDto.valueOf(1846), UtbetalingsgradDto.valueOf(100), 121100107L),
                lagOppdragslinje150(NOW.plusMonths(6), NOW.plusMonths(7), KodeEndringLinje.NY, KodeKlassifik.FERIEPENGER_BRUKER, TypeSats.DAG, SatsDto.valueOf(1846), UtbetalingsgradDto.valueOf(100), 121100108L)
        );
        var oppdragskontroll = new OppdragskontrollDto(1000302L, List.of(oppdrag110Dto));

        var beregninger = OppdragskontrollTilBeregingMapper.tilBeregningDto(oppdragskontroll, false, null);
        verifiserKorrektMapping(beregninger, 1, 2, null, 1);
    }

    @Test
    void simuleringSVPSkalIkkeVurdereOppdragsperioderSomStarterEtterNesteMåned() {
        var oppdrag110Dto = lagOppdrag110(KodeFagområde.SVP,
                lagOppdragslinje150(NOW.minusMonths(2).minusWeeks(2), NOW.minusMonths(2), KodeEndringLinje.NY, TypeSats.DAG, SatsDto.valueOf(1846), UtbetalingsgradDto.valueOf(100), 121100100L),
                lagOppdragslinje150(NOW.minusMonths(2).plusDays(3), NOW.minusMonths(1).minusWeeks(2), KodeEndringLinje.NY, TypeSats.DAG, SatsDto.valueOf(1846), UtbetalingsgradDto.valueOf(100), 121100101L),
                lagOppdragslinje150(NOW.minusMonths(1).minusWeeks(2).plusDays(1), NOW.plusWeeks(3), KodeEndringLinje.NY, TypeSats.DAG, SatsDto.valueOf(1846), UtbetalingsgradDto.valueOf(100), 121100102L),
                lagOppdragslinje150(NOW.plusWeeks(3).plusDays(3), NOW.plusMonths(4), KodeEndringLinje.NY, TypeSats.DAG, SatsDto.valueOf(1846), UtbetalingsgradDto.valueOf(100), 121100103L),
                lagOppdragslinje150(NOW.plusMonths(6), NOW.plusMonths(7), KodeEndringLinje.NY, TypeSats.DAG, SatsDto.valueOf(1846), UtbetalingsgradDto.valueOf(100), 121100104L)
        );
        var oppdragskontroll = new OppdragskontrollDto(1000302L, List.of(oppdrag110Dto));

        var beregninger = OppdragskontrollTilBeregingMapper.tilBeregningDto(oppdragskontroll, false, null);
        verifiserKorrektMapping(beregninger, 1, null, null, 1);
    }


    @Test
    void simuleringESReturnererEnBeregningMedFomOgTomLik() {
        var oppdrag110Dto = lagOppdrag110(KodeFagområde.REFUTG,
                lagOppdragslinje150(NOW, NOW.plusDays(3), KodeEndringLinje.NY, TypeSats.ENG, SatsDto.valueOf(90_300), null, 121100100L)
        );
        var oppdragskontroll = new OppdragskontrollDto(1000302L, List.of(oppdrag110Dto));

        var beregninger = OppdragskontrollTilBeregingMapper.tilBeregningDto(oppdragskontroll, false, null);
        verifiserKorrektMapping(beregninger, 1, 1, 1, 1);
    }


    @Test
    void simuleringIngenOppdragSkalReturnereTomListeMedBeregninger() {
        var oppdrag110Dto = lagOppdrag110(KodeFagområde.FP,
                new Ompostering116Dto(true, NOW, "2022-11-20-12.50.52.243")
        );
        var oppdragskontroll = new OppdragskontrollDto(1000302L, List.of(oppdrag110Dto));

        var beregninger = OppdragskontrollTilBeregingMapper.tilBeregningDto(oppdragskontroll, false, null);
        assertThat(beregninger).isEmpty();
    }


    /**
     * Hvis noen av Integer er 'null' så sjekkes ikke antallet, men bare inneholdet
     */
    private static void verifiserKorrektMapping(List<BeregningDto> beregninger,
                                                Integer antallBeregninger,
                                                Integer antallBeregningsPerioderPerBeregning,
                                                Integer antallBeregningStoppnivåPerBeregningPeriode,
                                                Integer antallBeregningStoppnivåDetaljPerStoppnivå) {
        if (antallBeregninger != null) {
            assertThat(beregninger).hasSize(antallBeregninger);
        }
        for (var beregning : beregninger) {
            assertThat(beregning.gjelderId()).isNotNull();
            assertThat(beregning.gjelderNavn()).isNotNull();
            assertThat(beregning.datoBeregnet()).isNotNull();
            assertThat(beregning.kodeFaggruppe()).isNotNull();
            assertThat(beregning.belop()).isNotNull();
            if (antallBeregningsPerioderPerBeregning != null) {
                assertThat(beregning.beregningsPeriode()).hasSize(antallBeregningsPerioderPerBeregning);
            }
            for (var beregningsPeriode : beregning.beregningsPeriode()) {
                assertThat(beregningsPeriode.periodeFom()).isNotNull();
                assertThat(erDatoGyldlig(beregningsPeriode.periodeFom())).isTrue();
                assertThat(erDatoGyldlig(beregningsPeriode.periodeTom())).isTrue();

                var beregningStoppnivaaListe = beregningsPeriode.beregningStoppnivaa();
                if (antallBeregningStoppnivåPerBeregningPeriode != null) {
                    assertThat(beregningStoppnivaaListe).hasSize(antallBeregningStoppnivåPerBeregningPeriode);
                }
                for (var beregningStoppnivaa : beregningStoppnivaaListe) {
                    assertThat(beregningStoppnivaa.kodeFagomraade()).isNotNull();
                    assertThat(beregningStoppnivaa.stoppNivaaId()).isNotNull();
                    assertThat(beregningStoppnivaa.behandlendeEnhet()).isNotNull();
                    assertThat(beregningStoppnivaa.fagsystemId()).isNotNull();
                    assertThat(beregningStoppnivaa.utbetalesTilId()).isNotNull();
                    assertThat(beregningStoppnivaa.bilagsType()).isNotNull();
                    assertThat(beregningStoppnivaa.forfall()).isNotNull();

                    var beregningStoppnivåDetaljer = beregningStoppnivaa.beregningStoppnivaaDetaljer();
                    if (antallBeregningStoppnivåDetaljPerStoppnivå != null) {
                        assertThat(beregningStoppnivåDetaljer).hasSize(antallBeregningStoppnivåDetaljPerStoppnivå);
                    }
                    for (var beregningStoppnivåDetalj : beregningStoppnivåDetaljer) {
                        assertThat(erDatoGyldlig(beregningStoppnivåDetalj.faktiskFom())).isTrue();
                        assertThat(erDatoGyldlig(beregningStoppnivåDetalj.faktiskTom())).isTrue();
                        assertThat(beregningStoppnivåDetalj.kontoStreng()).isNotNull();
                        assertThat(beregningStoppnivåDetalj.behandlingskode()).isNotNull();
                        assertThat(beregningStoppnivåDetalj.belop()).isNotNull();
                        assertThat(beregningStoppnivåDetalj.stonadId()).isNotNull();
                        assertThat(beregningStoppnivåDetalj.linjeId()).isNotNull();
                        assertThat(beregningStoppnivåDetalj.sats()).isNotNull();
                        assertThat(beregningStoppnivåDetalj.typeSats()).isNotNull();
                        assertThat(beregningStoppnivåDetalj.antallSats()).isNotNull();
                        assertThat(beregningStoppnivåDetalj.saksbehId()).isNotNull();
                        assertThat(beregningStoppnivåDetalj.uforeGrad()).isNotNull();
                        assertThat(beregningStoppnivåDetalj.bostedsenhet()).isNotNull();
                        assertThat(beregningStoppnivåDetalj.klassekode()).isNotNull();
                        assertThat(beregningStoppnivåDetalj.typeKlasse()).isNotNull();
                    }
                }
            }
        }
    }

    private static boolean erDatoGyldlig(String dato) {
        return YearMonth.parse(dato, dateTimeFormatter).isBefore(SISTE_MÅNED.plusMonths(1));
    }

    private static Oppdrag110Dto lagOppdrag110(KodeFagområde kodeFagområde, Oppdragslinje150Dto... oppdragslinje150Dtos) {
        return lagOppdrag110(kodeFagområde, null, oppdragslinje150Dtos);
    }

    private static Oppdrag110Dto lagOppdrag110(KodeFagområde kodeFagområde, Ompostering116Dto ompostering116Dto, Oppdragslinje150Dto... oppdragslinje150Dtos) {
        return new Oppdrag110Dto(
                KodeEndring.NY,
                kodeFagområde,
                121100L,
                "12499845829",
                "saksbeh",
                ompostering116Dto,
                List.of(oppdragslinje150Dtos)
        );
    }

    private static Oppdragslinje150Dto lagOppdragslinje150(LocalDate fom, LocalDate tom, KodeEndringLinje kodeEndringLinje, TypeSats typeSats, SatsDto satsDto, UtbetalingsgradDto utbetalingsgradDto, Long delytelseId) {
        return lagOppdragslinje150(fom, tom, kodeEndringLinje, KodeKlassifik.FERIEPENGER_BRUKER, typeSats, satsDto, utbetalingsgradDto, delytelseId);
    }

    private static Oppdragslinje150Dto lagOppdragslinje150(LocalDate fom, LocalDate tom, KodeEndringLinje kodeEndringLinje, KodeKlassifik kodeKlassifik, TypeSats typeSats, SatsDto satsDto, UtbetalingsgradDto utbetalingsgradDto, Long delytelseId) {
        return new Oppdragslinje150Dto(
                kodeEndringLinje,
                "2022-11-19",
                delytelseId,
                kodeKlassifik,
                new LukketPeriode(fom, tom),
                satsDto,
                typeSats,
                utbetalingsgradDto,
                null,
                null,
                FAKE_FNR,
                delytelseId - 1,
                126100L,
                null
        );
    }


}
