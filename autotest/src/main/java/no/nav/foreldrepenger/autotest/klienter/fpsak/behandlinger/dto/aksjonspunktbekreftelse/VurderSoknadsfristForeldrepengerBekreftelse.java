package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse;

import java.time.LocalDate;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;

@BekreftelseKode(kode="5043")
public class VurderSoknadsfristForeldrepengerBekreftelse extends AksjonspunktBekreftelse {

    protected Boolean harGyldigGrunn;
    protected LocalDate ansesMottattDato;
    
    public VurderSoknadsfristForeldrepengerBekreftelse(Fagsak fagsak, Behandling behandling) {
        super(fagsak, behandling);
    }
    
    public void bekreftHarGyldigGrunn(LocalDate ansesMottattDato) {
        this.harGyldigGrunn = true;
        this.ansesMottattDato = ansesMottattDato;
        this.begrunnelse = "Test";
    }
    
    public void bekreftHarIkkeGyldigGrunn() {
        harGyldigGrunn = false;
    }
    
    public void setAnsesMottattDato(LocalDate dato) {
        ansesMottattDato = dato;
    }

    
    
    

    
}
