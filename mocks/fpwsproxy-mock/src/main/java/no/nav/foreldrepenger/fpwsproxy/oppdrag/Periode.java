package no.nav.foreldrepenger.fpwsproxy.oppdrag;

import java.time.DayOfWeek;
import java.time.LocalDate;

import no.nav.foreldrepenger.kontrakter.simulering.request.KodeKlassifik;
import no.nav.foreldrepenger.kontrakter.simulering.request.SatsDto;

public class Periode implements Comparable<Periode>{

    private LocalDate fom;
    private LocalDate tom;
    private SatsDto sats;
    private SatsDto oldSats;
    private PeriodeType periodeType;
    private KodeKlassifik kodeKlassifik;


    Periode(LocalDate fom, LocalDate tom, SatsDto sats, KodeKlassifik kodeKlassifik){
        this.fom = fom;
        this.tom = tom;
        this.sats = sats;
        this.kodeKlassifik = kodeKlassifik;
    }

    Periode(LocalDate fom, LocalDate tom, SatsDto sats, KodeKlassifik kodeKlassifik, PeriodeType periodeType){
        this.fom = fom;
        this.tom = tom;
        this.sats = sats;
        this.kodeKlassifik = kodeKlassifik;
        this.periodeType = periodeType;
    }

    Periode(LocalDate fom, LocalDate tom, SatsDto oldSats, SatsDto sats, KodeKlassifik kodeKlassifik){
        this.fom = fom;
        this.tom = tom;
        this.oldSats = oldSats;
        this.sats = sats;
        this.kodeKlassifik = kodeKlassifik;
        if (erNySatsLikEllerStørreEnnGammelSats(oldSats, sats)) {
            this.periodeType = PeriodeType.ØKNING;
        } else {
            this.periodeType = PeriodeType.REDUKSJON;
        }
    }

    public Periode(LocalDate fom, LocalDate tom, SatsDto sats, SatsDto oldSats, PeriodeType periodeType, KodeKlassifik kodeKlassifik) {
        this.fom = fom;
        this.tom = tom;
        this.sats = sats;
        this.oldSats = oldSats;
        this.periodeType = periodeType;
        this.kodeKlassifik = kodeKlassifik;
    }

    public LocalDate getFom() {
        return fom;
    }

    public LocalDate getTom() {
        return tom;
    }

    public void setTom(LocalDate tom) {
        this.tom = tom;
    }

    public SatsDto getSats(){
        return sats;
    }

    public SatsDto getOldSats(){
        return oldSats;
    }

    public PeriodeType getPeriodeType(){
        return periodeType;
    }

    protected void setPeriodeType(PeriodeType periodeType) {
        this.periodeType = periodeType;
    }

    public KodeKlassifik getKodeKlassifik() {
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

    private static boolean erNySatsLikEllerStørreEnnGammelSats(SatsDto oldSats, SatsDto sats) {
        if (sats == null && oldSats == null) {
            return true;
        }
        if (sats == null || oldSats == null) {
            return false;
        }
        return oldSats.verdi() <= sats.verdi();
    }

    @Override
    public int compareTo(Periode comparePeriode) {
        return this.fom.compareTo(comparePeriode.getFom());
    }
}
