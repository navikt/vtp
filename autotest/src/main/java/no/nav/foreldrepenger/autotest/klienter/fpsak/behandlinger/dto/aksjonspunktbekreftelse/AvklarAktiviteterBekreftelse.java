package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;

@BekreftelseKode(kode="5052")
public class AvklarAktiviteterBekreftelse extends AksjonspunktBekreftelse {

    protected VentelønnVartpenger ventelønnVartpenger;

    public AvklarAktiviteterBekreftelse(Fagsak fagsak, Behandling behandling) {
        super(fagsak, behandling);
    }

    public AvklarAktiviteterBekreftelse leggTilVentelønnVartpenger(boolean inkludert){
        this.ventelønnVartpenger = new VentelønnVartpenger(inkludert);
        return this;
    }

}
