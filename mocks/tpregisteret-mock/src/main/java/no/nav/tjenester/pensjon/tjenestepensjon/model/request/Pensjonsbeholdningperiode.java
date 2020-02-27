package no.nav.tjenester.pensjon.tjenestepensjon.model.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;

import java.time.LocalDate;

@ApiModel()
@JsonIgnoreProperties(ignoreUnknown = true)
public class Pensjonsbeholdningperiode {

    private LocalDate datoFom;
    private Integer pensjonsbeholdning;
    private Integer garantipensjonsbeholdning;
    private Integer garantilleggsbeholdning;

    @JsonCreator
    public Pensjonsbeholdningperiode(
            @JsonProperty(value = "datoFom", required = true) LocalDate datoFom,
            @JsonProperty(value = "pensjonsbeholdning", required = true) Integer pensjonsbeholdning,
            @JsonProperty(value = "garantipensjonsbeholdning", required = true, defaultValue = "0") Integer garantipensjonsbeholdning,
            @JsonProperty(value = "garantilleggsbeholdning", required = true, defaultValue = "0") Integer garantilleggsbeholdning
    ) {
        this.datoFom = datoFom;
        this.pensjonsbeholdning = pensjonsbeholdning;
        this.garantipensjonsbeholdning = garantipensjonsbeholdning;
        this.garantilleggsbeholdning = garantilleggsbeholdning;
    }

    public LocalDate getDatoFom() {
        return datoFom;
    }

    public void setDatoFom(LocalDate datoFom) {
        this.datoFom = datoFom;
    }

    public Integer getPensjonsbeholdning() {
        return pensjonsbeholdning;
    }

    public void setPensjonsbeholdning(Integer pensjonsbeholdning) {
        this.pensjonsbeholdning = pensjonsbeholdning;
    }

    public Integer getGarantipensjonsbeholdning() {
        return garantipensjonsbeholdning;
    }

    public void setGarantipensjonsbeholdning(Integer garantipensjonsbeholdning) {
        this.garantipensjonsbeholdning = garantipensjonsbeholdning;
    }

    public Integer getGarantilleggsbeholdning() {
        return garantilleggsbeholdning;
    }

    public void setGarantilleggsbeholdning(Integer garantilleggsbeholdning) {
        this.garantilleggsbeholdning = garantilleggsbeholdning;
    }
}
