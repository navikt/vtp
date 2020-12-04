package no.nav.system.os.eksponering;

import java.math.BigDecimal;
import java.math.BigInteger;
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

    PeriodeGenerator periodeGenerator = new PeriodeGenerator();

    Boolean erOpphør;
    Boolean erOmpostering;
    Boolean erRefusjon;
    String refunderesOrgNr;

    List<Periode> oppdragsPeriodeList = new ArrayList<>();

    public SimulerBeregningResponse opprettSimuleringsResultat(SimulerBeregningRequest simulerBeregningRequest) {
        this.erOpphør = erOpphør(simulerBeregningRequest.getRequest().getOppdrag().getOppdragslinje());
        this.erOmpostering = erOmpostering(simulerBeregningRequest.getRequest().getOppdrag());
        if (erOmpostering){
            if (!simulerBeregningRequest.getRequest().getOppdrag().getOmpostering().getDatoOmposterFom().equals(simulerBeregningRequest.getRequest().getOppdrag().getOppdragslinje().get(0).getDatoStatusFom())){
                throw new IllegalArgumentException("datoOmposterFom (" + simulerBeregningRequest.getRequest().getOppdrag().getOmpostering().getDatoOmposterFom() + ") og datoStatusFom i første periode(" + simulerBeregningRequest.getRequest().getOppdrag().getOppdragslinje().get(0).getDatoStatusFom() + ")må være like");
            }
        }
        this.erRefusjon = erRefusjon(simulerBeregningRequest.getRequest().getOppdrag().getOppdragslinje());

        this.oppdragsPeriodeList = periodeGenerator.genererPerioder(simulerBeregningRequest.getRequest().getOppdrag().getOppdragslinje());

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
        List<BeregningsPeriode> beregningsPerioder = beregning.getBeregningsPeriode();

        for (Periode oppdragsperiode : oppdragsPeriodeList) {
            if (!YearMonth.from(oppdragsperiode.getFom()).isAfter(nesteMåned)) {
                beregningsPerioder.add(opprettBeregningsperiode(oppdragsperiode, simulerBeregningRequest.getRequest().getOppdrag()));
            }
        }
    }

    private BeregningsPeriode opprettBeregningsperiode(Periode oppdragsperiode, Oppdrag oppdrag) {
        BeregningsPeriode beregningsPeriode = new BeregningsPeriode();
        beregningsPeriode.setPeriodeFom(oppdragsperiode.getFom().format(dateTimeFormatter));
        beregningsPeriode.setPeriodeTom(oppdragsperiode.getTom().format(dateTimeFormatter));
        beregningsPeriode.getBeregningStoppnivaa().addAll(opprettBeregningStoppNivaa(oppdragsperiode, oppdrag));
        return beregningsPeriode;
    }

    private List<BeregningStoppnivaa> opprettBeregningStoppNivaa(Periode oppdragsperiode, Oppdrag oppdrag) {
        List<Periode> perioder = splittOppIPeriodePerMnd(oppdragsperiode);
        List<BeregningStoppnivaa> beregningStoppnivaaer = new ArrayList<>();

        YearMonth nesteMåned;
        if (oppdragsperiode.getPeriodeType().equals(PeriodeType.OPPH) || oppdragsperiode.getPeriodeType().equals(PeriodeType.REDUKSJON))
        {nesteMåned = YearMonth.from(LocalDate.now());}
        else {nesteMåned = YearMonth.from(LocalDate.now().plusMonths(1));}
        for (Periode periode : perioder) {
            if (!YearMonth.from(periode.getFom()).isAfter(nesteMåned)) {
                BeregningStoppnivaa stoppnivaa = new BeregningStoppnivaa();
                stoppnivaa.setKodeFagomraade(oppdrag.getKodeFagomraade());
                if (erRefusjon) {
                    stoppnivaa.setUtbetalesTilId(refunderesOrgNr);
                    stoppnivaa.setUtbetalesTilNavn("DUMMY FIRMA");
                } else {
                    stoppnivaa.setUtbetalesTilId(oppdrag.getOppdragGjelderId());
                    stoppnivaa.setUtbetalesTilNavn("DUMMY");
                }
                stoppnivaa.setBehandlendeEnhet("8052");
                LocalDate forfallsdato = periode.getFom().isBefore(LocalDate.now()) ? LocalDate.now() : periode.getTom().plusDays(1);
                stoppnivaa.setForfall(dateTimeFormatter.format(forfallsdato));
                stoppnivaa.setOppdragsId(1234L);
                stoppnivaa.setStoppNivaaId(BigInteger.ONE);
                stoppnivaa.setFagsystemId(oppdrag.getFagsystemId());
                stoppnivaa.setBilagsType("U");
                stoppnivaa.setFeilkonto(oppdragsperiode.getPeriodeType().equals(PeriodeType.OPPH));
                stoppnivaa.setKid("12345");

                if (oppdragsperiode.getPeriodeType().equals(PeriodeType.OPPH)){
                    for (int i = 1 ; i <= 3 ; i++){
                        stoppnivaa.getBeregningStoppnivaaDetaljer().add(opprettNegativBeregningStoppNivaaDetaljer(periode, oppdragsperiode, i));
                    }
                }
                else if (oppdragsperiode.getPeriodeType().equals(PeriodeType.REDUKSJON) && YearMonth.from(periode.getFom()).isBefore(nesteMåned)){
                    for (int i = 1 ; i <= 3 ; i++){
                        stoppnivaa.getBeregningStoppnivaaDetaljer().add(opprettNegativBeregningStoppNivaaDetaljer(periode, oppdragsperiode, i));
                    }
                }
                else if (oppdragsperiode.getPeriodeType().equals(PeriodeType.ØKNING) && YearMonth.from(periode.getFom()).isBefore(nesteMåned)){
                    stoppnivaa.getBeregningStoppnivaaDetaljer().add(opprettNegativBeregningStoppNivaaDetaljer(periode, oppdragsperiode, 1));
                    stoppnivaa.getBeregningStoppnivaaDetaljer().add(opprettNegativBeregningStoppNivaaDetaljer(periode, oppdragsperiode, 3));
                    stoppnivaa.getBeregningStoppnivaaDetaljer().add(opprettBeregningStoppNivaaDetaljer(periode, oppdragsperiode));
                }
                else if (!oppdragsperiode.getPeriodeType().equals(PeriodeType.OPPH)){
                    stoppnivaa.getBeregningStoppnivaaDetaljer().add(opprettBeregningStoppNivaaDetaljer(periode, oppdragsperiode));
                }
                if (!stoppnivaa.getBeregningStoppnivaaDetaljer().isEmpty()){
                    beregningStoppnivaaer.add(stoppnivaa);}
            }
        }

        return beregningStoppnivaaer;
    }

    private BeregningStoppnivaaDetaljer opprettBeregningStoppNivaaDetaljer(Periode periode, Periode oppdragsperiode) {
        BeregningStoppnivaaDetaljer stoppnivaaDetaljer = new BeregningStoppnivaaDetaljer();

        stoppnivaaDetaljer.setFaktiskFom(dateTimeFormatter.format(periode.getFom()));
        stoppnivaaDetaljer.setFaktiskTom(dateTimeFormatter.format(periode.getTom()));
        stoppnivaaDetaljer.setKontoStreng("1235432");
        stoppnivaaDetaljer.setBehandlingskode("2");
        stoppnivaaDetaljer.setBelop(oppdragsperiode.getSats().multiply(BigDecimal.valueOf(periode.getAntallVirkedager())));
        stoppnivaaDetaljer.setTrekkVedtakId(0L);
        stoppnivaaDetaljer.setStonadId("1234");
        stoppnivaaDetaljer.setKorrigering("");
        stoppnivaaDetaljer.setTilbakeforing(false);
        stoppnivaaDetaljer.setLinjeId(BigInteger.valueOf(21423L));
        stoppnivaaDetaljer.setSats(oppdragsperiode.getSats());
        stoppnivaaDetaljer.setTypeSats("DAG");
        stoppnivaaDetaljer.setAntallSats(BigDecimal.valueOf(periode.getAntallVirkedager()));
        stoppnivaaDetaljer.setSaksbehId("5323");
        stoppnivaaDetaljer.setUforeGrad(BigInteger.valueOf(100L));
        stoppnivaaDetaljer.setKravhaverId("");
        stoppnivaaDetaljer.setDelytelseId("3523");
        stoppnivaaDetaljer.setBostedsenhet("4643");
        stoppnivaaDetaljer.setSkykldnerId("");
        stoppnivaaDetaljer.setKlassekode(oppdragsperiode.getKodeKlassifik());
        stoppnivaaDetaljer.setKlasseKodeBeskrivelse("DUMMY");
        stoppnivaaDetaljer.setTypeKlasse("YTEL");
        stoppnivaaDetaljer.setTypeKlasseBeskrivelse("DUMMY");
        if (erRefusjon && refunderesOrgNr != null){stoppnivaaDetaljer.setRefunderesOrgNr(refunderesOrgNr);}
        else stoppnivaaDetaljer.setRefunderesOrgNr("");

        return stoppnivaaDetaljer;
    }

    private BeregningStoppnivaaDetaljer opprettNegativBeregningStoppNivaaDetaljer(Periode periode, Periode oppdragsperiode, int sequence) {
        BeregningStoppnivaaDetaljer stoppnivaaDetaljer = new BeregningStoppnivaaDetaljer();

        //Sequence explanation:
        //1.Ytelsen slik den stod original
        //2.Feilutbetalt beløp
        //3.Fjerning av ytelsen fra seqence 1

        stoppnivaaDetaljer.setFaktiskFom(dateTimeFormatter.format(periode.getFom()));
        stoppnivaaDetaljer.setFaktiskTom(dateTimeFormatter.format(periode.getTom()));
        stoppnivaaDetaljer.setKontoStreng("1235432");
        if (sequence == 2){stoppnivaaDetaljer.setBehandlingskode("0");}
        else {stoppnivaaDetaljer.setBehandlingskode("2");}
        stoppnivaaDetaljer.setBelop(setBeløp(periode.getAntallVirkedager(), oppdragsperiode, sequence));
        stoppnivaaDetaljer.setTrekkVedtakId(0L);
        stoppnivaaDetaljer.setStonadId("");
        if (sequence == 2){ stoppnivaaDetaljer.setKorrigering("J"); }
        else { stoppnivaaDetaljer.setKorrigering(""); }
        stoppnivaaDetaljer.setTilbakeforing(sequence == 3);
        stoppnivaaDetaljer.setLinjeId(BigInteger.valueOf(21423L));
        stoppnivaaDetaljer.setSats(BigDecimal.ZERO);
        stoppnivaaDetaljer.setTypeSats("");
        stoppnivaaDetaljer.setAntallSats(BigDecimal.valueOf(0));
        stoppnivaaDetaljer.setSaksbehId("5323");
        if (sequence == 3){stoppnivaaDetaljer.setUforeGrad(BigInteger.valueOf(100L));}
        else { stoppnivaaDetaljer.setUforeGrad(BigInteger.ZERO); }
        stoppnivaaDetaljer.setKravhaverId("");
        stoppnivaaDetaljer.setDelytelseId("");
        stoppnivaaDetaljer.setBostedsenhet("4643");
        stoppnivaaDetaljer.setSkykldnerId("");
        if (sequence == 2){ stoppnivaaDetaljer.setKlassekode("KL_KODE_FEIL_KORTTID"); }
        else { stoppnivaaDetaljer.setKlassekode(oppdragsperiode.getKodeKlassifik()); }
        stoppnivaaDetaljer.setKlasseKodeBeskrivelse("DUMMY");
        if (sequence == 2) { stoppnivaaDetaljer.setTypeKlasse("FEIL"); }
        else if (sequence == 3) { stoppnivaaDetaljer.setTypeKlasse("MOTP"); }
        else { stoppnivaaDetaljer.setTypeKlasse("YTEL"); }
        stoppnivaaDetaljer.setTypeKlasseBeskrivelse("DUMMY");
        if (erRefusjon && refunderesOrgNr != null){stoppnivaaDetaljer.setRefunderesOrgNr(refunderesOrgNr);}
        else stoppnivaaDetaljer.setRefunderesOrgNr("");

        return stoppnivaaDetaljer;
    }

    static List<Periode> splittOppIPeriodePerMnd(Periode oppdragsperiode) {
        List<Periode> perioder = new ArrayList<>();

        if (oppdragsperiode.getTom().isBefore(oppdragsperiode.getFom())) {
            throw new IllegalArgumentException("Startdato " + oppdragsperiode.getFom().format(dateTimeFormatter) + " kan ikke være etter sluttdato " + oppdragsperiode.getTom().format(dateTimeFormatter));
        }

        LocalDate dato = oppdragsperiode.getFom();
        while (!dato.isAfter(oppdragsperiode.getTom())) {
            LocalDate sisteDagIMnd = YearMonth.from(dato).atEndOfMonth();
            if (sisteDagIMnd.isBefore(oppdragsperiode.getTom())) {
                perioder.add(new Periode(dato, sisteDagIMnd));
                dato = sisteDagIMnd.plusDays(1);
            } else {
                perioder.add(new Periode(dato, oppdragsperiode.getTom()));
                dato = oppdragsperiode.getTom().plusDays(1);
            }
        }
        return perioder;
    }

    private BigDecimal setBeløp(int antallVirkedager, Periode oppdragsperiode, int sequence){
        BigDecimal belop = oppdragsperiode.getSats().multiply(BigDecimal.valueOf(antallVirkedager));

        if (oppdragsperiode.getPeriodeType().equals(PeriodeType.OPPH)){
            if (sequence == 3){
                return belop.negate();
            }
            else { return belop; }
        }
        else {
            if (sequence == 1){
                return oppdragsperiode.getOldSats().multiply(BigDecimal.valueOf(antallVirkedager));
            }
            else if (sequence == 2){
                return belop.subtract(oppdragsperiode.getOldSats().multiply(BigDecimal.valueOf(antallVirkedager))).negate();
            }
            else if (sequence == 3){
                return oppdragsperiode.getOldSats().multiply(BigDecimal.valueOf(antallVirkedager)).negate();
            }
            else return belop;
        }
    }
}
