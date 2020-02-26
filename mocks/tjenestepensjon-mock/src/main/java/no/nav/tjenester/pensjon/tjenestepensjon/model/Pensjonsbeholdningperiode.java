package no.nav.tjenester.pensjon.tjenestepensjon.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;

import java.time.LocalDate;

@ApiModel()
@JsonIgnoreProperties(ignoreUnknown = true)
public class Pensjonsbeholdningperiode {

    @JsonProperty(required = true)
    private LocalDate datoFom;

    @JsonProperty(required = true)
    private Integer pensjonsbeholdning;

    @JsonProperty(required = true)
    private Integer garantipensjonsbeholdning = 0;

    @JsonProperty(required = true)
    private Integer garantilleggsbeholdning = 0;
}
