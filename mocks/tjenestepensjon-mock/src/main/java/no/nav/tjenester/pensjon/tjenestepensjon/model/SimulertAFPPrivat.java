package no.nav.tjenester.pensjon.tjenestepensjon.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;

@ApiModel()
@JsonIgnoreProperties(ignoreUnknown = true)
public class SimulertAFPPrivat {

    @JsonProperty(required = true)
    private Integer afpOpptjeningTotalbelop;
    private Double kompensasjonstillegg;
}
