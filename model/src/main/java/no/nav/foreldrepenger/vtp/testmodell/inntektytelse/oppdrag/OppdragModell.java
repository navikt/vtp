package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.oppdrag;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OppdragModell {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("negativSimulering")
    Boolean negativSimulering;

    public Boolean getNegativSimulering() {
        return negativSimulering;
    }

}
