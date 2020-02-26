package no.nav.tjenester.pensjon.tjenestepensjon.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;

import java.time.LocalDate;

@ApiModel()
@JsonIgnoreProperties(ignoreUnknown = true)
public class Pensjonsbeholdningperiode {

    @JsonCreator
    public Pensjonsbeholdningperiode(
            @JsonProperty(value = "datoFom", required = true) LocalDate datoFom,
            @JsonProperty(value = "pensjonsbeholdning", required = true) Integer pensjonsbeholdning,
            @JsonProperty(value = "garantipensjonsbeholdning", required = true) Integer garantipensjonsbeholdning,
            @JsonProperty(value = "garantilleggsbeholdning", required = true) Integer garantilleggsbeholdning
    ) {
        this.datoFom = datoFom;
        this.pensjonsbeholdning = pensjonsbeholdning;
        this.garantipensjonsbeholdning = garantipensjonsbeholdning;
        this.garantilleggsbeholdning = garantilleggsbeholdning;
    }

    private LocalDate datoFom;
    private Integer pensjonsbeholdning;
    private Integer garantipensjonsbeholdning = 0;
    private Integer garantilleggsbeholdning = 0;
}
