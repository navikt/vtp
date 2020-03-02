package no.nav.tjenester.pensjon.tjenestepensjon.model.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class Stillingsprosent {

    private LocalDate datoFom;
    private LocalDate datoTom;
    private Double stillingsprosent;
    private Integer aldersgrense;
    private String faktiskHovedlonn;
    private String stillingsuavhengigTilleggslonn;

    @JsonCreator
    public Stillingsprosent(
            @JsonProperty(value = "datoFom") LocalDate datoFom,
            @JsonProperty(value = "datoFom", required = true) LocalDate datoTom,
            @JsonProperty(value = "datoFom") Double stillingsprosent,
            @JsonProperty(value = "datoFom") Integer aldersgrense,
            @JsonProperty(value = "datoFom") String faktiskHovedlonn,
            @JsonProperty(value = "datoFom") String stillingsuavhengigTilleggslonn
    ) {
        this.datoFom = datoFom;
        this.datoTom = datoTom;
        this.stillingsprosent = stillingsprosent;
        this.aldersgrense = aldersgrense;
        this.faktiskHovedlonn = faktiskHovedlonn;
        this.stillingsuavhengigTilleggslonn = stillingsuavhengigTilleggslonn;
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

    public Double getStillingsprosent() {
        return stillingsprosent;
    }

    public void setStillingsprosent(Double stillingsprosent) {
        this.stillingsprosent = stillingsprosent;
    }

    public Integer getAldersgrense() {
        return aldersgrense;
    }

    public void setAldersgrense(Integer aldersgrense) {
        this.aldersgrense = aldersgrense;
    }

    public String getFaktiskHovedlonn() {
        return faktiskHovedlonn;
    }

    public void setFaktiskHovedlonn(String faktiskHovedlonn) {
        this.faktiskHovedlonn = faktiskHovedlonn;
    }

    public String getStillingsuavhengigTilleggslonn() {
        return stillingsuavhengigTilleggslonn;
    }

    public void setStillingsuavhengigTilleggslonn(String stillingsuavhengigTilleggslonn) {
        this.stillingsuavhengigTilleggslonn = stillingsuavhengigTilleggslonn;
    }
}
