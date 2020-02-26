package no.nav.tjenester.pensjon.tjenestepensjon.model;

import java.util.ArrayList;
import java.util.List;

public class SimulerOffentligTjenestepensjonResponse {
    private List<SimulertPensjon> SimulertpensjonListe = new ArrayList();

    public List<SimulertPensjon> getSimulertpensjonListe() {
        return SimulertpensjonListe;
    }

    public void setSimulertpensjonListe(List<SimulertPensjon> simulertpensjonListe) {
        SimulertpensjonListe = simulertpensjonListe;
    }
}
