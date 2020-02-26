package no.nav.tjenester.pensjon.tjenestepensjon.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;

@ApiModel()
@JsonIgnoreProperties(ignoreUnknown = true)
public class SimulertAFPPrivat {

    @JsonCreator
    public SimulertAFPPrivat(
            @JsonProperty(value = "afpOpptjeningTotalbelop", required = true) Integer afpOpptjeningTotalbelop,
            @JsonProperty(value = "kompensasjonstillegg") Double kompensasjonstillegg
    ) {
        this.afpOpptjeningTotalbelop = afpOpptjeningTotalbelop;
        this.kompensasjonstillegg = kompensasjonstillegg;
    }

    private Integer afpOpptjeningTotalbelop;
    private Double kompensasjonstillegg;
}
