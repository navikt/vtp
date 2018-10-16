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
import no.nav.foreldrepenger.autotest.klienter.fpsak.dokument.DokumentKlient;
import no.nav.foreldrepenger.autotest.klienter.fpsak.dokument.dto.DokumentListeEnhet;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.FagsakKlient;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;
import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.KodeverkKlient;
import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kodeverk;
import no.nav.foreldrepenger.autotest.util.konfigurasjon.MiljoKonfigurasjon;
import no.nav.foreldrepenger.autotest.util.vent.Vent;

public class Saksbehandler extends Aktoer{
	
    
    public List<Fagsak> fagsaker;
    public Fagsak valgtFagsak;
    
    public List<DokumentListeEnhet> dokumenter;
    
    public List<Behandling> behandlinger;
    public Behandling valgtBehandling;
    
    private FagsakKlient fagsakKlient;
    private BehandlingerKlient behandlingerKlient;
    private KodeverkKlient kodeverkKlient;
    private DokumentKlient dokumentKlient;
    
    public Kodeverk kodeverk;
    public boolean ikkeVentPåStatus = false; //TODO hack for økonomioppdrag
    
    
    
    
    public Saksbehandler() {
        super();
        fagsakKlient = new FagsakKlient(session);
        behandlingerKlient = new BehandlingerKlient(session);
        kodeverkKlient = new KodeverkKlient(session);
        dokumentKlient = new DokumentKlient(session);
    }

    public Saksbehandler(String rolle) throws IOException {
        this();
	erLoggetInnMedRolle(rolle);
    }
    
    @Override
    public void erLoggetInnMedRolle(String rolle) throws IOException {
        super.erLoggetInnMedRolle(rolle);
        hentKodeverk();
    }
    
    @Override
    public void erLoggetInnUtenRolle() throws IOException {
        super.erLoggetInnUtenRolle();
        hentKodeverk();
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
        
        dokumenter = dokumentKlient.hentDokumentliste(valgtFagsak.saksnummer);
        
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
        
        BehandlingResourceRequest request = new BehandlingResourceRequest(valgtBehandling.id, valgtFagsak.saksnummer);
        valgtBehandling.personopplysning = behandlingerKlient.behandlingPersonopplysninger(request);
        valgtBehandling.verge = behandlingerKlient.behandlingVerge(request);
        valgtBehandling.beregningsgrunnlag = behandlingerKlient.behandlingBeregningsgrunnlag(request);
        valgtBehandling.beregningResultatEngangsstonad = behandlingerKlient.behandlingBeregningsresultatEngangsstønad(request);
        valgtBehandling.beregningResultatForeldrepenger = behandlingerKlient.behandlingBeregningsresultatForeldrepenger(request);
        valgtBehandling.soknad = behandlingerKlient.behandlingSøknad(request);
        valgtBehandling.familiehendelse = behandlingerKlient.behandlingFamiliehendelse(request);
        valgtBehandling.opptjening = behandlingerKlient.behandlingOpptjening(request);
        valgtBehandling.inntektArbeidYtelse = behandlingerKlient.behandlingInntektArbeidYtelse(request);
        valgtBehandling.kontrollerFaktaData = behandlingerKlient.behandlingKontrollerFaktaPerioder(request);
        valgtBehandling.medlem = behandlingerKlient.behandlingMedlemskap(request);

        
        for (Aksjonspunkt aksjonspunkt : valgtBehandling.aksjonspunkter) {
            aksjonspunkt.setBekreftelse(AksjonspunktBekreftelse.fromAksjonspunkt(valgtFagsak, valgtBehandling, aksjonspunkt));
        }
    }

    /*
     * Henting av kodeverk
     */
    public void hentKodeverk() {
        try {
            kodeverk = kodeverkKlient.getKodeverk();
        } catch (Exception e) {
            throw new RuntimeException("Kunne ikke hente kodeverk: " + e.getMessage());
        }
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
    public void settBehandlingPåVent(LocalDate frist, Kode årsak) throws Exception {
        behandlingerKlient.settPaVent(new BehandlingPaVent(valgtBehandling, frist, årsak));
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
    }
    
    public void bekreftAksjonspunktbekreftelserer(List<AksjonspunktBekreftelse> bekreftelser) throws Exception {
        BekreftedeAksjonspunkter aksjonspunkter = new BekreftedeAksjonspunkter(valgtFagsak, valgtBehandling, bekreftelser);
        behandlingerKlient.postBehandlingAksjonspunkt(aksjonspunkter);
        velgBehandling(valgtBehandling);
    }
    
    /*
     * Oversyring
     */
    public <T extends AksjonspunktBekreftelse> void overstyr(Class<T> type) throws Exception {
        overstyr(hentAksjonspunktbekreftelse(type));
    }
    
    public void overstyr(AksjonspunktBekreftelse bekreftelse) throws Exception {
        List<AksjonspunktBekreftelse> bekreftelser = new ArrayList<>();
        bekreftelser.add(bekreftelse);
        overstyr(bekreftelser);
    }
    
    public void overstyr(List<AksjonspunktBekreftelse> bekreftelser) throws Exception {
        BekreftedeAksjonspunkter aksjonspunkter = new BekreftedeAksjonspunkter(valgtFagsak, valgtBehandling, bekreftelser);
        behandlingerKlient.overstyr(aksjonspunkter);
        velgBehandling(valgtBehandling);
    }
    
    /*
     * Opretter behandling på nåværende fagsak
     */
    public void opprettBehandling(Kode behandlingstype) throws Exception {
        opprettBehandling(behandlingstype, valgtFagsak);
    }

    public void opprettBehandlingRevurdering() throws Exception {
        opprettBehandling(kodeverk.BehandlingType.getKode("Revurdering"));
    }
    public void opprettBehandlingKlage() throws Exception {
        opprettBehandling(kodeverk.BehandlingType.getKode("Klage"));
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
