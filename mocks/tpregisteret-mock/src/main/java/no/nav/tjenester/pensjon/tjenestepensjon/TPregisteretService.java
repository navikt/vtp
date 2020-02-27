package no.nav.tjenester.pensjon.tjenestepensjon;

import no.nav.tjenester.pensjon.tjenestepensjon.model.request.SimulerPensjonRequest;
import no.nav.tjenester.pensjon.tjenestepensjon.model.response.SimulerOffentligTjenestepensjonResponse;
import no.nav.tjenester.pensjon.tjenestepensjon.model.response.SimulertPensjon;

import java.util.List;

import static java.util.Collections.singletonList;

public class TPregisteretService {

    public SimulerOffentligTjenestepensjonResponse validateRequestAndRespond(SimulerPensjonRequest request) {
        return buildResponse(request);
    }

    private SimulerOffentligTjenestepensjonResponse buildResponse(SimulerPensjonRequest request) {
        SimulerOffentligTjenestepensjonResponse response = new SimulerOffentligTjenestepensjonResponse();
        response.setSimulertpensjonListe(buildResponseSingletonList());

        return response;
    }

    private List<SimulertPensjon> buildResponseSingletonList() {
        SimulertPensjon simulertPensjon = new SimulertPensjon();

        simulertPensjon.setFeilbeskrivelse(null);
        simulertPensjon.setFeilkode(null);
        simulertPensjon.setInkluderteOrdninger(null);
        simulertPensjon.setInkluderteTpnr(null);
        simulertPensjon.setLeverandorUrl(null);
        simulertPensjon.setNavnOrdning(null);
        simulertPensjon.setStatus(null);
        simulertPensjon.setUtbetalingsperioder(null);
        simulertPensjon.setUtbetalingsperioder(null);

        return singletonList(simulertPensjon);
    }
}
