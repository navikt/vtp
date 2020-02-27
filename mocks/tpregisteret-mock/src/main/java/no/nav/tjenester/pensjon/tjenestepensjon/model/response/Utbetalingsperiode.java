package no.nav.tjenester.pensjon.tjenestepensjon.model.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;

import java.time.LocalDate;

@ApiModel(value = "SimulertPensjon")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Utbetalingsperiode {

    private Integer grad;
    private Double arligUtbetaling;
    private LocalDate datoFom;
    private LocalDate datoTom;
    private String ytelsekode;
    private String mangelfullSimuleringkode;

    @JsonCreator
    public Utbetalingsperiode(
            @JsonProperty(value = "grad", required = true, defaultValue = "0") Integer grad,
            @JsonProperty(value = "arligUtbetaling", required = true, defaultValue = "0") Double arligUtbetaling,
            @JsonProperty(value = "datoFom", required = true) LocalDate datoFom,
            @JsonProperty(value = "datoTom", required = true) LocalDate datoTom,
            @JsonProperty(value = "ytelsekode", required = true) String ytelsekode,
            @JsonProperty(value = "mangelfullSimuleringkode", required = true) String mangelfullSimuleringkode
    ) {
        this.grad = grad;
        this.arligUtbetaling = arligUtbetaling;
        this.datoFom = datoFom;
        this.datoTom = datoTom;
        this.ytelsekode = ytelsekode;
        this.mangelfullSimuleringkode = mangelfullSimuleringkode;
    }

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
