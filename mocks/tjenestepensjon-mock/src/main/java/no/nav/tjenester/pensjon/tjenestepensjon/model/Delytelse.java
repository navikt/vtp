package no.nav.tjenester.pensjon.tjenestepensjon.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;

@ApiModel()
@JsonIgnoreProperties(ignoreUnknown = true)
public class Delytelse {

    @JsonProperty(required = true)
    private DelytelseType pensjonstype;

    @JsonProperty(required = true)
    private Double belop;

    public DelytelseType getPensjonstype() {
        return pensjonstype;
    }

    public void setPensjonstype(DelytelseType pensjonstype) {
        this.pensjonstype = pensjonstype;
    }

    public Double getBelop() {
        return belop;
    }

    public void setBelop(Double belop) {
        this.belop = belop;
    }
}
