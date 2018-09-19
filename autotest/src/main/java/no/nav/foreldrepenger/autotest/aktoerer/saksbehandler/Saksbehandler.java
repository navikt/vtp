package no.nav.foreldrepenger.autotest.aktoerer.saksbehandler;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import no.nav.foreldrepenger.autotest.aktoerer.Aktoer;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.BehandlingerKlient;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.BehandlingPaVent;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.FagsakKlient;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;
import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.KodeverkKlient;
import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;
import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kodeverk;

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
    
    /*
     * Setter behandling på vent
     */
    public void settBehandlingPåVent(LocalDate frist, String årsak) throws IOException {
        behandlingerKlient.settPaVent(new BehandlingPaVent(valgtBehandling, frist, kodeverk.hentKode(årsak, kodeverk.Venteårsak)));
    }
    
    public void gjenopptaBehandling() {
        throw new RuntimeException("Not implemented: gjenopptaBehandling");
    }
    
    /*
     * Henter aksjonspunkt bekreftelse av gitt klasse
     */
    public <T> T hentAksjonspunktbekreftelse(Class<T> type) {
        return null;
    }
    
    /*
     * bekrefter aksjonspunkt
     */
    public void bekreftAksjonspunkt(Object object) {
        throw new RuntimeException("Not implemented: bekreftAksjonspunkt");
    }
    
    /*
     * Bekrefte aksjonspunkt bekreftelse
     */
    public void bekreftAksjonspunktBekreftelse(Object object) {
        throw new RuntimeException("Ikke implementert bekreftAksjonspunkt");
    }
    
    public void bekreftAksjonspunktbekreftelserer(List<Object> aksjonspunkter) {
        throw new RuntimeException("Not implemented: bekreftAksjonspunktbekreftelserer");
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
