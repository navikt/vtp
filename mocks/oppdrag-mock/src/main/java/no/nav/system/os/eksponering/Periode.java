package no.nav.system.os.eksponering;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;

public class Periode implements Comparable<Periode>{

    private LocalDate fom;
    private LocalDate tom;
    private BigDecimal sats;
    private BigDecimal oldSats;
    private PeriodeType periodeType;
    private String kodeKlassifik;

    Periode(LocalDate fom, LocalDate tom) {
        this.fom = fom;
        this.tom = tom;
    }

    Periode(LocalDate fom, LocalDate tom, BigDecimal sats, String kodeKlassifik){
        this.fom = fom;
        this.tom = tom;
        this.sats = sats;
        this.kodeKlassifik = kodeKlassifik;
    }

    Periode(LocalDate fom, LocalDate tom, BigDecimal sats, String kodeKlassifik, PeriodeType periodeType){
        this.fom = fom;
        this.tom = tom;
        this.sats = sats;
        this.kodeKlassifik = kodeKlassifik;
        this.periodeType = periodeType;
    }

    Periode(LocalDate fom, LocalDate tom, BigDecimal oldSats, BigDecimal sats, String kodeKlassifik){
        this.fom = fom;
        this.tom = tom;
        this.oldSats = oldSats;
        this.sats = sats;
        this.kodeKlassifik = kodeKlassifik;
        if (oldSats.compareTo(sats) <= 0){this.periodeType = PeriodeType.ØKNING;}
        else {this.periodeType = PeriodeType.REDUKSJON;}
    }

    public LocalDate getFom() {
        return fom;
    }

    public LocalDate getTom() {
        return tom;
    }

    public BigDecimal getSats(){
        return sats;
    }

    public BigDecimal getOldSats(){
        return oldSats;
    }

    public PeriodeType getPeriodeType(){
        return periodeType;
    }

    protected void setPeriodeType(PeriodeType periodeType) {
        this.periodeType = periodeType;
    }

    public BigDecimal getFeilSats(){
        if (!periodeType.equals(PeriodeType.OPPH)){return BigDecimal.ZERO;}
        return oldSats.subtract(sats);
    }

    public String getKodeKlassifik() {
        return kodeKlassifik;
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

    @Override
    public int compareTo(Periode comparePeriode) {
        return this.fom.compareTo(comparePeriode.getFom());
    }
}
