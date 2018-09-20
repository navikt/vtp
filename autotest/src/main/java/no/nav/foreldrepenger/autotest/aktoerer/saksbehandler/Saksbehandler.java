package no.nav.foreldrepenger.autotest.aktoerer.saksbehandler;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;
import org.apache.http.HttpResponse;

import no.nav.foreldrepenger.autotest.aktoerer.Aktoer;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.BehandlingerKlient;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.BehandlingIdPost;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.BehandlingPaVent;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.AksjonspunktBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.BekreftedeAksjonspunkter;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Aksjonspunkt;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.FagsakKlient;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;
import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.KodeverkKlient;
import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kodeverk;
import no.nav.foreldrepenger.autotest.util.konfigurasjon.MiljoKonfigurasjon;

public class Saksbehandler extends Aktoer{
	
    
    public List<Fagsak> fagsaker;
    public Fagsak valgtFagsak;
    
    public List<Behandling> behandlinger;
    public Behandling valgtBehandling;
    
	private FagsakKlient fagsakKlient;
	private BehandlingerKlient behandlingerKlient;
	private KodeverkKlient kodeverkKlient;
	
	private Kodeverk kodeverk;
	
	
	
	public Saksbehandler() {
		super();
		fagsakKlient = new FagsakKlient(session);
		behandlingerKlient = new BehandlingerKlient(session);
		kodeverkKlient = new KodeverkKlient(session);
	}

	public Saksbehandler(String rolle) {
		this();
		erLoggetInnMedRolle(rolle);
	}

	/*
	 * Hent enkel fagsak
	 */
	public void hentFagsak(String saksnummer) throws IOException {
	    velgFagsak(fagsakKlient.getFagsak(saksnummer));
	}

	/*
	 * Søker etter fagsaker
	 */
	public void søkEtterFagsak(String søk) throws IOException {
		fagsaker = fagsakKlient.søk(søk);
		if(fagsaker.size() == 1) {
		    velgFagsak(fagsaker.get(0));
		}
	}
    
    /*
     * Velger fagsak
     */
    public void velgFagsak(Fagsak fagsak) throws IOException {
        if(fagsak == null) {
            throw new RuntimeException("Kan ikke velge fagsak. fagsak er null");
        }
        valgtFagsak = fagsak;
        
        behandlinger = behandlingerKlient.alle(fagsak.saksnummer);
        valgtBehandling = null;
        
        if(behandlinger.size() == 1) {
            velgBehandling(behandlinger.get(0));
        }
    }
    
    /*
     * velger behandling som valgt behandling
     */
    public void velgBehandling(Behandling behandling) {
        valgtBehandling = behandling;
    }

    /*
     * Henting av kodeverk
     */
    public void hentKodeverk() throws IOException {
        kodeverk = kodeverkKlient.getKodeverk();
    }
    
    public void hentSelftest() throws IOException {
        HttpResponse response = session.get(MiljoKonfigurasjon.hentSelftestUrl());
        System.out.println(MiljoKonfigurasjon.hentSelftestUrl());
        System.out.println(response.getStatusLine().getStatusCode());
        if(200 != response.getStatusLine().getStatusCode()) {
            throw new RuntimeException("Kunne ikke hente selftest. fikk httpstatus: " + response.getStatusLine().getStatusCode());
        }
    }
    
    /*
     * Setter behandling på vent
     */
    public void settBehandlingPåVent(LocalDate frist, String årsak) throws IOException {
        behandlingerKlient.settPaVent(new BehandlingPaVent(valgtBehandling, frist, kodeverk.hentKode(årsak, kodeverk.Venteårsak)));
    }
    
    public void gjenopptaBehandling() throws IOException {
        behandlingerKlient.gjenoppta(new BehandlingIdPost(valgtBehandling));
    }
    
    /*
     * Henter aksjonspunkt bekreftelse av gitt klasse
     */
    @SuppressWarnings("unchecked")
    public <T> T hentAksjonspunktbekreftelse(Class<T> type) {
        for (Aksjonspunkt aksjonspunkt : valgtBehandling.aksjonspunkter) {
            if(type.isInstance(aksjonspunkt.bekreftelse)) {
                return (T) aksjonspunkt;
            }
        }
        throw new RuntimeException("Valgt behandling har ikke aksjonspunktbekreftelse: " + type.getName());
    }
    
    /*
     * bekrefter aksjonspunkt
     */
    public void bekreftAksjonspunkt(Aksjonspunkt aksjonspunkt) throws IOException {
        bekreftAksjonspunktBekreftelse(aksjonspunkt.bekreftelse);
    }
    
    /*
     * Bekrefte aksjonspunkt bekreftelse
     */
    public void bekreftAksjonspunktBekreftelse(AksjonspunktBekreftelse bekreftelse) throws IOException {
        List<AksjonspunktBekreftelse> bekreftelser = new ArrayList<>();
        bekreftelser.add(bekreftelse);
        bekreftAksjonspunktbekreftelserer(bekreftelser);
    }
    
    public void bekreftAksjonspunktbekreftelserer(List<AksjonspunktBekreftelse> bekreftelser) throws IOException {
        BekreftedeAksjonspunkter aksjonspunkter = new BekreftedeAksjonspunkter(valgtFagsak, valgtBehandling, bekreftelser);
        behandlingerKlient.postBehandlingAksjonspunkt(aksjonspunkter);
    }
    
    /*
     * Opretter behandling på nåværende fagsak
     */
    public void opprettBehandling(Kode behandlingstype) {
        opprettBehandling(behandlingstype, valgtFagsak);
    }
    
    /*
     * Opretter behandling på gitt fagsak
     */
    public void opprettBehandling(Kode behandlingstype, Fagsak fagsak) {
        throw new RuntimeException("Not implemented: opprettBehandling");
    }
    
    /*
     * Private
     */

}
