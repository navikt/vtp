package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.AksjonspunktBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.BekreftelseKode;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;

@BekreftelseKode(kode="5022")
public class AvklarFaktaPersonstatusBekreftelse extends AksjonspunktBekreftelse {

    protected String erEosBorger;
    protected String oppholdsrettVurdering;
    
    public AvklarFaktaPersonstatusBekreftelse(Fagsak fagsak, Behandling behandling) {
        super(fagsak, behandling);
    }
    
    public void bekreftErEøsBorger() {
        erEosBorger = "" + true;
    }
    
    public void bekreftErIkkeEøsBorger() {
        erEosBorger = "" + false;
    }
    
    public void bekreftHarOppholdsrett() {
        erEosBorger = "" + true;
    }
    
    public void bekreftHarIkkeOppholdsrett() {
        erEosBorger = "" + false;
    }
}
