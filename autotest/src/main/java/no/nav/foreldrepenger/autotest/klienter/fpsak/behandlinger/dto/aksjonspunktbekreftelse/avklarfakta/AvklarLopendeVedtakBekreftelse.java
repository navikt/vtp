package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.AksjonspunktBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.BekreftelseKode;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;
import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;

@BekreftelseKode(kode="5031")
public class AvklarLopendeVedtakBekreftelse extends AksjonspunktBekreftelse {

    protected boolean erVilkarOk;
    protected String avslagskode;
    
    public AvklarLopendeVedtakBekreftelse(Fagsak fagsak, Behandling behandling) {
        super(fagsak, behandling);
        // TODO Auto-generated constructor stub
    }
    
    public void bekreftGodkjent() {
        erVilkarOk = true;
    }
    
    public void bekreftAvvist(Kode avslagskode) {
        erVilkarOk = false;
        this.avslagskode = avslagskode.kode;
    }
    
    

}
