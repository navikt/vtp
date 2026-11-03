package no.nav.foreldrepenger.sokos.simulering;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import no.nav.foreldrepenger.sokos.simulering.SokosSimuleringRequest.Oppdragslinje;
import no.nav.foreldrepenger.sokos.simulering.SokosSimuleringRequest.RefusjonsInfo;
import no.nav.foreldrepenger.sokos.simulering.SokosSimuleringRespons.Detaljer;
import no.nav.foreldrepenger.util.JacksonObjectMapperTestscenario;
import tools.jackson.databind.json.JsonMapper;

class SokosSimuleringMockTest {

    private static final JsonMapper JSON = JacksonObjectMapperTestscenario.getJsonMapper();
    private static final String BRUKER = "12345678910";
    private static final String ARBEIDSGIVER = "00999999999";
    private static final LocalDate FOM = LocalDate.of(2020, 1, 1);
    private static final LocalDate TOM = LocalDate.of(2020, 2, 29);
    private final SokosSimuleringMock mock = new SokosSimuleringMock();

    @DisplayName("Fordeler ytelse per måned og velger bruker eller refusjonsmottaker")
    @ParameterizedTest
    @CsvSource({
        "FP, FPATORD, DAG, false",
        "FPREF, FPREFAG-IOP, DAG, true",
        "SVP, FPSVATORD, DAG, false",
        "SVPREF, FPSVREFAG-IOP, DAG, true",
        "REFUTG, FPENFOD-OP, ENG, false"
    })
    void ytelseOgRefusjon(String fagområde, String klassifik, String typeSats, boolean refusjon) {
        var linje = new Oppdragslinje("NY", null, klassifik, FOM, TOM, BigDecimal.valueOf(100), typeSats,
            refusjon ? List.of(new RefusjonsInfo(ARBEIDSGIVER, TOM, FOM)) : List.of());
        var respons = mock.simulerBeregning(request(fagområde, List.of(linje), false));
        var beregning = respons.operation().container().responsSimulering().beregning();

        assertThat(beregning.gjelderId()).isEqualTo(BRUKER);
        assertThat(beregning.periodeStoppnivaa()).hasSize(2).allSatisfy(periode -> {
            assertThat(periode.beregningStoppnivaa().utbetalesTilId()).isEqualTo(refusjon ? ARBEIDSGIVER : BRUKER);
            assertThat(periode.beregningStoppnivaa().kodeFagomraade()).isEqualTo(fagområde);
            assertThat(periode.beregningStoppnivaa().datoForfall()).isEqualTo(LocalDate.now().toString());
            assertThat(periode.beregningsPeriode()).isEqualTo(new SokosSimuleringRespons.BeregningsPeriode(FOM.toString(), TOM.toString()));
        });
        assertThat(posteringer(respons)).containsExactly(
            detalj("2020-01-01", "2020-01-31", 2300, "YTEL"),
            detalj("2020-02-01", "2020-02-29", 2000, "YTEL"));

        var json = JSON.readTree(JSON.writeValueAsString(respons));
        assertThat(json.at("/OsSimulerBeregningOperationResponse/ZS1OCont/responsSimulering/statusKode").intValue()).isZero();
        assertThat(json.at("/OsSimulerBeregningOperationResponse/ZS1OCont/responsSimulering/beregning/periodeStoppnivaa/0"
            + "/beregningStoppnivaa/stoppnivaaDetaljer/0/belop").decimalValue()).isEqualByComparingTo("2300");
    }

    @DisplayName("Historiske satsendringer gir korreksjoner, også ved lik sats og uten inntrekk")
    @ParameterizedTest
    @CsvSource({"80, FEIL, 460, -2300, 400, -2000", "100, YTEL, -2300, 2300, -2000, 2000", "120, YTEL, -2300, 2760, -2000, 2400"})
    void satsendring(int nySats, String førsteType, long januarFørste, long januarAndre, long februarFørste, long februarAndre) {
        var linjer = List.of(linje("ENDR", FOM, TOM, 100), linje("NY", FOM, TOM, nySats));
        var respons = mock.simulerBeregning(request("FP", linjer, false));
        var utenInntrekk = mock.simulerBeregning(request("FP", linjer, true));

        assertThat(utenInntrekk).isEqualTo(respons);
        assertThat(posteringer(respons)).containsExactly(
            detalj("2020-01-01", "2020-01-31", januarFørste, førsteType),
            detalj("2020-01-01", "2020-01-31", januarAndre, "YTEL"),
            detalj("2020-02-01", "2020-02-29", februarFørste, førsteType),
            detalj("2020-02-01", "2020-02-29", februarAndre, "YTEL"));
    }

    @Test
    @DisplayName("Opphør bruker statusdato og gir ytelse, feilutbetaling og reversering")
    void opphørFraStatusdato() {
        var opphør = new Oppdragslinje("ENDR", FOM.plusDays(7), "FPATORD", FOM, TOM, BigDecimal.valueOf(100), "DAG", List.of());
        var respons = mock.simulerBeregning(request("FP", List.of(opphør), false));

        assertThat(posteringer(respons)).containsExactly(
            detalj("2020-01-08", "2020-01-31", 1800, "YTEL"),
            detalj("2020-01-08", "2020-01-31", 1800, "FEIL"),
            detalj("2020-01-08", "2020-01-31", -1800, "YTEL"),
            detalj("2020-02-01", "2020-02-29", 2000, "YTEL"),
            detalj("2020-02-01", "2020-02-29", 2000, "FEIL"),
            detalj("2020-02-01", "2020-02-29", -2000, "YTEL"));
    }

    @Test
    @DisplayName("Delvis overlapp splitter satsendringer, udekket opphør og ny ytelse")
    void delvisOverlapp() {
        var linjer = List.of(
            linje("NY", LocalDate.of(2020, 1, 20), LocalDate.of(2020, 2, 7), 120),
            linje("ENDR", LocalDate.of(2020, 1, 6), LocalDate.of(2020, 1, 24), 100),
            linje("NY", FOM, LocalDate.of(2020, 1, 10), 80));
        var respons = mock.simulerBeregning(request("FP", linjer, false));

        assertThat(posteringer(respons)).containsExactly(
            detalj("2020-01-01", "2020-01-05", 240, "YTEL"),
            detalj("2020-01-06", "2020-01-10", 100, "FEIL"),
            detalj("2020-01-06", "2020-01-10", -500, "YTEL"),
            detalj("2020-01-11", "2020-01-19", 500, "YTEL"),
            detalj("2020-01-11", "2020-01-19", 500, "FEIL"),
            detalj("2020-01-11", "2020-01-19", -500, "YTEL"),
            detalj("2020-01-20", "2020-01-24", -500, "YTEL"),
            detalj("2020-01-20", "2020-01-24", 600, "YTEL"),
            detalj("2020-01-25", "2020-01-31", 600, "YTEL"),
            detalj("2020-02-01", "2020-02-07", 600, "YTEL"));
    }

    @DisplayName("Avgrenser perioder og korreksjoner ved kjøremåneden før og etter den 19.")
    @ParameterizedTest
    @CsvSource({"2026-11-19, 2026-11-30, 2026-10-31", "2026-11-20, 2026-12-31, 2026-11-30"})
    void avgrensningOgForfall(LocalDate idag, LocalDate sisteYtelse, LocalDate sisteOpphør) {
        var fom = LocalDate.of(2026, 10, 1);
        var tom = LocalDate.of(2027, 1, 31);
        var linjer = List.of(linje("ENDR", fom, tom, 100), linje("NY", fom, tom, 80));
        var respons = SokosSimuleringMapper.simuler(request("FP", linjer, false), idag);
        var perioder = respons.operation().container().responsSimulering().beregning().periodeStoppnivaa();

        assertThat(perioder.getFirst().beregningStoppnivaa().stoppnivaaDetaljer())
            .extracting(Detaljer::typeKlasse).containsExactly("FEIL", "YTEL");
        assertThat(perioder.getLast().beregningStoppnivaa().stoppnivaaDetaljer())
            .extracting(Detaljer::typeKlasse).containsExactly("YTEL");
        assertThat(posteringer(respons).getLast().faktiskTom()).isEqualTo(sisteYtelse.toString());
        assertThat(perioder.getLast().beregningStoppnivaa().datoForfall()).isEqualTo("2026-11-20");
        assertThat(perioder.getFirst().beregningStoppnivaa().datoForfall()).isEqualTo(idag.toString());

        var opphør = SokosSimuleringMapper.simuler(request("FP", List.of(linje("ENDR", fom, tom, 100)), false), idag);
        assertThat(posteringer(opphør).getLast().faktiskTom()).isEqualTo(sisteOpphør.toString());
    }

    @Test
    @DisplayName("Tomme, fremtidige og rene helgeperioder gir vellykket respons uten beregning")
    void ingenBeregningsperioder() {
        var tilfeller = List.of(
            List.<Oppdragslinje>of(),
            List.of(linje("NY", LocalDate.of(2027, 2, 1), LocalDate.of(2027, 3, 1), 100)),
            List.of(linje("NY", LocalDate.of(2020, 1, 4), LocalDate.of(2020, 1, 5), 100)),
            List.of(new Oppdragslinje("ENDR", TOM.plusDays(1), "FPATORD", FOM, TOM, BigDecimal.valueOf(100), "DAG", List.of())));

        assertThat(tilfeller).allSatisfy(linjer -> {
            var respons = SokosSimuleringMapper.simuler(request("FP", linjer, false), LocalDate.of(2026, 11, 3))
                .operation().container().responsSimulering();
            assertThat(respons.statusKode()).isZero();
            assertThat(respons.beregning()).isNull();
        });
    }

    private static SokosSimuleringRequest request(String fagområde, List<Oppdragslinje> linjer, boolean utenInntrekk) {
        var json = """
            {"OsSimulerBeregningOperation":{"ZS1ICont":{"requestSimulering":{
              "simuleringsPeriode":{"datoSimulerFom":"","datoSimulerTom":""},
              "oppdrag":{"kodeEndring":"%s","kodeFagomraade":"%s","fagsystemId":"SYNTHETIC",
                "oppdragGjelderId":"%s","saksbehId":"Z123456","datoOppdragGjelderFom":"2000-01-01","utbetFrekvens":"MND"},
              "enhetOppdrag":[{"typeEnhet":"BOS","enhet":"8020","datoEnhetFom":"1900-01-01"}],
              "omposter":[{"ompostering":"%s","datoOmposterFom":"","feilreg":false}],
              "oppdragslinje":%s
            }}}}
            """.formatted(utenInntrekk ? "ENDR" : "NY", fagområde, BRUKER, utenInntrekk ? "N" : "J",
            JSON.writeValueAsString(linjer.stream().map(linje -> Map.of(
                "kodeEndringLinje", linje.kodeEndringLinje(),
                "datoStatusFom", linje.datoStatusFom() == null ? "" : linje.datoStatusFom().toString(),
                "kodeKlassifik", linje.kodeKlassifik(),
                "datoVedtakFom", linje.datoVedtakFom().toString(),
                "datoVedtakTom", linje.datoVedtakTom().toString(),
                "sats", linje.sats(),
                "typeSats", linje.typeSats(),
                "brukKjoreplan", false,
                "gradOppdragslinje", List.of(Map.of("typeGrad", "UFOR", "grad", 100)),
                "refusjonsInfo", linje.refusjonsInfo()
            )).toList()));
        return JSON.readValue(json, SokosSimuleringRequest.class);
    }

    private static List<Detaljer> posteringer(SokosSimuleringRespons respons) {
        return respons.operation().container().responsSimulering().beregning().periodeStoppnivaa().stream()
            .flatMap(periode -> periode.beregningStoppnivaa().stoppnivaaDetaljer().stream()).toList();
    }

    private static Detaljer detalj(String fom, String tom, long beløp, String typeKlasse) {
        return new Detaljer(fom, tom, BigDecimal.valueOf(beløp), typeKlasse);
    }

    private static Oppdragslinje linje(String endring, LocalDate fom, LocalDate tom, int sats) {
        return new Oppdragslinje(endring, null, "FPATORD", fom, tom, BigDecimal.valueOf(sats), "DAG", List.of());
    }
}
