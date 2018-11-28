package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;

@BekreftelseKode(kode="5039")
public class VurderVarigEndringEllerNyoppstartetSNBekreftelse extends AksjonspunktBekreftelse {

    protected boolean erVarigEndretNaering;

    public VurderVarigEndringEllerNyoppstartetSNBekreftelse(Fagsak fagsak, Behandling behandling) {
        super(fagsak, behandling);
    }

    public VurderVarigEndringEllerNyoppstartetSNBekreftelse setErVarigEndretNaering(boolean erVarigEndretNaering) {
        this.erVarigEndretNaering = erVarigEndretNaering;
        return this;
    }

    public boolean hentErVarigEndretNaering() {
        return erVarigEndretNaering;
    }

}
