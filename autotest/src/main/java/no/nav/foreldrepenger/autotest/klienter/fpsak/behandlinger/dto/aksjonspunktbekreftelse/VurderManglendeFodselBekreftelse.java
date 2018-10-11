package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse;

import java.time.LocalDate;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;

@BekreftelseKode(kode="5027")
public class VurderManglendeFodselBekreftelse extends AksjonspunktBekreftelse {

    protected Integer antallBarnFodt;
    protected LocalDate fodselsdato;
    protected boolean dokumentasjonForeligger;
    protected boolean brukAntallBarnITps;
    
    public VurderManglendeFodselBekreftelse(Fagsak fagsak, Behandling behandling) {
        super(fagsak, behandling);
    }
    
    public void bekreftDokumentasjonForeligger(int antallBarn, LocalDate dato) {
        dokumentasjonForeligger = true;
        antallBarnFodt = antallBarn;
        fodselsdato = dato;
    }
    
    public void bekreftDokumentasjonIkkeForeligger() {
        dokumentasjonForeligger = false;
        antallBarnFodt = null;
    }
    
    public void bekreftBrukAntallBarnITps() {
        brukAntallBarnITps = true;
    }

}
