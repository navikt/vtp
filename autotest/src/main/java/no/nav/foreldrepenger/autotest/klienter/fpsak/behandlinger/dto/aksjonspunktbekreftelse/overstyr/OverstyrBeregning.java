package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.overstyr;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.BekreftelseKode;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;

@BekreftelseKode(kode="6007")
public class OverstyrBeregning extends OverstyringsBekreftelse {

    protected long beregnetTilkjentYtelse;

    public OverstyrBeregning(Fagsak fagsak, Behandling behandling) {
        super(fagsak, behandling);
    }
    
    public OverstyrBeregning(Fagsak fagsak, Behandling behandling, long beregnetTilkjentYtelse) {
        super(fagsak, behandling);
        this.beregnetTilkjentYtelse = beregnetTilkjentYtelse;
    }
}
