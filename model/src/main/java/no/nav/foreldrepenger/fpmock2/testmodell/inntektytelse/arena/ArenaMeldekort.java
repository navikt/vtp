package no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.arena;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ArenaMeldekort {

    @JsonProperty("fom")
    private LocalDate fom;
    
    @JsonProperty("tom")
    private LocalDate tom;
    
    @JsonProperty("dagsats")
    private BigDecimal dagsats;
    
    @JsonProperty("beløp")
    private BigDecimal beløp;
    
    @JsonProperty("utbetalingsgrad")
    private BigDecimal utbetalingsgrad;

    public LocalDate getFom() {
        return fom;
    }

    public void setFom(LocalDate fom) {
        this.fom = fom;
    }

    public LocalDate getTom() {
        return tom;
    }

    public void setTom(LocalDate tom) {
        this.tom = tom;
    }

    public BigDecimal getDagsats() {
        return dagsats;
    }

    public void setDagsats(BigDecimal dagsats) {
        this.dagsats = dagsats;
    }

    public BigDecimal getBeløp() {
        return beløp;
    }

    public void setBeløp(BigDecimal beløp) {
        this.beløp = beløp;
    }

    public BigDecimal getUtbetalingsgrad() {
        return utbetalingsgrad;
    }

    public void setUtbetalingsgrad(BigDecimal utbetalingsgrad) {
        this.utbetalingsgrad = utbetalingsgrad;
    }
}
