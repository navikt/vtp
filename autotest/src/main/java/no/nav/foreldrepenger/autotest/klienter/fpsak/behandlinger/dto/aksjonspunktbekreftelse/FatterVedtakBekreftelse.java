package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse;

import java.util.ArrayList;
import java.util.List;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Aksjonspunkt;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;
import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;


@BekreftelseKode(kode="5016")
public class FatterVedtakBekreftelse extends AksjonspunktBekreftelse {

    protected List<AksjonspunktGodkjenningDto> aksjonspunktGodkjenningDtos = new ArrayList<>();
    
    public FatterVedtakBekreftelse(Fagsak fagsak, Behandling behandling) {
        super(fagsak, behandling);
        // TODO Auto-generated constructor stub
    }
    
    public void godkjennAksjonspunkter(List<Aksjonspunkt> aksjonspunkter) {
        for (Aksjonspunkt aksjonspunkt : aksjonspunkter) {
            godkjennAksjonspunkt(aksjonspunkt);
        }
    }
    
    public FatterVedtakBekreftelse godkjennAksjonspunkt(Aksjonspunkt aksjonspunkt) {
        if(!aksjonspunkt.skalTilToTrinnsBehandling()) {
            throw new RuntimeException("Godkjenner aksjonspunkt som ikke skal til totrinnskontroll: "+ aksjonspunkt.getDefinisjon().kode);
        }
        
        AksjonspunktGodkjenningDto godkjenning = new AksjonspunktGodkjenningDto(aksjonspunkt);
        godkjenning.godkjent = true;
        aksjonspunktGodkjenningDtos.add(godkjenning);
        return this;
    }
    
    public FatterVedtakBekreftelse avvisAksjonspunkt(Aksjonspunkt aksjonspunkt, Kode kode) {
        List<String> årsaker = new ArrayList<>();
        årsaker.add(kode.kode);
        avvisAksjonspunkt(aksjonspunkt, årsaker);
        return this;
    }
    
    public void avvisAksjonspunkt(Aksjonspunkt aksjonspunkt, List<String> arsaker) {
        if(!aksjonspunkt.skalTilToTrinnsBehandling()) {
            throw new RuntimeException("Avvister aksjonspunkt som ikke skal til totrinnskontroll: "+ aksjonspunkt.getDefinisjon().kode);
        }
        
        AksjonspunktGodkjenningDto godkjenning = new AksjonspunktGodkjenningDto(aksjonspunkt);
        godkjenning.godkjent = false;
        godkjenning.arsaker = arsaker;
        aksjonspunktGodkjenningDtos.add(godkjenning);
    }
    
    public static class AksjonspunktGodkjenningDto{
        protected String aksjonspunktKode;
        protected List<String> arsaker = new ArrayList<>();
        protected String begrunnelse = null;
        protected boolean godkjent = false;
        
        public AksjonspunktGodkjenningDto(Aksjonspunkt aksjonspunkt) {
            aksjonspunktKode = aksjonspunkt.getDefinisjon().kode;
        }
    }

    @Override
    public String toString(){
        return String.format("FatterVedtakBekreftelse: {kode:%s, begrunnelse%s}",kode,begrunnelse);
    }
}
