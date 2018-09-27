package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;

@BekreftelseKode(kode="5013")
public class VurderingAvForeldreansvarAndreLedd extends AksjonspunktBekreftelse {

    protected Boolean erVilkarOk;
    protected String avslagskode;
    
    public VurderingAvForeldreansvarAndreLedd(Fagsak fagsak, Behandling behandling) {
        super(fagsak, behandling);
        // TODO Auto-generated constructor stub
    }

    public void bekreftGodkjent() {
        erVilkarOk = true;
    }
    
    public void bekreftAvvist(String kode) {
        erVilkarOk = false;
        avslagskode = kode;
    }
}
