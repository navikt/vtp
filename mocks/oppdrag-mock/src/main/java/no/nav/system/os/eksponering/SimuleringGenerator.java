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
    Boolean erOpphør;
    Boolean erOmpostering;
    LocalDate opphørFom;
    LocalDate opphørTom;
    Boolean erRefusjon;
    String refunderesOrgNr;

    public SimulerBeregningResponse opprettSimuleringsResultat(SimulerBeregningRequest simulerBeregningRequest) {
        this.erOpphør = erOpphør(simulerBeregningRequest.getRequest().getOppdrag().getOppdragslinje());
        this.erOmpostering = erOmpostering(simulerBeregningRequest.getRequest().getOppdrag());
        if (erOmpostering){
            if (!simulerBeregningRequest.getRequest().getOppdrag().getOmpostering().getDatoOmposterFom().equals(simulerBeregningRequest.getRequest().getOppdrag().getOppdragslinje().get(0).getDatoStatusFom())){
                throw new IllegalArgumentException("datoOmposterFom (" + simulerBeregningRequest.getRequest().getOppdrag().getOmpostering().getDatoOmposterFom() + ") og datoStatusFom i første periode(" + simulerBeregningRequest.getRequest().getOppdrag().getOppdragslinje().get(0).getDatoStatusFom() + ")må være like");
            }
            this.opphørFom = LocalDate.parse(simulerBeregningRequest.getRequest().getOppdrag().getOppdragslinje().get(0).getDatoStatusFom(), dateTimeFormatter);
            this.opphørTom = LocalDate.parse(simulerBeregningRequest.getRequest().getOppdrag().getOppdragslinje().get(1).getDatoVedtakFom(), dateTimeFormatter);
        }
        this.erRefusjon = erRefusjon(simulerBeregningRequest.getRequest().getOppdrag().getOppdragslinje());

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

    private boolean erOpphør(List<Oppdragslinje> oppdragslinjer){
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

    private boolean erOmpostering(Oppdrag oppdrag){
        if (oppdrag.getOmpostering() != null && oppdrag.getOmpostering().getOmPostering() != null){
            return oppdrag.getOmpostering().getOmPostering().equals("J");
        }
        return false;
    }

    private boolean erOpphørPeriode(LocalDate fom, LocalDate tom){
        if (erOmpostering) {
            return !fom.isBefore(opphørFom) && tom.isBefore(opphørTom);
        }
        return false;
    }

    private boolean erRefusjon(List<Oppdragslinje> oppdragslinjer){
        if (oppdragslinjer.isEmpty() || oppdragslinjer.get(0).getRefusjonsInfo() == null) {
            return false;
        }
        this.refunderesOrgNr = oppdragslinjer.get(0).getRefusjonsInfo().getRefunderesId();
        for (Oppdragslinje oppdragslinje : oppdragslinjer){
            if (oppdragslinje.getRefusjonsInfo().getRefunderesId().isEmpty() || !oppdragslinje.getRefusjonsInfo().getRefunderesId().equals(refunderesOrgNr)){
                throw new IllegalArgumentException("Ved refusjon må alle oppdragslinjer ha samme refusjonsInfo. Både orgnr " + refunderesOrgNr + " og " + oppdragslinje.getRefusjonsInfo().getRefunderesId() + "ble funnet i samme request.");
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
        YearMonth nesteMåned;
        if (erOpphør){nesteMåned = YearMonth.from(LocalDate.now());}
        else {nesteMåned = YearMonth.from(LocalDate.now().plusMonths(1));}
        List<Oppdragslinje> oppdragslinjer = simulerBeregningRequest.getRequest().getOppdrag().getOppdragslinje();
        List<BeregningsPeriode> beregningsPerioder = beregning.getBeregningsPeriode();
        //Hvis det er opphør av ytelse fra DatoStatusFom til DatoVedtakFom (erOmpostering=J) konstrueres negative periode her eventuelt med feilutbetaling
        if (erOmpostering){
            if (oppdragslinjer.size() <= 1){
                throw new IllegalArgumentException("Simuleringer med omPostering=J må minimum ha oppdragslinje med kodeEndringLinje OPPH og en med kodeEndringLinje NY. Denne simulering-request hadde " + oppdragslinjer.size() + " oppdragslinjer");
            }
            //Trenger ikke null-sjekk her ettersom det gjøres i erOmpostering() metoden
            Oppdragslinje mallinje = oppdragslinjer.get(0);
            if (opphørFom.isBefore(LocalDate.parse(oppdragslinjer.get(1).getDatoVedtakFom(), dateTimeFormatter))) {
                Oppdragslinje omposteringsOppdragslinje = new Oppdragslinje();
                omposteringsOppdragslinje.setDatoVedtakFom(mallinje.getDatoStatusFom());
                omposteringsOppdragslinje.setDatoVedtakTom(LocalDate.parse(oppdragslinjer.get(1).getDatoVedtakFom(), dateTimeFormatter).minusDays(1L).toString());
                omposteringsOppdragslinje.setKodeEndringLinje("ENDR");
                omposteringsOppdragslinje.setKodeStatusLinje(KodeStatusLinje.OPPH);
                omposteringsOppdragslinje.setDatoStatusFom(mallinje.getDatoStatusFom());
                omposteringsOppdragslinje.setVedtakId(mallinje.getVedtakId());
                omposteringsOppdragslinje.setDelytelseId(mallinje.getDelytelseId());
                omposteringsOppdragslinje.setKodeKlassifik(mallinje.getKodeKlassifik());
                omposteringsOppdragslinje.setSats(mallinje.getSats());
                omposteringsOppdragslinje.setFradragTillegg(mallinje.getFradragTillegg());
                omposteringsOppdragslinje.setTypeSats(mallinje.getTypeSats());
                omposteringsOppdragslinje.setBrukKjoreplan(mallinje.getBrukKjoreplan());
                omposteringsOppdragslinje.setSaksbehId(mallinje.getSaksbehId());
                omposteringsOppdragslinje.setUtbetalesTilId(mallinje.getUtbetalesTilId());
                omposteringsOppdragslinje.setHenvisning(mallinje.getHenvisning());
                beregningsPerioder.add(opprettBeregningsperiode(omposteringsOppdragslinje, simulerBeregningRequest.getRequest().getOppdrag()));
                oppdragslinjer.get(0).setDatoStatusFom(oppdragslinjer.get(1).getDatoVedtakFom());
            }
            if (!YearMonth.from(opphørFom).isAfter(nesteMåned) && opphørFom.isBefore(LocalDate.parse(oppdragslinjer.get(0).getDatoVedtakFom(), dateTimeFormatter))) {
                Oppdragslinje omposteringsOppdragslinje = new Oppdragslinje();
                omposteringsOppdragslinje.setDatoVedtakFom(mallinje.getDatoStatusFom());
                omposteringsOppdragslinje.setDatoVedtakTom(LocalDate.parse(oppdragslinjer.get(0).getDatoVedtakFom(), dateTimeFormatter).minusDays(1L).toString());
                omposteringsOppdragslinje.setKodeEndringLinje("ENDR");
                omposteringsOppdragslinje.setKodeStatusLinje(KodeStatusLinje.OPPH);
                omposteringsOppdragslinje.setDatoStatusFom(mallinje.getDatoStatusFom());
                omposteringsOppdragslinje.setVedtakId(mallinje.getVedtakId());
                omposteringsOppdragslinje.setDelytelseId(mallinje.getDelytelseId());
                omposteringsOppdragslinje.setKodeKlassifik(mallinje.getKodeKlassifik());
                omposteringsOppdragslinje.setSats(mallinje.getSats());
                omposteringsOppdragslinje.setFradragTillegg(mallinje.getFradragTillegg());
                omposteringsOppdragslinje.setTypeSats(mallinje.getTypeSats());
                omposteringsOppdragslinje.setBrukKjoreplan(mallinje.getBrukKjoreplan());
                omposteringsOppdragslinje.setSaksbehId(mallinje.getSaksbehId());
                omposteringsOppdragslinje.setUtbetalesTilId(mallinje.getUtbetalesTilId());
                omposteringsOppdragslinje.setHenvisning(mallinje.getHenvisning());
                beregningsPerioder.add(opprettBeregningsperiode(omposteringsOppdragslinje, simulerBeregningRequest.getRequest().getOppdrag()));
                oppdragslinjer.get(0).setDatoStatusFom(null);
            }
        }
        for (Oppdragslinje oppdragslinje : oppdragslinjer) {
            LocalDate fom = LocalDate.parse(oppdragslinje.getDatoVedtakFom(), dateTimeFormatter);
            if (!YearMonth.from(fom).isAfter(nesteMåned)) {
                beregningsPerioder.add(opprettBeregningsperiode(oppdragslinje, simulerBeregningRequest.getRequest().getOppdrag()));
            }
        }
    }

    private BeregningsPeriode opprettBeregningsperiode(Oppdragslinje oppdragslinje, Oppdrag oppdrag) {
        BeregningsPeriode beregningsPeriode = new BeregningsPeriode();
        beregningsPeriode.setPeriodeFom(oppdragslinje.getDatoVedtakFom());
        beregningsPeriode.setPeriodeTom(oppdragslinje.getDatoVedtakTom());
        beregningsPeriode.getBeregningStoppnivaa().addAll(opprettBeregningStoppNivaa(oppdragslinje, oppdrag));
        return beregningsPeriode;
    }

    private List<BeregningStoppnivaa> opprettBeregningStoppNivaa(Oppdragslinje oppdragslinje, Oppdrag oppdrag) {
        List<Periode> perioder = splittOppIPeriodePerMnd(oppdragslinje.getDatoVedtakFom(), oppdragslinje.getDatoVedtakTom());
        List<BeregningStoppnivaa> beregningStoppnivaaer = new ArrayList<>();

        YearMonth nesteMåned;
        boolean opphørsLinje;
        if (oppdragslinje.getKodeStatusLinje() != null && oppdragslinje.getKodeStatusLinje().equals(KodeStatusLinje.OPPH))
            {nesteMåned = YearMonth.from(LocalDate.now());
            opphørsLinje = true;}
        else {nesteMåned = YearMonth.from(LocalDate.now().plusMonths(1));
            opphørsLinje = false;}
        for (Periode periode : perioder) {
            if (!YearMonth.from(periode.getFom()).isAfter(nesteMåned)) {
                BeregningStoppnivaa stoppnivaa = new BeregningStoppnivaa();
                stoppnivaa.setKodeFagomraade(oppdrag.getKodeFagomraade());
                if (erRefusjon) {
                    stoppnivaa.setUtbetalesTilId(refunderesOrgNr);
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
                stoppnivaa.setFeilkonto(erOpphør|erOmpostering);
                stoppnivaa.setKid("12345");

                if (opphørsLinje && erOpphørPeriode(periode.getFom(), periode.getTom())){
                    for (int i = 1 ; i <= 3 ; i++){
                        stoppnivaa.getBeregningStoppnivaaDetaljer().add(opprettNegativBeregningStoppNivaaDetaljer(periode, oppdragslinje,i));
                    }
                }
                else if (opphørsLinje && YearMonth.from(periode.getFom()).isBefore(nesteMåned.plusMonths(1))){
                    stoppnivaa.getBeregningStoppnivaaDetaljer().add(opprettNegativBeregningStoppNivaaDetaljer(periode, oppdragslinje, 1));
                    stoppnivaa.getBeregningStoppnivaaDetaljer().add(opprettNegativBeregningStoppNivaaDetaljer(periode, oppdragslinje, 3));
                }
                else if (!opphørsLinje){
                    stoppnivaa.getBeregningStoppnivaaDetaljer().add(opprettBeregningStoppNivaaDetaljer(periode, oppdragslinje));
                }
                if (!stoppnivaa.getBeregningStoppnivaaDetaljer().isEmpty()){
                beregningStoppnivaaer.add(stoppnivaa);}
            }
        }

        return beregningStoppnivaaer;
    }

    private BeregningStoppnivaaDetaljer opprettBeregningStoppNivaaDetaljer(Periode periode, Oppdragslinje oppdragslinje) {
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
        if (erRefusjon && refunderesOrgNr != null){stoppnivaaDetaljer.setRefunderesOrgNr(refunderesOrgNr);}
        else stoppnivaaDetaljer.setRefunderesOrgNr("");

        return stoppnivaaDetaljer;
    }

    private BeregningStoppnivaaDetaljer opprettNegativBeregningStoppNivaaDetaljer(Periode periode, Oppdragslinje oppdragslinje, int sequence) {
        BeregningStoppnivaaDetaljer stoppnivaaDetaljer = new BeregningStoppnivaaDetaljer();

        //Sequence explanation:
        //1.Ytelsen slik den stod original
        //2.Feilutbetalt beløp
        //3.Fjerning av ytelsen fra seqence 1

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
        stoppnivaaDetaljer.setBelop(setBeløp(periode, oppdragslinje, sequence));
        //trekkVedtakId
        stoppnivaaDetaljer.setTrekkVedtakId(0L);
        //stonadId
        stoppnivaaDetaljer.setStonadId("");
        //korrigering
        if (sequence == 2){ stoppnivaaDetaljer.setKorrigering("J"); }
        else { stoppnivaaDetaljer.setKorrigering(""); }
        //tilbakeforing
        stoppnivaaDetaljer.setTilbakeforing(sequence == 3);
        //linjeId
        stoppnivaaDetaljer.setLinjeId(BigInteger.valueOf(21423L));
        //sats
        stoppnivaaDetaljer.setSats(BigDecimal.ZERO);
        //typeSats
        stoppnivaaDetaljer.setTypeSats("");
        //antallSats
        stoppnivaaDetaljer.setAntallSats(BigDecimal.valueOf(0));
        //saksbehId
        stoppnivaaDetaljer.setSaksbehId("5323");
        //uforeGrad
        if (sequence == 3){stoppnivaaDetaljer.setUforeGrad(BigInteger.valueOf(100L));}
        else { stoppnivaaDetaljer.setUforeGrad(BigInteger.ZERO); }
        //kravHaverId ?
        stoppnivaaDetaljer.setKravhaverId("");
        //delytelseId
        stoppnivaaDetaljer.setDelytelseId("");
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
        if (erRefusjon && refunderesOrgNr != null){stoppnivaaDetaljer.setRefunderesOrgNr(refunderesOrgNr);}
        else stoppnivaaDetaljer.setRefunderesOrgNr("");

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

    private BigDecimal setBeløp(Periode periode, Oppdragslinje oppdragslinje, int sequence){
        int antallVirkedager = periode.getAntallVirkedager();
        BigDecimal belop = oppdragslinje.getSats().multiply(BigDecimal.valueOf(antallVirkedager));
        if (sequence == 3){
            return belop.negate();
        }
        else { return belop; }
    }
}
