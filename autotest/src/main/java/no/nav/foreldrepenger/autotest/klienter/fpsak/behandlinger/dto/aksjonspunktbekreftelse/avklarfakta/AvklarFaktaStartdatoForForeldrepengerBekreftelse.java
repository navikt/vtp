package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.AksjonspunktBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.BekreftelseKode;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;

import java.time.LocalDate;

@BekreftelseKode(kode="5045")
public class AvklarFaktaStartdatoForForeldrepengerBekreftelse extends AksjonspunktBekreftelse {

    protected LocalDate startdatoFraSoknad;
    
    public AvklarFaktaStartdatoForForeldrepengerBekreftelse(Fagsak fagsak, Behandling behandling) {
        super(fagsak, behandling);
    }
    
    public AvklarFaktaStartdatoForForeldrepengerBekreftelse setStartdatoFraSoknad(LocalDate dato) {
        startdatoFraSoknad = dato;
        return this;
    }

}
