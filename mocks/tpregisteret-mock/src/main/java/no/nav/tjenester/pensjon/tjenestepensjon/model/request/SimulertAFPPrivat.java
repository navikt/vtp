package no.nav.tjenester.pensjon.tjenestepensjon.model.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;

@ApiModel()
@JsonIgnoreProperties(ignoreUnknown = true)
public class SimulertAFPPrivat {

    private Integer afpOpptjeningTotalbelop;
    private Double kompensasjonstillegg;

    @JsonCreator
    public SimulertAFPPrivat(
            @JsonProperty(value = "afpOpptjeningTotalbelop", required = true) Integer afpOpptjeningTotalbelop,
            @JsonProperty(value = "kompensasjonstillegg") Double kompensasjonstillegg
    ) {
        this.afpOpptjeningTotalbelop = afpOpptjeningTotalbelop;
        this.kompensasjonstillegg = kompensasjonstillegg;
    }

    public Integer getAfpOpptjeningTotalbelop() {
        return afpOpptjeningTotalbelop;
    }

    public void setAfpOpptjeningTotalbelop(Integer afpOpptjeningTotalbelop) {
        this.afpOpptjeningTotalbelop = afpOpptjeningTotalbelop;
    }

    public Double getKompensasjonstillegg() {
        return kompensasjonstillegg;
    }

    public void setKompensasjonstillegg(Double kompensasjonstillegg) {
        this.kompensasjonstillegg = kompensasjonstillegg;
    }
}
