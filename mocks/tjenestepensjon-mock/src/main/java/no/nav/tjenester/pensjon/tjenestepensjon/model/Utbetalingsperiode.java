package no.nav.tjenester.pensjon.tjenestepensjon.model;

import java.time.LocalDate;

public class Utbetalingsperiode {
    private Integer grad;
    private Double arligUtbetaling;
    private LocalDate datoFom;
    private LocalDate datoTom;
    private String ytelsekode;
    private String mangelfullSimuleringkode;

    public Integer getGrad() {
        return grad;
    }

    public void setGrad(Integer grad) {
        this.grad = grad;
    }

    public Double getArligUtbetaling() {
        return arligUtbetaling;
    }

    public void setArligUtbetaling(Double arligUtbetaling) {
        this.arligUtbetaling = arligUtbetaling;
    }

    public LocalDate getDatoFom() {
        return datoFom;
    }

    public void setDatoFom(LocalDate datoFom) {
        this.datoFom = datoFom;
    }

    public LocalDate getDatoTom() {
        return datoTom;
    }

    public void setDatoTom(LocalDate datoTom) {
        this.datoTom = datoTom;
    }

    public String getYtelsekode() {
        return ytelsekode;
    }

    public void setYtelsekode(String ytelsekode) {
        this.ytelsekode = ytelsekode;
    }

    public String getMangelfullSimuleringkode() {
        return mangelfullSimuleringkode;
    }

    public void setMangelfullSimuleringkode(String mangelfullSimuleringkode) {
        this.mangelfullSimuleringkode = mangelfullSimuleringkode;
    }
}
