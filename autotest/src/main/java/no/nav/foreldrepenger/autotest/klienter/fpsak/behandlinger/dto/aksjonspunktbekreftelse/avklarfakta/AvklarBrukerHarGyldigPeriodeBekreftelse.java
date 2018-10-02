package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta;

import java.util.ArrayList;
import java.util.List;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.AksjonspunktBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.BekreftelseKode;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.medlem.Medlemskapsperiode;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;
import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;

@BekreftelseKode(kode="5021")
public class AvklarBrukerHarGyldigPeriodeBekreftelse extends AksjonspunktBekreftelse{

    protected Kode manuellVurderingType;
    protected List<Medlemskapsperiode> periods = new ArrayList<>();
    
    public AvklarBrukerHarGyldigPeriodeBekreftelse(Fagsak fagsak, Behandling behandling) {
        super(fagsak, behandling);
        
        for(Medlemskapsperiode periode : behandling.medlem.getMedlemskapPerioder()){
            periods.add(periode);
        }
    }
    
    public void setVurdering(Kode kode) {
        manuellVurderingType = kode;
    }
}
