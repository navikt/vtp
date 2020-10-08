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
import no.nav.system.os.entiteter.typer.simpletypes.KodeStatusLinje;
import no.nav.system.os.tjenester.simulerfpservice.simulerfpservicegrensesnitt.SimulerBeregningRequest;
import no.nav.system.os.tjenester.simulerfpservice.simulerfpservicegrensesnitt.SimulerBeregningResponse;
import no.nav.system.os.tjenester.simulerfpservice.simulerfpserviceservicetypes.Oppdrag;
import no.nav.system.os.tjenester.simulerfpservice.simulerfpserviceservicetypes.Oppdragslinje;

public class SimuleringGenerator {

    static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    Boolean negativSimulering;
    String kodeEndring;
    Boolean erOpphør;

    public SimulerBeregningResponse opprettSimuleringsResultat(SimulerBeregningRequest simulerBeregningRequest,
                                                               Boolean negativSimulering) {
        this.negativSimulering = negativSimulering;
        this.kodeEndring = simulerBeregningRequest.getRequest().getOppdrag().getKodeEndring();
        this.erOpphør = erOpphør(simulerBeregningRequest);

        SimulerBeregningResponse response = new SimulerBeregningResponse();
        Beregning beregning = lagBeregning(simulerBeregningRequest);
        if (beregning == null) {
            response.setResponse(null);
        } else {
            no.nav.system.os.tjenester.simulerfpservice.simulerfpserviceservicetypes.SimulerBeregningResponse innerResponse =
                    new no.nav.system.os.tjenester.simulerfpservice.simulerfpserviceservicetypes.SimulerBeregningResponse();
            response.setResponse(innerResponse);
            innerResponse.setSimulering(beregning);
        }
        return response;

    }

    private boolean erOpphør(SimulerBeregningRequest simulerBeregningRequest){
        List<Oppdragslinje> oppdragslinjer = simulerBeregningRequest.getRequest().getOppdrag().getOppdragslinje();
        if (oppdragslinjer.isEmpty()) {
            return false;
        }

        for (Oppdragslinje oppdragslinje : oppdragslinjer){
            if (oppdragslinje.getKodeStatusLinje() == null || !oppdragslinje.getKodeStatusLinje().equals(KodeStatusLinje.OPPH)){
                return false;
            }
        }
        return true;
    }

    private Beregning lagBeregning(SimulerBeregningRequest simulerBeregningRequest) {
        Beregning beregning = new Beregning();
        leggTilBeregningsperioder(simulerBeregningRequest, beregning);
        if (beregning.getBeregningsPeriode().size() == 0) {
            return null;
        }

        beregning.setGjelderId(simulerBeregningRequest.getRequest().getOppdrag().getOppdragGjelderId());
        beregning.setGjelderNavn("DUMMY");
        beregning.setDatoBeregnet(dateTimeFormatter.format(LocalDate.now()));
        beregning.setKodeFaggruppe("KORTTID");
        beregning.setBelop(BigDecimal.valueOf(1234L));

        return beregning;
    }

    private void leggTilBeregningsperioder(SimulerBeregningRequest simulerBeregningRequest, Beregning beregning) {
        YearMonth nesteMåned = YearMonth.from(LocalDate.now().plusMonths(1));
        List<Oppdragslinje> oppdragslinjer = simulerBeregningRequest.getRequest().getOppdrag().getOppdragslinje();
        List<BeregningsPeriode> beregningsPeriode = beregning.getBeregningsPeriode();
        for (Oppdragslinje oppdragslinje : oppdragslinjer) {
            LocalDate fom = LocalDate.parse(oppdragslinje.getDatoVedtakFom(), dateTimeFormatter);
            if (!YearMonth.from(fom).isAfter(nesteMåned)) {
                beregningsPeriode.add(opprettBeregningsperiode(oppdragslinje, simulerBeregningRequest.getRequest().getOppdrag()));
            }
        }
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

        YearMonth nesteMåned;
        if (erOpphør){nesteMåned = YearMonth.from(LocalDate.now());}
        else {nesteMåned = YearMonth.from(LocalDate.now().plusMonths(1));}
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
                stoppnivaa.setFeilkonto(erOpphør);
                stoppnivaa.setKid("12345");

                if (negativSimulering && kodeEndring.equals("ENDR")){
                    stoppnivaa.getBeregningStoppnivaaDetaljer().add(OpprettNegativBeregningStoppNivaaDetaljer(periode, oppdragslinje,1));
                    stoppnivaa.getBeregningStoppnivaaDetaljer().add(OpprettNegativBeregningStoppNivaaDetaljer(periode, oppdragslinje,2));
                    stoppnivaa.getBeregningStoppnivaaDetaljer().add(OpprettNegativBeregningStoppNivaaDetaljer(periode, oppdragslinje,3));
                    stoppnivaa.getBeregningStoppnivaaDetaljer().add(OpprettNegativBeregningStoppNivaaDetaljer(periode, oppdragslinje,4));
                }
                else if (erOpphør){
                    stoppnivaa.getBeregningStoppnivaaDetaljer().add(OpprettNegativBeregningStoppNivaaDetaljer(periode,oppdragslinje,1));
                    stoppnivaa.getBeregningStoppnivaaDetaljer().add(OpprettNegativBeregningStoppNivaaDetaljer(periode,oppdragslinje,2));
                    stoppnivaa.getBeregningStoppnivaaDetaljer().add(OpprettNegativBeregningStoppNivaaDetaljer(periode,oppdragslinje,3));
                }
                else {
                    stoppnivaa.getBeregningStoppnivaaDetaljer().add(OpprettBeregningStoppNivaaDetaljer(periode, oppdragslinje));
                }
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

    private BeregningStoppnivaaDetaljer OpprettNegativBeregningStoppNivaaDetaljer(Periode periode, Oppdragslinje oppdragslinje, int sequence) {
        int antallVirkedager = periode.getAntallVirkedager();

        BeregningStoppnivaaDetaljer stoppnivaaDetaljer = new BeregningStoppnivaaDetaljer();

        //Sequence explanation:
        //1.Ytelsen slik den stod original
        //2.Feilutbetalt beløp
        //3.Fjerning av ytelsen fra seqence 1
        //4.Ny ytelse (hvis det er noen)

        //fom
        stoppnivaaDetaljer.setFaktiskFom(dateTimeFormatter.format(periode.getFom()));
        //tom
        stoppnivaaDetaljer.setFaktiskTom(dateTimeFormatter.format(periode.getTom()));
        //kontoStreng
        stoppnivaaDetaljer.setKontoStreng("1235432");
        //behandlingskode
        if (sequence == 2){stoppnivaaDetaljer.setBehandlingskode("0");}
        else {stoppnivaaDetaljer.setBehandlingskode("2");}
        //belop
        BigDecimal belop = oppdragslinje.getSats().multiply(BigDecimal.valueOf(antallVirkedager));
        if (sequence == 3 && periode.getFom().isBefore(LocalDate.now())){
            if (erOpphør){ stoppnivaaDetaljer.setBelop(belop.negate()); }
            else { stoppnivaaDetaljer.setBelop(belop.multiply(BigDecimal.valueOf(2)).negate()); }
        }
        else { stoppnivaaDetaljer.setBelop(belop); }
        //trekkVedtakId
        stoppnivaaDetaljer.setTrekkVedtakId(0L);
        //stonadId
        if (sequence == 4){ stoppnivaaDetaljer.setStonadId("1234"); }
        else { stoppnivaaDetaljer.setStonadId(""); }
        //korrigering
        if (sequence == 2){ stoppnivaaDetaljer.setKorrigering("J"); }
        else { stoppnivaaDetaljer.setKorrigering(""); }
        //tilbakeforing
        stoppnivaaDetaljer.setTilbakeforing(sequence == 3);
        //linjeId
        stoppnivaaDetaljer.setLinjeId(BigInteger.valueOf(21423L));
        //sats
        if (sequence == 4){ stoppnivaaDetaljer.setSats(oppdragslinje.getSats()); }
        else { stoppnivaaDetaljer.setSats(BigDecimal.ZERO); }
        //typeSats
        if (sequence == 4){ stoppnivaaDetaljer.setTypeSats("DAG"); }
        else { stoppnivaaDetaljer.setTypeSats(""); }
        //antallSats
        if (sequence >= 1 && sequence <= 2 || erOpphør) { stoppnivaaDetaljer.setAntallSats(BigDecimal.valueOf(0)); }
        else { stoppnivaaDetaljer.setAntallSats(BigDecimal.valueOf(antallVirkedager)); }
        //saksbehId
        stoppnivaaDetaljer.setSaksbehId("5323");
        //uforeGrad
        if (sequence == 3 && erOpphør){stoppnivaaDetaljer.setUforeGrad(BigInteger.valueOf(100L));}
        else if (sequence != 4){ stoppnivaaDetaljer.setUforeGrad(BigInteger.ZERO); }
        else { stoppnivaaDetaljer.setUforeGrad(BigInteger.valueOf(100L)); }
        //kravHaverId ?
        stoppnivaaDetaljer.setKravhaverId("");
        //delytelseId
        if (sequence == 4){ stoppnivaaDetaljer.setDelytelseId("3523"); }
        else { stoppnivaaDetaljer.setDelytelseId(""); }
        //bostedsenhet
        stoppnivaaDetaljer.setBostedsenhet("4643");
        //skyldnerId ?
        stoppnivaaDetaljer.setSkykldnerId("");
        //klassekode
        if (sequence == 2){ stoppnivaaDetaljer.setKlassekode("KL_KODE_FEIL_KORTTID"); }
        else { stoppnivaaDetaljer.setKlassekode(oppdragslinje.getKodeKlassifik()); }
        //klasseKodeBeskrivelse
        stoppnivaaDetaljer.setKlasseKodeBeskrivelse("DUMMY");
        //typeKlasse
        if (sequence == 2) { stoppnivaaDetaljer.setTypeKlasse("FEIL"); }
        else { stoppnivaaDetaljer.setTypeKlasse("YTEL"); }
        //typeKlasseBeskrivelse
        stoppnivaaDetaljer.setTypeKlasseBeskrivelse("DUMMY");
        //refunderesOrgNr ?
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
