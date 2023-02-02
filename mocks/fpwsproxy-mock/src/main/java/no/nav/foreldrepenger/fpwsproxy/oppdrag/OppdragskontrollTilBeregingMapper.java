package no.nav.foreldrepenger.fpwsproxy.oppdrag;

import static no.nav.foreldrepenger.fpwsproxy.UtilKlasse.safeStream;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import no.nav.foreldrepenger.kontrakter.fpwsproxy.simulering.request.Oppdrag110Dto;
import no.nav.foreldrepenger.kontrakter.fpwsproxy.simulering.request.OppdragskontrollDto;
import no.nav.foreldrepenger.kontrakter.fpwsproxy.simulering.request.Oppdragslinje150Dto;
import no.nav.foreldrepenger.kontrakter.fpwsproxy.simulering.request.Refusjonsinfo156Dto;
import no.nav.foreldrepenger.kontrakter.fpwsproxy.simulering.respons.BeregningDto;
import no.nav.foreldrepenger.kontrakter.fpwsproxy.simulering.respons.BeregningStoppnivåDetaljerDto;
import no.nav.foreldrepenger.kontrakter.fpwsproxy.simulering.respons.BeregningStoppnivåDto;
import no.nav.foreldrepenger.kontrakter.fpwsproxy.simulering.respons.BeregningsPeriodeDto;

class OppdragskontrollTilBeregingMapper {

    static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String DUMMY_VALUE = "DUMMY";

    private OppdragskontrollTilBeregingMapper() {
    }

    static List<BeregningDto> tilBeregningDto(OppdragskontrollDto oppdragskontrollDto, boolean utenInntrekk, YtelseType ytelseType) {
        return safeStream(oppdragskontrollDto.oppdrag())
                .map(OppdragskontrollTilBeregingMapper::tilBeregningDto)
                .filter(Objects::nonNull)
                .toList();
    }

    private static BeregningDto tilBeregningDto(Oppdrag110Dto oppdrag110Dto) {
        var beregningsPeriode = tilBeregningPeriodeDto(oppdrag110Dto);
        if (beregningsPeriode == null || beregningsPeriode.isEmpty()) {
            return null;
        }
        return new BeregningDto.Builder()
                .beregningsPeriode(beregningsPeriode)
                .gjelderId(oppdrag110Dto.oppdragGjelderId())
                .gjelderNavn(DUMMY_VALUE)
                .datoBeregnet(dateTimeFormatter.format(LocalDate.now()))
                .kodeFaggruppe("KORTTID")
                .belop(BigDecimal.valueOf(1234))
                .build();
    }

    /**
     * Skal effektivt være det samme som PeriodeGenerator
     * @param oppdrag110Dto
     * @return
     */
    private static List<BeregningsPeriodeDto> tilBeregningPeriodeDto(Oppdrag110Dto oppdrag110Dto) {
        var periodeGenerator = new PeriodeGenerator();
        var oppdragsperioder = periodeGenerator.genererPerioder(oppdrag110Dto.oppdragslinje150Liste());
        return safeStream(oppdragsperioder)
                .filter(OppdragskontrollTilBeregingMapper::ikkeLeggTilPeriodeHvisStartDatoEtterNesteMånedEllerInneholderIngenVirkedager)
                .map(periode -> tilBeregningPeriodeDto(periode, oppdrag110Dto))
                .toList();
    }

    private static boolean ikkeLeggTilPeriodeHvisStartDatoEtterNesteMånedEllerInneholderIngenVirkedager(Periode periode) {
        var sisteMåned = sisteMånedForPerioder(periode);
        return !YearMonth.from(periode.getFom()).isAfter(sisteMåned) && periode.getAntallVirkedager() != 0;
    }

    private static BeregningsPeriodeDto tilBeregningPeriodeDto(Periode periode, Oppdrag110Dto oppdrag110Dto) {
        var sisteMåned = sisteMånedForPerioder(periode);

        if (YearMonth.from(periode.getTom()).isAfter(sisteMåned)) {
            periode.setTom(sisteMåned.atEndOfMonth());
        }

        return new BeregningsPeriodeDto(
                periode.getFom().format(dateTimeFormatter),
                periode.getTom().format(dateTimeFormatter),
                tilBeregningStoppnivåDtoListe(periode, oppdrag110Dto));
    }

    private static List<BeregningStoppnivåDto> tilBeregningStoppnivåDtoListe(Periode periode, Oppdrag110Dto oppdrag) {
        var perioder = splittOppIPeriodePerMnd(periode);
        var sisteMåned = sisteMånedForPerioder(periode);

        return safeStream(perioder)
                .filter(p -> !YearMonth.from(p.getFom()).isAfter(sisteMåned))
                .map(p -> tilBeregningStoppnivåDto(p, oppdrag))
                .toList();
    }

    private static YearMonth sisteMånedForPerioder(Periode periode) {
        return PeriodeType.OPPH.equals(periode.getPeriodeType()) ? nesteMåned().minusMonths(1) : nesteMåned();
    }

    static YearMonth nesteMåned() {
        return LocalDate.now().getDayOfMonth() <= 19 ? YearMonth.from(LocalDate.now()) : YearMonth.from(LocalDate.now().plusMonths(1));
    }

    private static BeregningStoppnivåDto tilBeregningStoppnivåDto(Periode periodeMND, Oppdrag110Dto oppdrag) {
        var refunderesOrgNr = refunderesOrgNr(oppdrag);
        return new BeregningStoppnivåDto.Builder()
                .kodeFagomraade(oppdrag.kodeFagomrade().name())
                .utbetalesTilId(refunderesOrgNr.orElseGet(oppdrag::oppdragGjelderId))
                .utbetalesTilNavn(DUMMY_VALUE)
                .behandlendeEnhet("8052")
                .forfall(dateTimeFormatter.format(YearMonth.from(periodeMND.getFom()).equals(nesteMåned()) ? LocalDate.now().withDayOfMonth(20) : LocalDate.now()))
                .oppdragsId(1234L)
                .stoppNivaaId(BigInteger.ONE)
                .fagsystemId(oppdrag.fagsystemId().toString())
                .bilagsType("U")
                .feilkonto(periodeMND.getPeriodeType().equals(PeriodeType.OPPH))
                .kid("12345")
                .beregningStoppnivaaDetaljer(tilBeregningStoppNivåDetaljerDto(periodeMND, refunderesOrgNr))
                .build();
    }

    private static List<BeregningStoppnivåDetaljerDto> tilBeregningStoppNivåDetaljerDto(Periode periodeMND, Optional<String> refunderesOrgNr) {
        if (periodeMND.getPeriodeType().equals(PeriodeType.OPPH)) {
            return tilBeregningStoppNivåDetaljerOpphør(periodeMND, refunderesOrgNr);
        } else if (periodeMND.getPeriodeType().equals(PeriodeType.REDUKSJON) && YearMonth.from(periodeMND.getFom()).isBefore(nesteMåned())) {
            return tilBeregningStoppNivåDetaljerReduksjon(periodeMND, refunderesOrgNr);
        } else if (periodeMND.getPeriodeType().equals(PeriodeType.ØKNING) && YearMonth.from(periodeMND.getFom()).isBefore(nesteMåned())) {
            return tilBeregningStoppNivåDetaljerØkning(periodeMND, refunderesOrgNr);
        }
        return tilBeregningStoppNivåDetaljerYtelse(periodeMND, refunderesOrgNr);
    }

    private static List<BeregningStoppnivåDetaljerDto> tilBeregningStoppNivåDetaljerYtelse(Periode periodeMND, Optional<String> refunderesOrgNr) {
        return List.of(opprettBeregningStoppNivaaDetaljer(periodeMND, refunderesOrgNr));
    }

    private static List<BeregningStoppnivåDetaljerDto> tilBeregningStoppNivåDetaljerØkning(Periode periodeMND, Optional<String> refunderesOrgNr) {
        return List.of(
            opprettNegativBeregningStoppNivaaDetaljer(periodeMND, refunderesOrgNr, 3),
            opprettBeregningStoppNivaaDetaljer(periodeMND, refunderesOrgNr)
        );
    }

    private static List<BeregningStoppnivåDetaljerDto> tilBeregningStoppNivåDetaljerReduksjon(Periode periodeMND, Optional<String> refunderesOrgNr) {
        List<BeregningStoppnivåDetaljerDto> stoppnivå = new ArrayList<>();
        for (int i = 2 ; i <= 3 ; i++) {
            stoppnivå.add(opprettNegativBeregningStoppNivaaDetaljer(periodeMND, refunderesOrgNr, i));
        }
        return stoppnivå;
    }

    private static List<BeregningStoppnivåDetaljerDto> tilBeregningStoppNivåDetaljerOpphør(Periode periodeMND, Optional<String> refunderesOrgNr) {
        List<BeregningStoppnivåDetaljerDto> stoppnivå = new ArrayList<>();
        for (int i = 1 ; i <= 3 ; i++) {
            stoppnivå.add(opprettNegativBeregningStoppNivaaDetaljer(periodeMND, refunderesOrgNr, i));
        }
        return stoppnivå;
    }

    private static BeregningStoppnivåDetaljerDto opprettBeregningStoppNivaaDetaljer(Periode periodeMND, Optional<String> refunderesOrgNr) {
        return new BeregningStoppnivåDetaljerDto.Builder()
                .faktiskFom(dateTimeFormatter.format(periodeMND.getFom()))
                .faktiskTom(dateTimeFormatter.format(periodeMND.getTom()))
                .kontoStreng("1235432")
                .behandlingskode("2")
                .belop(BigDecimal.valueOf(periodeMND.getSats().verdi()).multiply(BigDecimal.valueOf(periodeMND.getAntallVirkedager())))
                .trekkVedtakId(0)
                .stonadId("1234")
                .korrigering("")
                .tilbakeforing(false)
                .linjeId(BigInteger.valueOf(21423L))
                .sats(BigDecimal.valueOf(periodeMND.getSats().verdi()))
                .typeSats("DAG")
                .antallSats(BigDecimal.valueOf(periodeMND.getAntallVirkedager()))
                .saksbehId("5323")
                .uforeGrad(BigInteger.valueOf(100))
                .kravhaverId("")
                .delytelseId("3523")
                .bostedsenhet("4643")
                .skykldnerId("")
                .klassekode(periodeMND.getKodeKlassifik().getKode())
                .klasseKodeBeskrivelse(DUMMY_VALUE)
                .typeKlasse("YTEL")
                .typeKlasseBeskrivelse(DUMMY_VALUE)
                .refunderesOrgNr(refunderesOrgNr.orElse(""))
                .build();
    }

    /**
     *  Sequence explanation:
     *      1.Ytelsen slik den stod original
     *      2.Feilutbetalt beløp
     *      3.Fjerning av ytelsen fra seqence 1
     */
    private static BeregningStoppnivåDetaljerDto opprettNegativBeregningStoppNivaaDetaljer(Periode periodeMND, Optional<String> refunderesOrgNr, int sequence) {
        var erSequence2 = sequence == 2;
        return new BeregningStoppnivåDetaljerDto.Builder()
                .faktiskFom(dateTimeFormatter.format(periodeMND.getFom()))
                .faktiskTom(dateTimeFormatter.format(periodeMND.getTom()))
                .kontoStreng("1235432")
                .behandlingskode(erSequence2 ? "0" : "2")
                .belop(tilBelop(periodeMND, sequence))
                .trekkVedtakId(0)
                .stonadId("")
                .korrigering(erSequence2 ? "J" : "")
                .tilbakeforing(sequence == 3)
                .linjeId(BigInteger.valueOf(21423L))
                .sats(BigDecimal.ZERO)
                .typeSats("")
                .antallSats(BigDecimal.ZERO)
                .saksbehId("5323")
                .uforeGrad(sequence == 3 ? BigInteger.valueOf(100) : BigInteger.ZERO)
                .kravhaverId("")
                .delytelseId("")
                .bostedsenhet("4643")
                .skykldnerId("")
                .klassekode(erSequence2 ? "KL_KODE_FEIL_KORTTID" : periodeMND.getKodeKlassifik().getKode())
                .klasseKodeBeskrivelse(DUMMY_VALUE)
                .typeKlasse(erSequence2 ? "FEIL" : "YTEL")
                .typeKlasseBeskrivelse(DUMMY_VALUE)
                .refunderesOrgNr(refunderesOrgNr.orElse(""))
                .build();
    }

    private static BigDecimal tilBelop(Periode periodeMND, int sequence) {
        int antallVirkedager = periodeMND.getAntallVirkedager();
        var belop = BigDecimal.valueOf(periodeMND.getSats().verdi()).multiply(BigDecimal.valueOf(antallVirkedager));

        if (periodeMND.getPeriodeType().equals(PeriodeType.OPPH)){
            if (sequence == 3){
                return belop.negate();
            }
            else { return belop; }
        }
        else {
            var oldSats = BigDecimal.valueOf(periodeMND.getOldSats().verdi());
            if (sequence == 1){
                return oldSats.multiply(BigDecimal.valueOf(antallVirkedager));
            }
            else if (sequence == 2){
                return belop.subtract(oldSats.multiply(BigDecimal.valueOf(antallVirkedager))).negate();
            }
            else if (sequence == 3){
                return oldSats.multiply(BigDecimal.valueOf(antallVirkedager)).negate();
            }
            else return belop;
        }
    }


    private static Optional<String> refunderesOrgNr(Oppdrag110Dto oppdrag110Dto) {
        return oppdrag110Dto.oppdragslinje150Liste().stream()
                .map(Oppdragslinje150Dto::refusjonsinfo156)
                .filter(Objects::nonNull)
                .map(Refusjonsinfo156Dto::refunderesId)
                .findFirst();
    }

    private static List<Periode> splittOppIPeriodePerMnd(Periode oppdragsperiode) {
        List<Periode> perioder = new ArrayList<>();

        if (oppdragsperiode.getTom().isBefore(oppdragsperiode.getFom())) {
            throw new IllegalArgumentException("Startdato " + oppdragsperiode.getFom() + " kan ikke være etter sluttdato " + oppdragsperiode.getTom());
        }

        LocalDate dato = oppdragsperiode.getFom();
        while (!dato.isAfter(oppdragsperiode.getTom())) {
            LocalDate sisteDagIMnd = YearMonth.from(dato).atEndOfMonth();
            if (sisteDagIMnd.isBefore(oppdragsperiode.getTom())) {
                perioder.add(new Periode(
                        dato,
                        sisteDagIMnd,
                        oppdragsperiode.getSats(),
                        oppdragsperiode.getOldSats(),
                        oppdragsperiode.getPeriodeType(),
                        oppdragsperiode.getKodeKlassifik()
                        ));
                dato = sisteDagIMnd.plusDays(1);
            } else {
                perioder.add(new Periode(
                        dato,
                        oppdragsperiode.getTom(),
                        oppdragsperiode.getSats(),
                        oppdragsperiode.getOldSats(),
                        oppdragsperiode.getPeriodeType(),
                        oppdragsperiode.getKodeKlassifik()));
                dato = oppdragsperiode.getTom().plusDays(1);
            }
        }
        return perioder;
    }

}
