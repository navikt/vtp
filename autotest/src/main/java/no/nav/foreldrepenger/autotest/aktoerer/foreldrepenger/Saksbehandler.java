package no.nav.foreldrepenger.autotest.aktoerer.foreldrepenger;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
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
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.KlageVurderingResultatAksjonspunktMellomlagringDto;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.AksjonspunktBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.BekreftedeAksjonspunkter;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.FatterVedtakBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.OverstyrAksjonspunkter;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Aksjonspunkt;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.AksjonspunktKoder;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Vilkar;
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
import no.nav.foreldrepenger.autotest.klienter.fpsak.prosesstask.ProsesstaskKlient;
import no.nav.foreldrepenger.autotest.klienter.fpsak.prosesstask.dto.ProsessTaskListItemDto;
import no.nav.foreldrepenger.autotest.klienter.fpsak.prosesstask.dto.SokeFilterDto;
import no.nav.foreldrepenger.autotest.util.AllureHelper;
import no.nav.foreldrepenger.autotest.util.deferred.Deffered;
import no.nav.foreldrepenger.autotest.util.konfigurasjon.MiljoKonfigurasjon;
import no.nav.foreldrepenger.autotest.util.vent.Vent;

public class Saksbehandler extends Aktoer{

    
    public List<Fagsak> fagsaker;
    public Fagsak valgtFagsak;
    
    private Deffered<List<DokumentListeEnhet>> dokumenter;
    private Deffered<List<HistorikkInnslag>> historikkInnslag;
    private Deffered<Behandling> annenPartBehandling;
    
    public List<Behandling> behandlinger;
    public Behandling valgtBehandling;
    
    
    private FagsakKlient fagsakKlient;
    private BehandlingerKlient behandlingerKlient;
    private KodeverkKlient kodeverkKlient;
    private DokumentKlient dokumentKlient;
    private BrevKlient brevKlient;
    private HistorikkKlient historikkKlient;
    private ProsesstaskKlient prosesstaskKlient;
    
    public Kodeverk kodeverk;
    
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
    @Step("Refresh behandling")
    public void refreshBehandling() throws Exception {
        velgBehandling(valgtBehandling);
    }

    @Step("Refresh fagsak")
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
    protected void velgBehandling(Kode behandlingstype) throws Exception {
        Behandling behandling = getBehandling(behandlingstype);
        if(null != behandling) {
            velgBehandling(behandling);
        }
        else {
            throw new RuntimeException("Valgt fagsak har ikke behandling av type: " + behandlingstype.kode);
        }
    }

    public void velgFørstegangsbehandling() throws Exception {
        velgBehandling(kodeverk.BehandlingType.getKode("BT-002"));
    }
    
    public void velgKlageBehandling() throws Exception {
        velgBehandling(kodeverk.BehandlingType.getKode("BT-003"));
    }
    
    public void velgRevurderingBehandling() throws Exception {
        velgBehandling(kodeverk.BehandlingType.getKode("BT-004"));
    }
    
    public void velgDokumentInnsynBehandling() throws Exception {
        velgBehandling(kodeverk.BehandlingType.getKode("BT-006"));
    }
    
    @Step("Velger behandling {behandling}")
    public void velgBehandling(Behandling behandling) throws Exception {
        ventPåStatus(behandling);
        
        Deffered<Behandling> dBehandling = Deffered.deffered(() -> {
            return behandlingerKlient.getBehandling(behandling.id);
        });
        Deffered<List<HistorikkInnslag>> dHistorikkInnslag = Deffered.deffered(() -> {
            return historikkKlient.hentHistorikk(valgtFagsak.saksnummer);
        });
        Deffered<List<DokumentListeEnhet>> dDokumentListeEnhet = Deffered.deffered(() -> {
            return dokumentKlient.hentDokumentliste(valgtFagsak.saksnummer);
        });
        Deffered<Behandling> dAnnenPartBehandling = Deffered.deffered(() -> {
            return behandlingerKlient.annenPartBehandling(valgtFagsak.saksnummer);
        });
        
        
        valgtBehandling = dBehandling.get();
        populateBehandling(valgtBehandling);
        
        setDokumenter(dDokumentListeEnhet);
        setHistorikkInnslag(dHistorikkInnslag);
        setAnnenPartBehandling(dAnnenPartBehandling);
    }
    
    private void populateBehandling(Behandling behandling) throws Exception {
        

        /*
        KODE	OFFISIELL_KODE	BESKRIVELSE
        BT-002	ae0034	Førstegangsbehandling
        BT-003	ae0058	Klage
        BT-004	ae0028	Revurdering
        BT-005	ae0043	Tilbakebetaling endring
        BT-006	ae0042	Dokumentinnsyn
         */

        if(behandling.type.kode.equalsIgnoreCase("BT-006") /* Dokumentinnsyn*/) {
            
        } else if (behandling.type.kode.equalsIgnoreCase("BT-003" /* Klage */)) {
            var behandlingId = behandling.id;
            valgtBehandling.setKlagevurdering(Deffered.deffered(() -> {
                return behandlingerKlient.klage(behandlingId);
            }));
        } else {
            BehandlingResourceRequest request = new BehandlingResourceRequest(valgtBehandling.id, valgtFagsak.saksnummer);
            
            // FIXME: Forespørslene her burde konsultere resultat for valgtbehandling for å sjekke om URLene er tilgjengelig før de kjører.
            // URLene kan endre seg, men koden i behandlingerKlient tar ikke hensyn til det p.t. I tillegg er det unødvendig å spørre på noe som ikke finnes
            // slik det skjer nå.
            
            valgtBehandling.setPersonopplysning(Deffered.deffered(() -> {
                return behandlingerKlient.behandlingPersonopplysninger(request);
            }));
            valgtBehandling.setVerge(Deffered.deffered(() -> {
                return behandlingerKlient.behandlingVerge(request);
            }));
            valgtBehandling.setBeregningsgrunnlag(Deffered.deffered(() -> {
                return behandlingerKlient.behandlingBeregningsgrunnlag(request);
            }));
            valgtBehandling.setBeregningResultatEngangsstonad(Deffered.deffered(() -> {
                return behandlingerKlient.behandlingBeregningsresultatEngangsstønad(request);
            }));
            valgtBehandling.setBeregningResultatForeldrepenger(Deffered.deffered(() -> {
                return behandlingerKlient.behandlingBeregningsresultatForeldrepenger(request);
            }));
            valgtBehandling.setSoknad(Deffered.deffered(() -> {
                return behandlingerKlient.behandlingSøknad(request);
            }));
            valgtBehandling.setFamiliehendelse(Deffered.deffered(() -> {
                return behandlingerKlient.behandlingFamiliehendelse(request);
            }));
            valgtBehandling.setOpptjening(Deffered.deffered(() -> {
                return behandlingerKlient.behandlingOpptjening(request);
            }));
            valgtBehandling.setInntektArbeidYtelse(Deffered.deffered(() -> {
                return behandlingerKlient.behandlingInntektArbeidYtelse(request);
            }));
            valgtBehandling.setKontrollerFaktaData(Deffered.deffered(() -> {
                return behandlingerKlient.behandlingKontrollerFaktaPerioder(request);
            }));
            valgtBehandling.setMedlem(Deffered.deffered(() -> {
                return behandlingerKlient.behandlingMedlemskap(request);
            }));
            valgtBehandling.setUttakResultatPerioder(Deffered.deffered(() -> {
                return behandlingerKlient.behandlingUttakResultatPerioder(request);
            }));
            if (!valgtBehandling.getUttakResultatPerioder().getPerioderForSøker().isEmpty()) {
                valgtBehandling.setSaldoer(Deffered.deffered(() -> {
                    return behandlingerKlient.behandlingUttakStonadskontoer(request);
                }));
            }
        }
        
        valgtBehandling.setAksjonspunkter(Deffered.deffered(() -> {
            return behandlingerKlient.getBehandlingAksjonspunkt(behandling.id);
        }));
        valgtBehandling.setVilkar(Deffered.deffered(() -> {
            return behandlingerKlient.behandlingVilkår(behandling.id);
        }));
        for (Aksjonspunkt aksjonspunkt : valgtBehandling.getAksjonspunkter()) {
            aksjonspunkt.setBekreftelse(AksjonspunktBekreftelse.fromAksjonspunkt(valgtFagsak, valgtBehandling, aksjonspunkt));
        }
    }
    
    private void ventPåStatus(Behandling behandling) throws Exception {
        if(!ikkeVentPåStatus) {
            Vent.til(() -> {
                return verifiserStatusForBehandling(behandling);
            }, 90, () -> {
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
        else if(status.getStatusCode() == 418){
            if(status.getStatus() != AsyncPollingStatus.Status.DELAYED) {
                AllureHelper.debugFritekst("Prosesstask feilet i behandlingsverifisering: " + status.getMessage());
                throw new IllegalStateException("Prosesstask i vrang tilstand: " + status.getMessage());
            } else {
                AllureHelper.debugFritekst("Prossesstask DELAYED: "+status.getMessage());
                return false;
            }
        }
        else if(status.isPending()) {
            return false;
        }
        else {
            AllureHelper.debugFritekst("Prosesstask feilet for behandling[" + behandling.id + "] i behandlingsverifisering: " + status.getMessage());
            throw new RuntimeException("Status for behandling " + behandling.id + " feilet: " + status.getMessage());
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
    @Step("Setter behandling på vent")
    protected void settBehandlingPåVent(LocalDate frist, Kode årsak) throws Exception {
        behandlingerKlient.settPaVent(new BehandlingPaVent(valgtBehandling, frist, årsak));
        refreshBehandling();
    }
    
    public void settBehandlingPåVent(LocalDate frist, String årsak) throws Exception {
        settBehandlingPåVent(frist, kodeverk.Venteårsak.getKode(årsak));
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
        for (Aksjonspunkt aksjonspunkt : valgtBehandling.getAksjonspunkter()) {
            if(type.isInstance(aksjonspunkt.getBekreftelse())) {
                return (T) aksjonspunkt.getBekreftelse();
            }
        }
        AllureHelper.debugLoggBehandling("Behandling mangler aksjonspunkt: ", valgtBehandling);
        throw new RuntimeException("Valgt behandling (" + valgtBehandling.id + " - " + valgtFagsak.saksnummer + ") har ikke aksjonspunktbekreftelse: " + type.getName());
    }
    
    /*
     * Henter aksjonspunkt av gitt kode
     */
    @Step("Henter aksjonspunkt {kode}")
    public Aksjonspunkt hentAksjonspunkt(String kode) {
        for (Aksjonspunkt aksjonspunkt : valgtBehandling.getAksjonspunkter()) {
            if(aksjonspunkt.getDefinisjon().kode.equals(kode)) {
                return aksjonspunkt;
            }
        }
        return null;
    }

    /*
    * Sjekker om aksjonspunkt av gitt kode er på behandlingen
    */
    public boolean harAksjonspunkt(String kode) {
        AllureHelper.debugLoggBehandling(valgtBehandling);
        return null != hentAksjonspunkt(kode);
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
    
    public void bekreftAksjonspunktbekreftelserer(AksjonspunktBekreftelse... bekreftelser) throws Exception {
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

    protected void opprettBehandlingRevurdering(Kode årsak) throws Exception {
        opprettBehandling(kodeverk.BehandlingType.getKode("BT-004"), årsak);
    }
    
    public void opprettBehandlingRevurdering(String årsak) throws Exception {
        opprettBehandlingRevurdering(kodeverk.BehandlingÅrsakType.getKode(årsak));
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
        }, 30, () -> "Behandling har ikke dokument: " + dokument + "\n\tDokumenter:" + getDokumenter());
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
    @Step("Venter på historikkinnslag {type}")
    public void ventTilHistorikkinnslag(HistorikkInnslag.Type type) throws Exception {
        if(harHistorikkinnslag(type)) {
            return;
        }
        Vent.til( () -> {
            refreshBehandling();
            return harHistorikkinnslag(type);
        }, 20, () -> "Saken  hadde ikke historikkinslag " + type + "\n\tHistorikkInnslag:" + String.join("\t\n", String.valueOf(getHistorikkInnslag())));
    }

    /*
     * Historikkinnslag
     */
    @Step("Venter sekunder antall sekunder på historikkinnslag {type}")
    public void ventTilAntallHistorikkinnslag(HistorikkInnslag.Type type, Integer sekunder, Integer antallHistorikkInnslag) throws Exception {
        Vent.til( () -> {
            velgBehandling(valgtBehandling);
            return harAntallHistorikkinnslag(type) == antallHistorikkInnslag;
        }, sekunder, () -> "Saken  hadde ikke historikkinslag " + type + "\n\tHistorikkInnslag:" + String.join("\t\n", String.valueOf(getHistorikkInnslag())));
    }

    public boolean harHistorikkinnslag(HistorikkInnslag.Type type) {
        return getHistorikkInnslag(type) != null; 
    }

    private HistorikkInnslag getHistorikkInnslag(HistorikkInnslag.Type type) {
        for (HistorikkInnslag innslag : getHistorikkInnslag()) {
            if(innslag.getTypeKode().contains(type.getKode())) {
                return innslag;
            }
        }
        return null;
    }
    
    /*
     * Fagsakstatus
     */
    
    public String getFagsakstatus() {
        return valgtFagsak.hentStatus().kode;
    }
    
    public boolean harFagsakstatus(Kode status) {
        return valgtFagsak.hentStatus().equals(status);
    }
    
    protected void ventTilFagsakstatus(Kode status) throws Exception {
        if(harFagsakstatus(status)) {
            return;
        }
        Vent.til(() ->{
            refreshFagsak();
            return harFagsakstatus(status);
        }, 10, "Fagsak har ikke status " + status);
    }
    
    protected void ventTilFagsakstatus(String status) throws Exception {
        ventTilFagsakstatus(kodeverk.FagsakStatus.getKode(status));
    }

    public int harAntallHistorikkinnslag(HistorikkInnslag.Type type) {
        int antall = 0;
        for (HistorikkInnslag innslag : getHistorikkInnslag()) {
            if(innslag.getTypeKode().equals(type.getKode())) {
                antall++;
            }
        }
        return antall;
    }


    /*
     * Aksjonspunkt
     */
    @Step("Venter på aksjonspunkt {kode}")
    public void ventTilAksjonspunkt(String kode) throws Exception {
        if(harAksjonspunkt(kode)) {
            return;
        }
        Vent.til( () -> {
            refreshBehandling();
            return harAksjonspunkt(kode);
        }, 20, () -> "Saken  hadde ikke aksjonspunkt " + kode + (valgtBehandling==null? "" : "\n\tAksjonspunkter:" + valgtBehandling.getAksjonspunkter()));
    }

    private Vilkar hentVilkår(Kode vilkårKode) {
        for (Vilkar vilkår : valgtBehandling.getVilkar()) {
            if(vilkår.getVilkarType().equals(vilkårKode)) {
                return vilkår;
            }
        }
        throw new IllegalStateException(String.format("Fant ikke vilkår %s for behandling %s", vilkårKode.toString(), valgtBehandling.id));
    }
    
    public Vilkar hentVilkår(String vilkårKode) {
        return hentVilkår(new Kode("VILKAR_TYPE", vilkårKode));
    }
    
    public Kode vilkårStatus(String vilkårKode) {
        return hentVilkår(vilkårKode).getVilkarStatus();
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
        }, 60, "Behandlingsstatus var ikke " + status + " men var " + getBehandlingsstatus() + " i sak: " + valgtFagsak.saksnummer);
    }
    
    public boolean harBehandlingsstatus(String status) {
        return getBehandlingsstatus().equals(status);
    }
    
    public String getBehandlingsstatus() {
        return valgtBehandling.status.kode;
    }
    
    public void ventTilSakHarFørstegangsbehandling() throws Exception {
        ventTilSakHarBehandling(kodeverk.BehandlingType.getKode("BT-002"));
    }
    
    public void ventTilSakHarRevurdering() throws Exception {
        ventTilSakHarBehandling(kodeverk.BehandlingType.getKode("BT-004"));
    }
    
    public void ventTilSakHarKlage() throws Exception {
        ventTilSakHarBehandling(kodeverk.BehandlingType.getKode("BT-003"));
    }
    
    @Step("Venter på at fagsak får behandlingstype: {behandlingType}")
    protected void ventTilSakHarBehandling(Kode behandlingType) throws Exception {
        if(harBehandling(behandlingType)) {
            return;
        }
        Vent.til(() -> {
            refreshFagsak();
            return harBehandling(behandlingType);
        }, 30, "Saken har ikke fått behandling av type: " + behandlingType);
    }
    
    protected boolean harBehandling(Kode behandlingType){
        for (Behandling behandling: behandlinger) {
            if (behandling.type.kode.equals(behandlingType.kode)){
                return true;
            }
        }
        return false;
    }
    
    private Behandling getBehandling(Kode behandlingstype) {
        for (Behandling behandling : behandlinger) {
            if (behandling.type.kode.equals(behandlingstype.kode)) {
                return behandling;
            }
        }
        return null;
    }

    @Step("Fatter vedtak og venter til sak er avsluttet")
    public void fattVedtakOgVentTilAvsluttetBehandling() throws Exception {
        bekreftAksjonspunktBekreftelse(FatterVedtakBekreftelse.class);
        ventTilAvsluttetBehandling();
    }

    @Step("Venter til saken er avsluttet")
    public void ventTilAvsluttetBehandling() throws Exception {
        ventTilBehandlingsstatus("AVSLU");
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
        return getAnnenPartBehandling() != null;
    }

    public void mellomlagreKlage() throws Exception {
        behandlingerKlient.mellomlagre(new KlageVurderingResultatAksjonspunktMellomlagringDto(valgtBehandling, hentAksjonspunkt(AksjonspunktKoder.MANUELL_VURDERING_AV_KLAGE_NFP)));
        refreshBehandling();
    }
    
    public void mellomlagreOgGjennåpneKlage() throws Exception {
        behandlingerKlient.mellomlagreGjennapne(new KlageVurderingResultatAksjonspunktMellomlagringDto(valgtBehandling, hentAksjonspunkt(AksjonspunktKoder.MANUELL_VURDERING_AV_KLAGE_NFP)));
        refreshBehandling();
    }

    public boolean harRevurderingBehandling() {
        return harBehandling(kodeverk.BehandlingType.getKode("BT-004"));
    }

    public void ventTilFagsakAvsluttet() throws Exception {
        ventTilFagsakstatus("AVSLU");
    }
    
    public void ventTilFagsakLøpende() throws Exception {
        ventTilFagsakstatus("LOP");
    }

    public List<DokumentListeEnhet> getDokumenter() {
        return get(dokumenter);
    }

    private void setDokumenter(Deffered<List<DokumentListeEnhet>> dDokumentListeEnhet) {
        this.dokumenter = dDokumentListeEnhet;
    }

    public List<HistorikkInnslag> getHistorikkInnslag() {
        return get(historikkInnslag);
    }

    private void setHistorikkInnslag(Deffered<List<HistorikkInnslag>> dHistorikkInnslag) {
        this.historikkInnslag = dHistorikkInnslag;
    }

    public Behandling getAnnenPartBehandling() {
        return get(annenPartBehandling);
    }

    private void setAnnenPartBehandling(Deffered<Behandling> dAnnenPartBehandling) {
        this.annenPartBehandling = dAnnenPartBehandling;
    }
    
    private static <V> V get(Deffered<V> deferred) {
        try {
            return deferred.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
}
