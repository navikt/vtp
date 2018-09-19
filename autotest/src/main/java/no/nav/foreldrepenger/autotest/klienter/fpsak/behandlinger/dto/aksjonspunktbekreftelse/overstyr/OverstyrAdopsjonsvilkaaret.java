package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.overstyr;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.BekreftelseKode;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;

@BekreftelseKode(kode="6004")
public class OverstyrAdopsjonsvilkaaret extends OverstyringsBekreftelse {

    public OverstyrAdopsjonsvilkaaret(Fagsak fagsak, Behandling behandling) {
        super(fagsak, behandling);
    }

}
