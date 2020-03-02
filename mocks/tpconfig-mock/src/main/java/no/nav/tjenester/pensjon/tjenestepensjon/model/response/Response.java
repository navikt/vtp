package no.nav.tjenester.pensjon.tjenestepensjon.model.response;

import java.util.List;

public class Response {
    private List<Stillingsprosent> StillingsprosentListe;

    public List<Stillingsprosent> getStillingsprosentListe() {
        return StillingsprosentListe;
    }

    public void setStillingsprosentListe(List<Stillingsprosent> stillingsprosentListe) {
        StillingsprosentListe = stillingsprosentListe;
    }
}
