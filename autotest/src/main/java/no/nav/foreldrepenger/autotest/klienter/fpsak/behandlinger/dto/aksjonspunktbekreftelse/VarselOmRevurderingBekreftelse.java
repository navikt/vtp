package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse;

import java.time.LocalDate;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;
import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;

@BekreftelseKode(kode="5026")
public class VarselOmRevurderingBekreftelse extends AksjonspunktBekreftelse{

    protected String begrunnelseForVarsel;
    protected String fritekst;
    protected String sendVarsel;
    protected LocalDate frist;
    protected String ventearsak;
    
    public VarselOmRevurderingBekreftelse(Fagsak fagsak, Behandling behandling) {
        super(fagsak, behandling);
    }
    
    public void setFrist(LocalDate frist) {
        this.frist = frist;
    }
    
    public void bekreftSendVarsel(Kode årsak, String fritekst) {
        sendVarsel = "" + true;
        this.fritekst = fritekst;
        ventearsak = årsak.kode;
    }
    
    public void bekreftIkkeSendVarsel() {
        sendVarsel = "" + false;
    }

}
