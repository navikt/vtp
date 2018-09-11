package no.nav.foreldrepenger.autotest.aktoerer.saksbehandler;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import no.nav.foreldrepenger.autotest.aktoerer.Aktoer;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.BehandlingerKlient;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.BehandlingPaVent;
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

	public void hentFagsak(String saksnummer) throws IOException {
	    velgFagsak(fagsakKlient.getFagsak(saksnummer));
	}

	public void søkEtterFagsak(String søk) throws IOException {
		fagsaker = fagsakKlient.søk(søk);
	}

    public void settBehandlingPåVent(LocalDate frist, String årsak) {
        behandlingerKlient.settPaVent(new BehandlingPaVent(valgtBehandling, frist, kodeverk.hentKode(årsak, kodeverk.Venteårsak)));
    }
    
    public void velgFagsak(Fagsak fagsak) throws IOException {
        valgtFagsak = fagsak;
        
        behandlinger = behandlingerKlient.alle(fagsak.saksnummer);
        valgtBehandling = null;
        
        if(behandlinger.size() == 1) {
            velgBehandling(behandlinger.get(0));
        }
    }
    
    
    public void velgBehandling(Behandling behandling) {
        valgtBehandling = behandling;
    }

    public void hentKodeverk() throws IOException {
        kodeverk = kodeverkKlient.getKodeverk();
    }
    
    /*
     * Private
     */

}
