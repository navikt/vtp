package no.nav.system.os.eksponering;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import no.nav.system.os.entiteter.beregningskjema.Beregning;
import no.nav.system.os.entiteter.beregningskjema.BeregningStoppnivaa;
import no.nav.system.os.entiteter.beregningskjema.BeregningStoppnivaaDetaljer;
import no.nav.system.os.entiteter.beregningskjema.BeregningsPeriode;
import no.nav.system.os.tjenester.simulerfpservice.simulerfpservicegrensesnitt.SimulerBeregningRequest;
import no.nav.system.os.tjenester.simulerfpservice.simulerfpservicegrensesnitt.SimulerBeregningResponse;
import no.nav.system.os.tjenester.simulerfpservice.simulerfpserviceservicetypes.Oppdrag;
import no.nav.system.os.tjenester.simulerfpservice.simulerfpserviceservicetypes.Oppdragslinje;

public class SimuleringGenerator {

    static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public SimulerBeregningResponse opprettSimuleringsResultat(SimulerBeregningRequest simulerBeregningRequest) {
        SimulerBeregningResponse response = new SimulerBeregningResponse();
        no.nav.system.os.tjenester.simulerfpservice.simulerfpserviceservicetypes.SimulerBeregningResponse innerResponse = new no.nav.system.os.tjenester.simulerfpservice.simulerfpserviceservicetypes.SimulerBeregningResponse();
        response.setResponse(innerResponse);
        innerResponse.setSimulering(lagBeregning(simulerBeregningRequest));
        return response;

    }

    private Beregning lagBeregning(SimulerBeregningRequest simulerBeregningRequest) {
        Beregning beregning = new Beregning();
        beregning.setGjelderId(simulerBeregningRequest.getRequest().getOppdrag().getOppdragGjelderId());
        beregning.setGjelderNavn("DUMMY");
        beregning.setDatoBeregnet("2018-10-10");
        beregning.setKodeFaggruppe("DUMMY");
        beregning.setBelop(BigDecimal.valueOf(1234L));

        YearMonth nesteMåned = YearMonth.from(LocalDate.now().plusMonths(1));
        List<Oppdragslinje> oppdragslinjer = simulerBeregningRequest.getRequest().getOppdrag().getOppdragslinje();
        for (Oppdragslinje oppdragslinje : oppdragslinjer) {
            LocalDate fom = LocalDate.parse(oppdragslinje.getDatoVedtakFom(), dateTimeFormatter);
            if (!YearMonth.from(fom).isAfter(nesteMåned)) {
                beregning.getBeregningsPeriode().add(opprettBeregningsperiode(oppdragslinje, simulerBeregningRequest.getRequest().getOppdrag()));
            }
        }
        return beregning;
    }

    private BeregningsPeriode opprettBeregningsperiode(Oppdragslinje oppdragslinje, Oppdrag oppdrag) {
        BeregningsPeriode beregningsPeriode = new BeregningsPeriode();
        String datoVedtakFom = oppdragslinje.getDatoVedtakFom();
        beregningsPeriode.setPeriodeFom(datoVedtakFom);
        String datoVedtakTom = oppdragslinje.getDatoVedtakTom();
        beregningsPeriode.setPeriodeTom(datoVedtakTom);
        beregningsPeriode.getBeregningStoppnivaa().addAll(OpprettBeregningStoppNivaa(oppdragslinje, oppdrag));
        return beregningsPeriode;
    }

    private List<BeregningStoppnivaa> OpprettBeregningStoppNivaa(Oppdragslinje oppdragslinje, Oppdrag oppdrag) {
        List<Periode> perioder = splittOppIPeriodePerMnd(oppdragslinje.getDatoVedtakFom(), oppdragslinje.getDatoVedtakTom());
        List<BeregningStoppnivaa> beregningStoppnivaaer = new ArrayList<>();

        YearMonth nesteMåned = YearMonth.from(LocalDate.now().plusMonths(1));
        for (Periode periode : perioder) {
            if (!YearMonth.from(periode.getFom()).isAfter(nesteMåned)) {
                BeregningStoppnivaa stoppnivaa = new BeregningStoppnivaa();
                stoppnivaa.setKodeFagomraade(oppdrag.getKodeFagomraade());
                if (oppdragslinje.getRefusjonsInfo() != null) {
                    stoppnivaa.setUtbetalesTilId(oppdragslinje.getRefusjonsInfo().getRefunderesId());
                    stoppnivaa.setUtbetalesTilNavn("DUMMY FIRMA");
                } else {
                    stoppnivaa.setUtbetalesTilId(oppdragslinje.getUtbetalesTilId());
                    stoppnivaa.setUtbetalesTilNavn("DUMMY");
                }
                stoppnivaa.setBehandlendeEnhet("8052");
                LocalDate forfallsdato = periode.getFom().isBefore(LocalDate.now()) ? LocalDate.now() : periode.getTom().plusDays(1);
                stoppnivaa.setForfall(dateTimeFormatter.format(forfallsdato));
                stoppnivaa.setOppdragsId(1234L);
                stoppnivaa.setStoppNivaaId(BigInteger.ONE);
                stoppnivaa.setFagsystemId(oppdrag.getFagsystemId());
                stoppnivaa.setBilagsType("U");
                stoppnivaa.setFeilkonto(false);
                stoppnivaa.setKid("12345");

                stoppnivaa.getBeregningStoppnivaaDetaljer().add(OpprettBeregningStoppNivaaDetaljer(periode, oppdragslinje));
                beregningStoppnivaaer.add(stoppnivaa);
            }
        }

        return beregningStoppnivaaer;
    }

    private BeregningStoppnivaaDetaljer OpprettBeregningStoppNivaaDetaljer(Periode periode, Oppdragslinje oppdragslinje) {
        int antallVirkedager = periode.getAntallVirkedager();

        BeregningStoppnivaaDetaljer stoppnivaaDetaljer = new BeregningStoppnivaaDetaljer();

        stoppnivaaDetaljer.setFaktiskFom(dateTimeFormatter.format(periode.getFom()));
        stoppnivaaDetaljer.setFaktiskTom(dateTimeFormatter.format(periode.getTom()));
        stoppnivaaDetaljer.setKontoStreng("1235432");
        stoppnivaaDetaljer.setBehandlingskode("2");
        stoppnivaaDetaljer.setBelop(oppdragslinje.getSats().multiply(BigDecimal.valueOf(antallVirkedager)));
        stoppnivaaDetaljer.setTrekkVedtakId(0L);
        stoppnivaaDetaljer.setStonadId("1234");
        stoppnivaaDetaljer.setKorrigering("");
        stoppnivaaDetaljer.setTilbakeforing(false);
        stoppnivaaDetaljer.setLinjeId(BigInteger.valueOf(21423L));
        stoppnivaaDetaljer.setSats(oppdragslinje.getSats());
        stoppnivaaDetaljer.setTypeSats("DAG");
        stoppnivaaDetaljer.setAntallSats(BigDecimal.valueOf(antallVirkedager));
        stoppnivaaDetaljer.setSaksbehId("5323");
        stoppnivaaDetaljer.setUforeGrad(BigInteger.valueOf(100L));
        stoppnivaaDetaljer.setKravhaverId("");
        stoppnivaaDetaljer.setDelytelseId("3523");
        stoppnivaaDetaljer.setBostedsenhet("4643");
        stoppnivaaDetaljer.setSkykldnerId("");
        stoppnivaaDetaljer.setKlassekode(oppdragslinje.getKodeKlassifik());
        stoppnivaaDetaljer.setKlasseKodeBeskrivelse("DUMMY");
        stoppnivaaDetaljer.setTypeKlasse("YTEL");
        stoppnivaaDetaljer.setTypeKlasseBeskrivelse("DUMMY");
        stoppnivaaDetaljer.setRefunderesOrgNr("");

        return stoppnivaaDetaljer;
    }

    static List<Periode> splittOppIPeriodePerMnd(String datoVedtakFom, String datoVedtakTom) {
        List<Periode> perioder = new ArrayList<>();

        LocalDate startDato = LocalDate.parse(datoVedtakFom, dateTimeFormatter);
        LocalDate sluttDato = LocalDate.parse(datoVedtakTom, dateTimeFormatter);
        if (sluttDato.isBefore(startDato)) {
            throw new IllegalArgumentException("Startdato " + datoVedtakFom + " kan ikke være etter sluttdato " + datoVedtakTom);
        }

        LocalDate dato = startDato;
        while (!dato.isAfter(sluttDato)) {
            LocalDate sisteDagIMnd = YearMonth.from(dato).atEndOfMonth();
            if (sisteDagIMnd.isBefore(sluttDato)) {
                perioder.add(new Periode(dato, sisteDagIMnd));
                dato = sisteDagIMnd.plusDays(1);
            } else {
                perioder.add(new Periode(dato, sluttDato));
                dato = sluttDato.plusDays(1);
            }
        }
        return perioder;
    }


    static class Periode {
        private LocalDate fom;
        private LocalDate tom;

        Periode(LocalDate fom, LocalDate tom) {
            this.fom = fom;
            this.tom = tom;
        }

        public LocalDate getFom() {
            return fom;
        }

        public LocalDate getTom() {
            return tom;
        }

        public int getAntallVirkedager() {
            LocalDate startDato = fom;
            int antallVirkedager = 0;
            while (startDato.isBefore(tom) || startDato.isEqual(tom)) {
                if (!startDato.getDayOfWeek().equals(DayOfWeek.SATURDAY) && !startDato.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                    antallVirkedager++;
                }
                startDato = startDato.plusDays(1);
            }
            return antallVirkedager;
        }
    }
}
