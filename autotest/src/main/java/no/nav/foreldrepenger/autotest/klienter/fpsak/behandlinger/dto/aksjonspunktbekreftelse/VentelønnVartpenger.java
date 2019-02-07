package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse;

class VentelønnVartpenger {



    protected boolean inkludert;

    public VentelønnVartpenger(boolean inkludert) {
        this.inkludert = inkludert;
    }

    public boolean isInkludert() {
        return inkludert;
    }

    public void setInkludert(boolean inkludert) {
        this.inkludert = inkludert;
    }
}
