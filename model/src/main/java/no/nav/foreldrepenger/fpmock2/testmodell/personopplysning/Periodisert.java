package no.nav.foreldrepenger.fpmock2.testmodell.personopplysning;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class Periodisert {

    @JsonProperty("fom")
    private LocalDate fom;

    @JsonProperty("tom")
    private LocalDate tom;

    @JsonProperty("endringstype")
    private Endringstype endringstype;
    
    @JsonProperty("endringstidspunkt")
    private LocalDate endringstidspunkt;

    protected Periodisert() {
    }

    protected Periodisert(LocalDate fom, LocalDate tom) {
        this.fom = fom;
        this.tom = tom;
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

    public String getEndringstype() {
        return endringstype == null ? null : endringstype.name();
    }
    
    public LocalDate getEndringstidspunkt() {
        return endringstidspunkt;
    }

    @Override
    public Periodisert clone() {
        try {
            return (Periodisert) super.clone();
        } catch (CloneNotSupportedException e) {
            // skal ikke kunne skje
            throw new UnsupportedOperationException(e);
        }
    }

    public enum Endringstype {
        /** matcher verdier i TPS Endringstype, derav lower-case */
        ny, endret, utgaatt, slettet;
    }

}
