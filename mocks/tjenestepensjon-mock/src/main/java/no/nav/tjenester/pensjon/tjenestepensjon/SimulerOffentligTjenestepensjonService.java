package no.nav.tjenester.pensjon.tjenestepensjon;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.tjenester.pensjon.tjenestepensjon.model.SimulerOffentligTjenestepensjonResponse;
import no.nav.tjenester.pensjon.tjenestepensjon.model.SimulerPensjonRequest;
import no.nav.tjenester.pensjon.tjenestepensjon.model.SimulertPensjon;

import java.util.Arrays;
import java.util.List;

public class SimulerOffentligTjenestepensjonService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public SimulerOffentligTjenestepensjonResponse start(SimulerPensjonRequest request) {
        SimulerOffentligTjenestepensjonResponse response = new SimulerOffentligTjenestepensjonResponse();
        response.setSimulertpensjonListe(buildResponseSingletonList());

        try {
            objectMapper.readValue(
                    objectMapper.writeValueAsString(request),
                    SimulerPensjonRequest.class
            );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return response;
    }



    private List<SimulertPensjon> buildResponseSingletonList() {
        return Arrays.asList(new SimulertPensjon());
    }

}
