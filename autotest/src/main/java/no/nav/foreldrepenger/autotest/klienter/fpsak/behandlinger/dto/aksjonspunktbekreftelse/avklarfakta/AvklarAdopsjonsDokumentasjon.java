package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta;

import java.time.LocalDate;
import java.util.Map;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.AksjonspunktBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.BekreftelseKode;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;

@BekreftelseKode(kode="5004")
public class AvklarAdopsjonsDokumentasjon extends AksjonspunktBekreftelse{

    protected LocalDate omsorgsovertakelseDato;
    protected Map<Integer, LocalDate> fodselsdatoer;
    
    public AvklarAdopsjonsDokumentasjon(Fagsak fagsak, Behandling behandling) {
        super(fagsak, behandling);
        
        if(behandling.soknad.omsorgsovertakelseDato != null){
            omsorgsovertakelseDato = behandling.soknad.omsorgsovertakelseDato;
        }
        
        if(behandling.soknad.adopsjonFodelsedatoer != null){
            fodselsdatoer = behandling.soknad.adopsjonFodelsedatoer;
        }
    }
    
    public void setOmsorgsovertakelseDato(LocalDate omsorgsovertakelseDato) {
        this.omsorgsovertakelseDato = omsorgsovertakelseDato;
    }
    
    public void leggTilFødselsdato(LocalDate fødselsdato) {
        fodselsdatoer.put(fodselsdatoer.size(), fødselsdato);
    }
    
    public void endreFødselsdato(Integer index, LocalDate fødselsdato) {
        fodselsdatoer.put(index, fødselsdato);
    }
}
