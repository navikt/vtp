package no.nav.tjenester.pensjon.tjenestepensjon.model.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;

@ApiModel()
@JsonIgnoreProperties(ignoreUnknown = true)
public class Delytelse {

    private DelytelseType pensjonstype;
    private Double belop;

    @JsonCreator
    public Delytelse(
            @JsonProperty(value = "pensjonstype", required = true) DelytelseType pensjonstype,
            @JsonProperty(value = "belop", required = true) Double belop
    ) {
        this.pensjonstype = pensjonstype;
        this.belop = belop;
    }

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
