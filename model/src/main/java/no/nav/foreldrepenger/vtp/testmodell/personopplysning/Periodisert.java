package no.nav.foreldrepenger.vtp.testmodell.personopplysning;

import java.time.LocalDate;

public abstract class Periodisert {
    private LocalDate fom;
    private LocalDate tom;
    private Endringstype endringstype;
    private LocalDate endringstidspunkt;

    protected Periodisert() {
    }

    protected Periodisert(LocalDate fom, LocalDate tom) {
        this(fom, tom, null, null);
    }

    protected Periodisert(LocalDate fom, LocalDate tom, Endringstype endringstype, LocalDate endringstidspunkt) {
        this.fom = fom;
        this.tom = tom;
        this.endringstype = endringstype;
        this.endringstidspunkt = endringstidspunkt;
    }

    public LocalDate getFom() {
        return fom;
    }

    public LocalDate getTom() {
        return tom;
    }

    public void setFom(LocalDate fom) {
        this.fom = fom;
    }

    public void setTom(LocalDate tom) {
        this.tom = tom;
    }

    public Endringstype getEndringstype() {
        return endringstype;
    }

    public LocalDate getEndringstidspunkt() {
        return endringstidspunkt;
    }

    public enum Endringstype {
        /** matcher verdier i TPS Endringstype, derav lower-case */
        ny, endret, utgaatt, slettet
    }

}
