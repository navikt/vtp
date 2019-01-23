package no.nav.foreldrepenger.autotest.aktoerer.foreldrepenger;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.http.HttpResponse;

import io.qameta.allure.Step;
import no.nav.foreldrepenger.autotest.aktoerer.Aktoer;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.BehandlingerKlient;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.AsyncPollingStatus;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.BehandlingHenlegg;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.BehandlingIdPost;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.BehandlingNy;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.BehandlingPaVent;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.BehandlingResourceRequest;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.AksjonspunktBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.BekreftedeAksjonspunkter;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.FatterVedtakBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.OverstyrAksjonspunkter;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Aksjonspunkt;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Familiehendelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.KontrollerFaktaData;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Personopplysning;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Soknad;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Verge;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.arbeid.InntektArbeidYtelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.Beregningsgrunnlag;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.Beregningsresultat;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.BeregningsresultatMedUttaksplan;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.medlem.Medlem;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.opptjening.Opptjening;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.uttak.UttakResultatPerioder;
import no.nav.foreldrepenger.autotest.klienter.fpsak.brev.BrevKlient;
import no.nav.foreldrepenger.autotest.klienter.fpsak.brev.dto.BestillBrev;
import no.nav.foreldrepenger.autotest.klienter.fpsak.dokument.DokumentKlient;
import no.nav.foreldrepenger.autotest.klienter.fpsak.dokument.dto.DokumentListeEnhet;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.FagsakKlient;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;
import no.nav.foreldrepenger.autotest.klienter.fpsak.historikk.HistorikkKlient;
import no.nav.foreldrepenger.autotest.klienter.fpsak.historikk.dto.HistorikkInnslag;
import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.KodeverkKlient;
import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;
import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kodeverk;
import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kodeverk.KodeListe;
import no.nav.foreldrepenger.autotest.klienter.fpsak.prosesstask.ProsesstaskKlient;
import no.nav.foreldrepenger.autotest.klienter.fpsak.prosesstask.dto.ProsessTaskListItemDto;
import no.nav.foreldrepenger.autotest.klienter.fpsak.prosesstask.dto.SokeFilterDto;
import no.nav.foreldrepenger.autotest.util.deferred.Deffered;
import no.nav.foreldrepenger.autotest.util.konfigurasjon.MiljoKonfigurasjon;
import no.nav.foreldrepenger.autotest.util.vent.Vent;

public class Saksbehandler extends Aktoer{

    
    public List<Fagsak> fagsaker;
    public Fagsak valgtFagsak;
    
    public List<DokumentListeEnhet> dokumenter;
    public List<HistorikkInnslag> historikkInnslag;
    
    public List<Behandling> behandlinger;
    public Behandling valgtBehandling;
    
    public Behandling annenPartBehandling;
    
    private FagsakKlient fagsakKlient;
    private BehandlingerKlient behandlingerKlient;
    private KodeverkKlient kodeverkKlient;
    private DokumentKlient dokumentKlient;
    private BrevKlient brevKlient;
    private HistorikkKlient historikkKlient;
    private ProsesstaskKlient prosesstaskKlient;
    
    public Kodeverk kodeverk;
    public KodeListe henleggArsaker;
    public KodeListe henleggArsakerKlage;
    public KodeListe henleggArsakerInnsyn;
    
    public boolean ikkeVentPåStatus = false; //TODO hack for økonomioppdrag
    
    public Saksbehandler() {
        super();
        fagsakKlient = new FagsakKlient(session);
        behandlingerKlient = new BehandlingerKlient(session);
        kodeverkKlient = new KodeverkKlient(session);
        dokumentKlient = new DokumentKlient(session);
        brevKlient = new BrevKlient(session);
        historikkKlient = new HistorikkKlient(session);
        prosesstaskKlient = new ProsesstaskKlient(session);
    }

    public Saksbehandler(Rolle rolle) throws IOException {
        this();
        erLoggetInnMedRolle(rolle);
    }
    
    @Override
    public void erLoggetInnMedRolle(Rolle rolle) throws IOException {
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
    @Step("Hent fagsak {saksnummer}")
    public void hentFagsak(long saksnummer) throws Exception {
        hentFagsak("" + saksnummer);
    }

    /*
     * Søker etter fagsaker
     */
    @Step("Søker etter fagsak {søk}")
    public void søkEtterFagsak(String søk) throws Exception {
        fagsaker = fagsakKlient.søk(søk);
        if(fagsaker.size() == 1) {
            velgFagsak(fagsaker.get(0));
        }
    }
    
    /*
     * Refresh
     */
    public void refreshBehandling() throws Exception {
        velgBehandling(valgtBehandling);
    }
    
    public void refreshFagsak() throws Exception {
        Behandling behandling = valgtBehandling;
        hentFagsak(valgtFagsak.saksnummer);
        if(valgtBehandling == null && behandling != null) {
            velgBehandling(behandling);
        }
    }
    
    /*
     * Velger fagsak
     */
    @Step("Velger fagsak {fagsak}")
    public void velgFagsak(Fagsak fagsak) throws Exception {
        if(fagsak == null) {
            throw new RuntimeException("Kan ikke velge fagsak. fagsak er null");
        }
        valgtFagsak = fagsak;
        
        behandlinger = behandlingerKlient.alle(fagsak.saksnummer);
        valgtBehandling = null;
        
        if(behandlinger.size() == 1) { //ellers må en velge explisit
            velgBehandling(behandlinger.get(0));
        }
    }
    
    /*
     * velger behandling som valgt behandling
     */
    public void velgBehandling(Kode behandlingstype) throws Exception {
        Behandling behandling = getBehandling(behandlingstype);
        if(null != behandling) {
            velgBehandling(behandling);
        }
        else {
            throw new RuntimeException("Valgt fagsak har ikke behandling av type: " + behandlingstype.kode);
        }
    }

    @Step("Velger behandling {behandling}")
    public void velgBehandling(Behandling behandling) throws Exception {
        ventPåStatus(behandling);
        
        Deffered<Behandling> dBehandling = new Deffered<>(() -> {
            return behandlingerKlient.getBehandling(behandling.id);
        });
        Deffered<List<HistorikkInnslag>> dHistorikkInnslag = new Deffered<>(() -> {
            return historikkKlient.hentHistorikk(valgtFagsak.saksnummer);
        });
        Deffered<List<DokumentListeEnhet>> dDokumentListeEnhet = new Deffered<>(() -> {
            return dokumentKlient.hentDokumentliste(valgtFagsak.saksnummer);
        });
        Deffered<Behandling> dAnnenPartBehandling = new Deffered<>(() -> {
            return behandlingerKlient.annenPartBehandling(valgtFagsak.saksnummer);
        });
        
        
        valgtBehandling = dBehandling.get();
        populateBehandling(valgtBehandling);
        
        dokumenter = dDokumentListeEnhet.get();
        historikkInnslag = dHistorikkInnslag.get();
        annenPartBehandling = dAnnenPartBehandling.get();
    }
    
    private void populateBehandling(Behandling behandling) throws Exception {
        Deffered<List<Aksjonspunkt>> dAksonspunkter = new Deffered<>(() -> {
            return behandlingerKlient.getBehandlingAksjonspunkt(behandling.id);
        });
        
        if (behandling.type.navn.equals("Klage")) {
            valgtBehandling.klagevurdering = behandlingerKlient.klage(behandling.id);
        } else {
            BehandlingResourceRequest request = new BehandlingResourceRequest(valgtBehandling.id, valgtFagsak.saksnummer);
            
            Deffered<Personopplysning> dPersonopplysninger = new Deffered<>(() -> {
                return behandlingerKlient.behandlingPersonopplysninger(request);
            });
            Deffered<Verge> dVerge = new Deffered<>(() -> {
                return behandlingerKlient.behandlingVerge(request);
            });
            Deffered<Beregningsgrunnlag> dBeregningsgrunnlag = new Deffered<>(() -> {
                return behandlingerKlient.behandlingBeregningsgrunnlag(request);
            });
            Deffered<Beregningsresultat> dBeregningsresultat = new Deffered<>(() -> {
                return behandlingerKlient.behandlingBeregningsresultatEngangsstønad(request);
            });
            Deffered<BeregningsresultatMedUttaksplan> dBeregningsresultatMedUttaksplan = new Deffered<>(() -> {
                return behandlingerKlient.behandlingBeregningsresultatForeldrepenger(request);
            });
            Deffered<Soknad> dSoknad = new Deffered<>(() -> {
                return behandlingerKlient.behandlingSøknad(request);
            });
            Deffered<Familiehendelse> dFamiliehendelse = new Deffered<>(() -> {
                return behandlingerKlient.behandlingFamiliehendelse(request);
            });
            Deffered<Opptjening> dOpptjening = new Deffered<>(() -> {
                return behandlingerKlient.behandlingOpptjening(request);
            });
            Deffered<InntektArbeidYtelse> dInntektArbeidYtelse = new Deffered<>(() -> {
                return behandlingerKlient.behandlingInntektArbeidYtelse(request);
            });
            Deffered<KontrollerFaktaData> dKontrollerFaktaData = new Deffered<>(() -> {
                return behandlingerKlient.behandlingKontrollerFaktaPerioder(request);
            });
            Deffered<Medlem> dMedlem = new Deffered<>(() -> {
                return behandlingerKlient.behandlingMedlemskap(request);
            });
            Deffered<UttakResultatPerioder> dUttakResultatPerioder = new Deffered<>(() -> {
                return behandlingerKlient.behandlingUttakResultatPerioder(request);
            });
            
            
            valgtBehandling.personopplysning = dPersonopplysninger.get();
            valgtBehandling.verge = dVerge.get();
            valgtBehandling.beregningsgrunnlag = dBeregningsgrunnlag.get();
            valgtBehandling.beregningResultatEngangsstonad = dBeregningsresultat.get();
            valgtBehandling.beregningResultatForeldrepenger = dBeregningsresultatMedUttaksplan.get();
            valgtBehandling.soknad = dSoknad.get();
            valgtBehandling.familiehendelse = dFamiliehendelse.get();
            valgtBehandling.opptjening = dOpptjening.get();
            valgtBehandling.inntektArbeidYtelse = dInntektArbeidYtelse.get();
            valgtBehandling.kontrollerFaktaData = dKontrollerFaktaData.get();
            valgtBehandling.medlem = dMedlem.get();
            valgtBehandling.uttakResultatPerioder = dUttakResultatPerioder.get();
        }
        
        valgtBehandling.aksjonspunkter = dAksonspunkter.get();
        for (Aksjonspunkt aksjonspunkt : valgtBehandling.aksjonspunkter) {
            aksjonspunkt.setBekreftelse(AksjonspunktBekreftelse.fromAksjonspunkt(valgtFagsak, valgtBehandling, aksjonspunkt));
        }
    }
    
    private void ventPåStatus(Behandling behandling) throws Exception {
        if(!ikkeVentPåStatus) {
            Vent.til(() -> {
                return verifiserStatusForBehandling(behandling);
            }, 60, () -> {
                List<ProsessTaskListItemDto> prosessTasker = hentProsesstaskerForBehandling(behandling);
                String prosessTaskList = "";
                for (ProsessTaskListItemDto prosessTaskListItemDto : prosessTasker) {
                    prosessTaskList += prosessTaskListItemDto.getTaskType() + " - " + prosessTaskListItemDto.getStatus() + "\n";
                }
                return "Behandling status var ikke klar men har ikke feilet\n" + prosessTaskList;
            });
        }
    }
    
    private boolean verifiserStatusForBehandling(Behandling behandling) throws Exception {
        AsyncPollingStatus status = behandlingerKlient.statusAsObject(behandling.id, null);
        
        if(status == null) {
            return true;
        }
        else if(status.isPending()) {
            return false;
        }
        else {
            throw new RuntimeException("Status for behandling " + behandling.id + " feilet: " + status.getMessage());
        }
    }

    /*
     * Henting av kodeverk
     */
    public void hentKodeverk() {
        try {
            kodeverk = kodeverkKlient.getKodeverk();
            henleggArsaker = kodeverkKlient.henleggArsaker();
            henleggArsakerKlage = kodeverkKlient.henleggArsakerKlage();
            henleggArsakerInnsyn = kodeverkKlient.henleggArsakerInnsyn();
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
    @Step("Setter behandling på vent")
    public void settBehandlingPåVent(LocalDate frist, Kode årsak) throws Exception {
        behandlingerKlient.settPaVent(new BehandlingPaVent(valgtBehandling, frist, årsak));
        refreshBehandling();
    }
    
    @Step("Gjenopptar Behandling")
    public void gjenopptaBehandling() throws Exception {
        behandlingerKlient.gjenoppta(new BehandlingIdPost(valgtBehandling));
        refreshBehandling();
    }
    
    @Step("Henlegger behandling")
    public void henleggBehandling(Kode årsak) throws Exception {
        behandlingerKlient.henlegg(new BehandlingHenlegg(valgtBehandling.id, valgtBehandling.versjon, årsak.kode, "Henlagt"));
        refreshBehandling();
    }
    
    public <T extends AksjonspunktBekreftelse> T aksjonspunktBekreftelse(Class<T> type) {
        return hentAksjonspunktbekreftelse(type);
    }
    
    /*
     * Henter aksjonspunkt bekreftelse av gitt klasse
     */
    @SuppressWarnings("unchecked")
    @Step("Henter aksjonspunktbekreftelse for {type}")
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
    @Step("Henter aksjonspunkt {kode}")
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

    @Step("Bekrefter aksjonspunkt {bekreftelse}")
    public void bekreftAksjonspunktBekreftelse(AksjonspunktBekreftelse bekreftelse) throws Exception {
        List<AksjonspunktBekreftelse> bekreftelser = new ArrayList<>();
        bekreftelser.add(bekreftelse);
        bekreftAksjonspunktbekreftelserer(bekreftelser);
    }
    
    public <T extends AksjonspunktBekreftelse> void bekreftAksjonspunktbekreftelserer(AksjonspunktBekreftelse... bekreftelser) throws Exception {
        bekreftAksjonspunktbekreftelserer(Arrays.asList(bekreftelser));
    }
    
    public void bekreftAksjonspunktbekreftelserer(List<AksjonspunktBekreftelse> bekreftelser) throws Exception {
        BekreftedeAksjonspunkter aksjonspunkter = new BekreftedeAksjonspunkter(valgtFagsak, valgtBehandling, bekreftelser);
        behandlingerKlient.postBehandlingAksjonspunkt(aksjonspunkter);
        refreshBehandling();
    }
    
    /*
     * Oversyring
     */
    public void overstyr(AksjonspunktBekreftelse bekreftelse) throws Exception {
        List<AksjonspunktBekreftelse> bekreftelser = new ArrayList<>();
        bekreftelser.add(bekreftelse);
        overstyr(bekreftelser);
    }
    
    @Step("Overstyrer Aksonspunkt")
    public void overstyr(List<AksjonspunktBekreftelse> bekreftelser) throws Exception {
        OverstyrAksjonspunkter aksjonspunkter = new OverstyrAksjonspunkter(valgtFagsak, valgtBehandling, bekreftelser);
        behandlingerKlient.overstyr(aksjonspunkter);
        refreshBehandling();
    }
    
    /*
     * Opretter behandling på nåværende fagsak
     */
    public void opprettBehandling(Kode behandlingstype, Kode årsak) throws Exception {
        opprettBehandling(behandlingstype, årsak, valgtFagsak);
        hentFagsak(valgtFagsak.saksnummer);
    }

    public void opprettBehandlingRevurdering(Kode årsak) throws Exception {
        opprettBehandling(kodeverk.BehandlingType.getKode("Revurdering"), årsak);
    }
    
    public void oprettBehandlingInnsyn(Kode årsak) throws Exception {
        opprettBehandling(kodeverk.BehandlingType.getKode("BT-006"), årsak);
    }
    

    /*
     * Brev
     */
    @Step("Sender breev med malkode {brevmalKode} til mottaker {mottaker}")
    public void sendBrev(String brevmalKode, String mottaker, String fritekst) throws IOException {
        brevKlient.bestill(new BestillBrev(valgtBehandling.id,
                                           mottaker,
                                           brevmalKode, 
                                           fritekst));
    }
    
    
    /*
     * Dokumenter
     */
    @Step("Venter på dokument {dokument}")
    public void ventTilDokument(String dokument) throws Exception{
        if(harDokument(dokument)) {
            return;
        }
        Vent.til(() -> {
            refreshBehandling();
            return harDokument(dokument);
        }, 5, "Behandling har ikke dokument: " + dokument);
    }
    
    public boolean harDokument(String dokument) {
        return getDokument(dokument) != null;
    }
    
    private DokumentListeEnhet getDokument(String dokument) {
        throw new RuntimeException("getDokument ikke implementert");
    }
    
    /*
     * Historikkinnslag
     */
    @Step("Venter på historikkinnslag {tekst}")
    public void ventTilHistorikkinnslag(String tekst) throws Exception {
        if(harHistorikkinnslag(tekst)) {
            return;
        }
        Vent.til( () -> {
            refreshBehandling();
            return harHistorikkinnslag(tekst);
        }, 10, "Saken  hadde ikke historikkinslag " + tekst);
    }

    public boolean harHistorikkinnslag(String tekst) {
        return getHistorikkInnslag(tekst) != null; 
    }
    
    private HistorikkInnslag getHistorikkInnslag(String tekst) {
        for (HistorikkInnslag innslag : historikkInnslag) {
            if(innslag.getTekst().contains(tekst)) {
                return innslag;
            }
        }
        return null;
    }

    
    /*
     * Behandlingsstatus
     */
    @Step("Venter på behandlingsstatus {status}")
    public void ventTilBehandlingsstatus(String status) throws Exception {
        if(harBehandlingsstatus(status)) {
            return;
        }
        Vent.til(() -> {
            refreshBehandling();
            return harBehandlingsstatus(status);
        }, 10, "Behandlingsstatus var ikke " + status + " men var " + getBehandlingsstatus());
    }
    
    public boolean harBehandlingsstatus(String status) {
        return getBehandlingsstatus().equals(status);
    }
    
    public String getBehandlingsstatus() {
        return valgtBehandling.status.kode;
    }

    public void ventTilSakHarBehandling(Kode behandlingType) throws Exception {
        if(harBehandling(behandlingType)) {
            return;
        }
        Vent.til(() -> {
            refreshFagsak();
            return harBehandling(behandlingType);
        }, 10, "Saken har ikke fått behandling av type: " + behandlingType);
    }
    
    public boolean harBehandling(Kode behandlingType){
        for (Behandling behandling: behandlinger) {
            if (behandling.type.kode.equals(behandlingType.kode)){
                return true;
            }
        }
        return false;
    }
    
    public boolean harIkkeBehandling(Kode behandlingType) {
        return !harBehandling(behandlingType);
    }

    private Behandling getBehandling(Kode behandlingstype) {
        for (Behandling behandling : behandlinger) {
            if (behandling.type.kode.equals(behandlingstype.kode)) {
                return behandling;
            }
        }
        return null;
    }
    
    public void fattVedtak() throws Exception {
        ikkeVentPåStatus = true;
        bekreftAksjonspunktBekreftelse(FatterVedtakBekreftelse.class);
        ventTilBehandlingsstatus("AVSLU");
        ikkeVentPåStatus = false;
    }
    
    public void fattVedtakOgGodkjennØkonomioppdrag() throws Exception {
        ikkeVentPåStatus = true;
        bekreftAksjonspunktBekreftelse(FatterVedtakBekreftelse.class);
        ventOgGodkjennØkonomioppdrag();
        ikkeVentPåStatus = false;
    }
    
    public void ventOgGodkjennØkonomioppdrag() throws Exception {
       Vent.til(() ->  {
           return ventTilØkonomioppdragFerdigstilles();
       }, 10, "Fant ingen økonomioppdag å godkjenne");
       ventTilBehandlingsstatus("AVSLU");
    }

    @Step("Venter på økonomioppdrag")
    private boolean ventTilØkonomioppdragFerdigstilles() throws Exception {
        ventTilBehandlingsstatus("AVSLU");
        return true;
    }
    
    /*
     * Private
     */
    
    /*
     * Opretter behandling på gitt fagsak
     */
    private void opprettBehandling(Kode behandlingstype, Kode årsak, Fagsak fagsak) throws Exception {
        behandlingerKlient.putBehandlinger(new BehandlingNy(fagsak.saksnummer, behandlingstype.kode, årsak == null ? null : årsak.kode));
        velgFagsak(valgtFagsak); //Henter fagsaken på ny
    }
    
    private List<ProsessTaskListItemDto> hentProsesstaskerForBehandling(Behandling behandling) throws IOException {
        SokeFilterDto filter = new SokeFilterDto();
        filter.setSisteKjoeretidspunktFraOgMed(LocalDateTime.now().minusMinutes(5));
        filter.setSisteKjoeretidspunktTilOgMed(LocalDateTime.now());
        List<ProsessTaskListItemDto> prosesstasker = prosesstaskKlient.list(filter);
        return prosesstasker.stream().filter(p -> p.getTaskParametre().getBehandlingId() == "" + behandling.id).collect(Collectors.toList());
    }

    public boolean sakErKobletTilAnnenpart() {
        return annenPartBehandling != null;
    }

}
