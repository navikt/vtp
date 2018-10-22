package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;
import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;



@BekreftelseKode(kode="5011")
public class VurderingAvOmsorgsvilkoret extends AksjonspunktBekreftelse {

    protected String avslagskode;
    protected boolean erVilkarOk;
    
    public VurderingAvOmsorgsvilkoret(Fagsak fagsak, Behandling behandling) {
        super(fagsak, behandling);
    }
    
    public void bekreftGodkjent() {
        erVilkarOk = true;
    }
    
    public void bekreftAvvist(Kode kode) {
        erVilkarOk = false;
        avslagskode = kode.kode;
    }

}
