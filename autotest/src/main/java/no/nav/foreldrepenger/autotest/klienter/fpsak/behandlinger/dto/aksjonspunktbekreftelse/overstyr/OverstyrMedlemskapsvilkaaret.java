package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.overstyr;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.BekreftelseKode;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;

@BekreftelseKode(kode="6005")
public class OverstyrMedlemskapsvilkaaret extends OverstyringsBekreftelse {

    public OverstyrMedlemskapsvilkaaret(Fagsak fagsak, Behandling behandling) {
        super(fagsak, behandling);
    }
    
}
