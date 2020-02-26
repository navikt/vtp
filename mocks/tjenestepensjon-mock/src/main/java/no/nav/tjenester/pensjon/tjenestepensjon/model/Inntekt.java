package no.nav.tjenester.pensjon.tjenestepensjon.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;

import java.time.LocalDate;

@ApiModel()
@JsonIgnoreProperties(ignoreUnknown = true)
public class Inntekt {

    @JsonProperty(required = true)
    private LocalDate datoFom;

    @JsonProperty(required = true)
    private Double inntekt;


    public LocalDate getDatoFom() {
        return datoFom;
    }

    public void setDatoFom(LocalDate datoFom) {
        this.datoFom = datoFom;
    }

    public Double getInntekt() {
        return inntekt;
    }

    public void setInntekt(Double inntekt) {
        this.inntekt = inntekt;
    }
}
