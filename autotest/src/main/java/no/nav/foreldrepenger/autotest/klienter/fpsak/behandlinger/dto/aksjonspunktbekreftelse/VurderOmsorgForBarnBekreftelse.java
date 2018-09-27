package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;

@BekreftelseKode(kode="5061")
public class VurderOmsorgForBarnBekreftelse extends AksjonspunktBekreftelse {

    protected Boolean omsorg;
    protected List<Periode> ikkeOmsorgPerioder = new ArrayList<>();
    
    public VurderOmsorgForBarnBekreftelse(Fagsak fagsak, Behandling behandling) {
        super(fagsak, behandling);
    }
    
    
    public void bekreftBrukerHarOmsorg() {
        omsorg = true;
    }
    
    public void bekreftBrukerHarIkkeOmsorg(LocalDate fra, LocalDate til) {
        omsorg = false;
        ikkeOmsorgPerioder.add(new Periode(fra, til));
    }
    
    public static class Periode {
        LocalDate periodeFom;
        LocalDate periodeTom;
        
        public Periode(LocalDate periodeFom, LocalDate periodeTom) {
            this.periodeFom = periodeFom;
            this.periodeTom = periodeTom;
        }
    }
}
