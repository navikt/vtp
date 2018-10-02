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
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.BehandlingNy;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.BehandlingPaVent;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.BehandlingResourceRequest;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.AksjonspunktBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.BekreftedeAksjonspunkter;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Aksjonspunkt;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.FagsakKlient;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;
import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.KodeverkKlient;
import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kodeverk;
import no.nav.foreldrepenger.autotest.util.konfigurasjon.MiljoKonfigurasjon;
import no.nav.foreldrepenger.autotest.util.vent.Vent;

public class Saksbehandler extends Aktoer{
	
    
    public List<Fagsak> fagsaker;
    public Fagsak valgtFagsak;
    
    public List<Behandling> behandlinger;
    public Behandling valgtBehandling;
    
    private FagsakKlient fagsakKlient;
    private BehandlingerKlient behandlingerKlient;
    private KodeverkKlient kodeverkKlient;
    
    public Kodeverk kodeverk;
    public boolean ikkeVentPåStatus = false; //TODO hack for økonomioppdrag
    
    
    
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
    public void hentFagsak(String saksnummer) throws Exception {
        velgFagsak(fagsakKlient.getFagsak(saksnummer));
    }
	
    /*
     * Hent enkel fagsak
     */
    public void hentFagsak(long saksnummer) throws Exception {
        hentFagsak("" + saksnummer);
    }

    /*
     * Søker etter fagsaker
     */
    public void søkEtterFagsak(String søk) throws Exception {
        fagsaker = fagsakKlient.søk(søk);
        if(fagsaker.size() == 1) {
            velgFagsak(fagsaker.get(0));
        }
    }
    
    /*
     * Velger fagsak
     */
    public void velgFagsak(Fagsak fagsak) throws Exception {
        if(fagsak == null) {
            throw new RuntimeException("Kan ikke velge fagsak. fagsak er null");
        }
        valgtFagsak = fagsak;
        
        behandlinger = behandlingerKlient.alle(fagsak.saksnummer);
        valgtBehandling = null;
        
        if(behandlinger.size() > 0) {
            velgBehandling(behandlinger.get(0));
        }
    }
    
    /*
     * velger behandling som valgt behandling
     */
    public void velgBehandling(Behandling behandling) throws Exception {
        Vent.til(() -> {
            return behandlingerKlient.erStatusOk(behandling.id, null) || ikkeVentPåStatus;
        }, 60, "Behandling status var ikke klar");
        valgtBehandling = behandlingerKlient.getBehandling(behandling.id);
        valgtBehandling.aksjonspunkter = behandlingerKlient.getBehandlingAksjonspunkt(behandling.id);
        valgtBehandling.personopplysning = behandlingerKlient.behandlingPersonopplysninger(new BehandlingResourceRequest(valgtBehandling.id, valgtFagsak.saksnummer));
        valgtBehandling.verge = behandlingerKlient.behandlingVerge(new BehandlingResourceRequest(valgtBehandling.id, valgtFagsak.saksnummer));
        //valgtBehandling.behandlingsresultat = behandlingerKlient.behandl
        valgtBehandling.beregningsgrunnlag = behandlingerKlient.behandlingBeregningsgrunnlag(new BehandlingResourceRequest(valgtBehandling.id, valgtFagsak.saksnummer));
        valgtBehandling.soknad = behandlingerKlient.behandlingSøknad(new BehandlingResourceRequest(valgtBehandling.id, valgtFagsak.saksnummer));
        valgtBehandling.familiehendelse = behandlingerKlient.behandlingFamiliehendelse(new BehandlingResourceRequest(valgtBehandling.id, valgtFagsak.saksnummer));
        valgtBehandling.opptjening = behandlingerKlient.behandlingOpptjening(new BehandlingResourceRequest(valgtBehandling.id, valgtFagsak.saksnummer));
        valgtBehandling.inntektArbeidYtelse = behandlingerKlient.behandlingInntektArbeidYtelse(new BehandlingResourceRequest(valgtBehandling.id, valgtFagsak.saksnummer));
        valgtBehandling.kontrollerFaktaData = behandlingerKlient.behandlingKontrollerFaktaPerioder(new BehandlingResourceRequest(valgtBehandling.id, valgtFagsak.saksnummer));
        valgtBehandling.medlem = behandlingerKlient.behandlingMedlemskap(new BehandlingResourceRequest(valgtBehandling.id, valgtFagsak.saksnummer));

        
        for (Aksjonspunkt aksjonspunkt : valgtBehandling.aksjonspunkter) {
            aksjonspunkt.setBekreftelse(AksjonspunktBekreftelse.fromAksjonspunkt(valgtFagsak, valgtBehandling, aksjonspunkt));
        }
    }

    /*
     * Henting av kodeverk
     */
    public void hentKodeverk() throws IOException {
        kodeverk = kodeverkKlient.getKodeverk();
    }
    
    public void hentSelftest() throws IOException {
        HttpResponse response = session.get(MiljoKonfigurasjon.getRouteMetrics());
        if(200 != response.getStatusLine().getStatusCode()) {
            throw new RuntimeException("Kunne ikke hente selftest. fikk httpstatus: " + response.getStatusLine().getStatusCode());
        }
    }
    
    /*
     * Setter behandling på vent
     */
    public void settBehandlingPåVent(LocalDate frist, String årsak) throws Exception {
        behandlingerKlient.settPaVent(new BehandlingPaVent(valgtBehandling, frist, kodeverk.hentKode(årsak, kodeverk.Venteårsak)));
        velgBehandling(valgtBehandling);
    }
    
    public void gjenopptaBehandling() throws Exception {
        behandlingerKlient.gjenoppta(new BehandlingIdPost(valgtBehandling));
        velgBehandling(valgtBehandling);
    }
    
    public <T extends AksjonspunktBekreftelse> T aksjonspunktBekreftelse(Class<T> type) {
        return hentAksjonspunktbekreftelse(type);
    }
    
    /*
     * Henter aksjonspunkt bekreftelse av gitt klasse
     */
    @SuppressWarnings("unchecked")
    public <T extends AksjonspunktBekreftelse> T hentAksjonspunktbekreftelse(Class<T> type) {
        for (Aksjonspunkt aksjonspunkt : valgtBehandling.aksjonspunkter) {
            if(type.isInstance(aksjonspunkt.getBekreftelse())) {
                return (T) aksjonspunkt.getBekreftelse();
            }
        }
        throw new RuntimeException("Valgt behandling har ikke aksjonspunktbekreftelse: " + type.getName());
    }
    
    /*
     * Henter aksjonspunkt av gitt kode
     */
    public Aksjonspunkt hentAksjonspunkt(String kode) {
        for (Aksjonspunkt aksjonspunkt : valgtBehandling.aksjonspunkter) {
            if(aksjonspunkt.getDefinisjon().kode.equals(kode)) {
                return aksjonspunkt;
            }
        }
        throw new RuntimeException("Fant ikke aksonspunkt ");
    }
    
    /*
     * bekrefter aksjonspunkt
     */
    public void bekreftAksjonspunkt(Aksjonspunkt aksjonspunkt) throws Exception {
        bekreftAksjonspunktBekreftelse(aksjonspunkt.getBekreftelse());
    }
    
    /*
     * Bekrefte aksjonspunkt bekreftelse
     */
    public <T extends AksjonspunktBekreftelse> void bekreftAksjonspunktBekreftelse(Class<T> type) throws Exception {
        bekreftAksjonspunktBekreftelse(hentAksjonspunktbekreftelse(type));
    }
    
    public void bekreftAksjonspunktBekreftelse(AksjonspunktBekreftelse bekreftelse) throws Exception {
        List<AksjonspunktBekreftelse> bekreftelser = new ArrayList<>();
        bekreftelser.add(bekreftelse);
        bekreftAksjonspunktbekreftelserer(bekreftelser);
        velgBehandling(valgtBehandling);
    }
    
    public void bekreftAksjonspunktbekreftelserer(List<AksjonspunktBekreftelse> bekreftelser) throws IOException {
        BekreftedeAksjonspunkter aksjonspunkter = new BekreftedeAksjonspunkter(valgtFagsak, valgtBehandling, bekreftelser);
        behandlingerKlient.postBehandlingAksjonspunkt(aksjonspunkter);
    }
    
    /*
     * Opretter behandling på nåværende fagsak
     */
    public void opprettBehandling(Kode behandlingstype) throws Exception {
        opprettBehandling(behandlingstype, valgtFagsak);
    }

    public void opprettBehandlingRevurdering() throws Exception {
        opprettBehandling(kodeverk.hentKode("Revurdering", kodeverk.BehandlingType));
    }
    public void opprettBehandlingKlage() throws Exception {
        opprettBehandling(kodeverk.hentKode("Klage", kodeverk.BehandlingType));
    }

    /*
     * Private
     */
    
    /*
     * Opretter behandling på gitt fagsak
     */
    private void opprettBehandling(Kode behandlingstype, Fagsak fagsak) throws Exception {
        behandlingerKlient.putBehandlinger(new BehandlingNy(fagsak.saksnummer, behandlingstype));
        velgFagsak(valgtFagsak); //Henter fagsaken på ny
    }
}
