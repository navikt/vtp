package no.nav.foreldrepenger.fpwsproxy.oppdrag;

import static java.util.Collections.emptyList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import no.nav.foreldrepenger.kontrakter.simulering.request.KodeEndringLinje;
import no.nav.foreldrepenger.kontrakter.simulering.request.Oppdragslinje150Dto;

// TODO: Denne er en kopi av tidligere. Rent kaos. Må ryddes opp i...
public class PeriodeGenerator {

    private final List<Periode> opphørsPerioder = new ArrayList<>();
    private final List<Periode> ytelsesPerioder = new ArrayList<>();

    public List<Periode> genererPerioder(List<Oppdragslinje150Dto> oppdragslinjeList) {

        lagOpphørOgYtelse(oppdragslinjeList);
        Collections.sort(ytelsesPerioder);
        Collections.sort(opphørsPerioder);

        if (opphørsPerioder.isEmpty() && ytelsesPerioder.isEmpty()) {
            return emptyList();
        }

        if (opphørsPerioder.isEmpty()){
            for (Periode periode : ytelsesPerioder){
                periode.setPeriodeType(PeriodeType.YTEL);
            }
            return ytelsesPerioder;
        }

        if (ytelsesPerioder.isEmpty()){
            for (Periode periode : opphørsPerioder){
                periode.setPeriodeType(PeriodeType.OPPH);
            }
            return opphørsPerioder;
        }

        List<Periode> perioder = new ArrayList<>(definerYtelseOgOmpostPerioder());
        perioder.addAll(definerOpphørPerioder());
        Collections.sort(perioder);
        return perioder;
    }

    private List<Periode> definerOpphørPerioder(){
        List<Periode> periodeList = new ArrayList<>();
        List<Periode> opphørPerioderTemp = new ArrayList<>(opphørsPerioder);
        List<Periode> removeOpphørPerioder = new ArrayList<>();
        List<Periode> addOpphørPerioder = new ArrayList<>();

        while (!opphørPerioderTemp.isEmpty()){
            opphørloop:
            for (Periode opphør : opphørPerioderTemp){
                //ytelseloop:
                for (Periode ytelse : ytelsesPerioder){
                    //Scenario 1
                    if (opphør.getFom().isBefore(ytelse.getFom()) && opphør.getTom().isBefore(ytelse.getFom())){
                        periodeList.add(new Periode(opphør.getFom(), opphør.getTom(), opphør.getSats(), opphør.getKodeKlassifik(), PeriodeType.OPPH));
                        removeOpphørPerioder.add(opphør);
                        continue opphørloop;
                    //Scenario 2
                    } else if (opphør.getFom().isBefore(ytelse.getFom()) && opphør.getTom().isEqual(ytelse.getFom())){
                        periodeList.add(new Periode(opphør.getFom(), opphør.getTom().minusDays(1), opphør.getSats(), opphør.getKodeKlassifik(), PeriodeType.OPPH));
                        removeOpphørPerioder.add(opphør);
                        continue opphørloop;
                    //Scenario 3,4 og 5
                    } else if (opphør.getFom().isBefore(ytelse.getFom()) && opphør.getTom().isAfter(ytelse.getFom())){
                        periodeList.add(new Periode(opphør.getFom(), ytelse.getFom().minusDays(1), opphør.getSats(), opphør.getKodeKlassifik(), PeriodeType.OPPH));
                        removeOpphørPerioder.add(opphør);
                    //Scenario 3 & 4
                        if (!opphør.getTom().isAfter(ytelse.getTom())){
                            continue opphørloop;
                        }
                    //Scenario 5
                        else { // if opphør.getTom().isAfter(ytelse.getTom())
                            addOpphørPerioder.add(new Periode(ytelse.getTom().plusDays(1), opphør.getTom(), opphør.getSats(), opphør.getKodeKlassifik()));
                            continue opphørloop;
                        }
                    }
                    //Scenario 6 & 7
                    else if (opphør.getFom().isEqual(ytelse.getFom()) && !opphør.getTom().isAfter(ytelse.getTom())){
                        removeOpphørPerioder.add(opphør);
                        continue opphørloop;
                    }
                    //Scenario 8
                    else if (opphør.getFom().isEqual(ytelse.getFom()) && opphør.getTom().isAfter(ytelse.getTom())){
                        removeOpphørPerioder.add(opphør);
                        addOpphørPerioder.add(new Periode(ytelse.getTom().plusDays(1), opphør.getTom(), opphør.getSats(), opphør.getKodeKlassifik()));
                        continue opphørloop;
                    }
                    //Scenario 9
                    else if (opphør.getFom().isAfter(ytelse.getFom()) && !opphør.getTom().isAfter(ytelse.getTom())){
                        removeOpphørPerioder.add(opphør);
                        continue opphørloop;
                    }
                    //Scenario 10 & 11
                    else if (opphør.getFom().isAfter(ytelse.getFom()) && !opphør.getFom().isAfter(ytelse.getTom())){
                        removeOpphørPerioder.add(opphør);
                        addOpphørPerioder.add(new Periode(ytelse.getTom().plusDays(1), opphør.getTom(), opphør.getSats(), opphør.getKodeKlassifik()));
                        continue opphørloop;
                    }
                    //Scenario 12 - sjekkes mot neste ytelsesperiode
                }
                //Scenario 12 - hvis den ikke treffer noen ytelsesperioder.
                periodeList.add(new Periode(opphør.getFom(), opphør.getTom(), opphør.getSats(), opphør.getKodeKlassifik(), PeriodeType.OPPH));
                removeOpphørPerioder.add(opphør);
            }
            opphørPerioderTemp.removeAll(removeOpphørPerioder);
            removeOpphørPerioder.clear();
            opphørPerioderTemp.addAll(addOpphørPerioder);
            addOpphørPerioder.clear();
            Collections.sort(opphørPerioderTemp);
        }
        Collections.sort(periodeList);
        return periodeList;
    }

    private List<Periode> definerYtelseOgOmpostPerioder() {
        List<Periode> periodeList = new ArrayList<>();
        List<Periode> ytelsesPerioderTemp = new ArrayList<>(ytelsesPerioder);
        List<Periode> removeYtelsesPerioder = new ArrayList<>();
        List<Periode> addYtelsesPerioder = new ArrayList<>();

        while (!ytelsesPerioderTemp.isEmpty()) {
            ytelseloop:
            for (Periode ytelse : ytelsesPerioderTemp) {
                //opphørloop:
                for (Periode opphør : opphørsPerioder) {
                    //Scenarioer beskrevet i readme.md - Ytelse sjekkes mot opphør
                    //Scenario 1
                    if (ytelse.getFom().isBefore(opphør.getFom()) && ytelse.getTom().isBefore(opphør.getFom())) {
                        periodeList.add(new Periode(ytelse.getFom(), ytelse.getTom(), ytelse.getSats(), ytelse.getKodeKlassifik(), PeriodeType.YTEL));
                        removeYtelsesPerioder.add(ytelse);
                        continue ytelseloop;
                    //Scenario 2
                    } else if (ytelse.getFom().isBefore(opphør.getFom()) && ytelse.getTom().isEqual(opphør.getFom())) {
                        periodeList.add(new Periode(ytelse.getFom(), ytelse.getTom().minusDays(1), ytelse.getSats(), ytelse.getKodeKlassifik(), PeriodeType.YTEL));
                        periodeList.add(new Periode(ytelse.getTom(), ytelse.getTom(), opphør.getSats(), ytelse.getSats(), ytelse.getKodeKlassifik()));
                        removeYtelsesPerioder.add(ytelse);
                        continue ytelseloop;
                    //Scenario 3,4 og 5
                    } else if (ytelse.getFom().isBefore(opphør.getFom()) && ytelse.getTom().isAfter(opphør.getFom())) {
                        periodeList.add(new Periode(ytelse.getFom(), opphør.getFom().minusDays(1), ytelse.getSats(), ytelse.getKodeKlassifik(), PeriodeType.YTEL));
                    //Scenario 3 & 4
                        if (!ytelse.getTom().isAfter(opphør.getTom())) {
                            periodeList.add(new Periode(opphør.getFom(), ytelse.getTom(), opphør.getSats(), ytelse.getSats(), ytelse.getKodeKlassifik()));
                            removeYtelsesPerioder.add(ytelse);
                            continue ytelseloop;
                    //Scenario 5
                        } else { //if (ytelse.getTom().isAfter(opphør.getTom())
                            periodeList.add(new Periode(opphør.getFom(), opphør.getTom(), opphør.getSats(), ytelse.getSats(), ytelse.getKodeKlassifik()));
                            addYtelsesPerioder.add(new Periode(opphør.getTom().plusDays(1), ytelse.getFom(), ytelse.getSats(), ytelse.getKodeKlassifik())); //Nytt objekt på slutten til samme loop
                            removeYtelsesPerioder.add(ytelse);
                            continue ytelseloop;
                        }
                    //Scenario 6 & 7
                    } else if (ytelse.getFom().isEqual(opphør.getFom()) && !ytelse.getTom().isAfter(opphør.getTom())) {
                        periodeList.add(new Periode(ytelse.getFom(), ytelse.getTom(), opphør.getSats(), ytelse.getSats(), ytelse.getKodeKlassifik()));
                        removeYtelsesPerioder.add(ytelse);
                        continue ytelseloop;
                    //Scenario 8
                    } else if (ytelse.getFom().isEqual(opphør.getFom()) && ytelse.getTom().isAfter(opphør.getTom())) {
                        periodeList.add(new Periode(ytelse.getFom(), opphør.getTom(), opphør.getSats(), ytelse.getSats(), ytelse.getKodeKlassifik()));
                        addYtelsesPerioder.add(new Periode(opphør.getTom().plusDays(1), ytelse.getTom(), ytelse.getSats(), ytelse.getKodeKlassifik())); ////Nytt objekt på slutten til samme loop
                        removeYtelsesPerioder.add(ytelse);
                        continue ytelseloop;
                    //Scenario 9
                    } else if (ytelse.getFom().isAfter(opphør.getFom()) && !ytelse.getTom().isAfter(opphør.getTom())) { //ytelse.getFom() er implisit før opphør.getTom() da ytelse.getFom() ikke kan være etter ytelse.getTom()
                        periodeList.add(new Periode(ytelse.getFom(), ytelse.getTom(), opphør.getSats(), ytelse.getSats(), ytelse.getKodeKlassifik()));
                        removeYtelsesPerioder.add(ytelse);
                        continue ytelseloop;
                    //Scenario 10 & 11
                    } else if (ytelse.getFom().isAfter(opphør.getFom()) && !ytelse.getFom().isAfter(opphør.getTom())) { //ytelse.getTom() er implisit after opphør.getTom() ellers ville den truffet forrige if-statement
                        periodeList.add(new Periode(ytelse.getFom(), opphør.getTom(), opphør.getSats(), ytelse.getSats(), ytelse.getKodeKlassifik()));
                        addYtelsesPerioder.add(new Periode(opphør.getTom().plusDays(1), ytelse.getTom(), ytelse.getSats(), ytelse.getKodeKlassifik()));
                        removeYtelsesPerioder.add(ytelse);
                        continue ytelseloop;
                    }
                    //Scenario 12 - prøver igjen mot neste opphørsperiode
                }
                //Scenario 12 - hvis ytelsen ikke treffer noen opphørsperioder.
                periodeList.add(new Periode(ytelse.getFom(), ytelse.getTom(), ytelse.getSats(), ytelse.getKodeKlassifik(), PeriodeType.YTEL)); //Hvis ytelsen ikke treffer noen opphør
                removeYtelsesPerioder.add(ytelse);
            }
            ytelsesPerioderTemp.removeAll(removeYtelsesPerioder);
            removeYtelsesPerioder.clear();
            ytelsesPerioderTemp.addAll(addYtelsesPerioder);
            addYtelsesPerioder.clear();
            Collections.sort(ytelsesPerioderTemp);
        }
        return periodeList;
    }

    private void lagOpphørOgYtelse(List<Oppdragslinje150Dto> oppdragslinjeList) {
        for (var oppdragslinje : oppdragslinjeList){
            if (oppdragslinje.kodeEndringLinje() == KodeEndringLinje.ENDR) {
                this.opphørsPerioder.add(new Periode(
                        oppdragslinje.datoStatusFom() != null ? oppdragslinje.datoStatusFom() : oppdragslinje.getDatoVedtakFom(),
                        oppdragslinje.getDatoVedtakTom(),
                        oppdragslinje.sats(),
                        oppdragslinje.kodeKlassifik()
                ));
            } else if (oppdragslinje.kodeEndringLinje() == KodeEndringLinje.NY) {
                this.ytelsesPerioder.add(new Periode(
                        oppdragslinje.getDatoVedtakFom(),
                        oppdragslinje.getDatoVedtakTom(),
                        oppdragslinje.sats(),
                        oppdragslinje.kodeKlassifik()
                ));
            } else {
                throw new IllegalArgumentException("Forventet kodeEndringLinje NY eller ENDR. Verdi var: " + oppdragslinje.kodeEndringLinje());
            }
        }
    }
}
