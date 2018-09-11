package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger;

import java.io.IOException;
import java.util.List;

import com.google.gson.reflect.TypeToken;

import no.nav.foreldrepenger.autotest.klienter.fpsak.FpsakKlient;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.BehandlingByttEnhet;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.BehandlingHenlegg;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.BehandlingIdPost;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.BehandlingPaVent;
import no.nav.foreldrepenger.autotest.util.http.HttpSession;
import no.nav.foreldrepenger.autotest.util.http.rest.StatusRange;

public class BehandlingerKlient extends FpsakKlient{
    
    private static final String BEHANDLINGER_URL = "/behandlinger";
    
    private static final String BEHANDLINGER_URL_GET = BEHANDLINGER_URL + "?behandlingId=%S";
    
    private static final String BEHANDLINGER_STATUS_URL = BEHANDLINGER_URL + "/status?behandlingId=%s&grupp=%s";
    private static final String BEHANDLINGER_SETT_PA_VENT_URL = BEHANDLINGER_URL + "/sett-pa-vent";
    private static final String BEHANDLINGER_ENDRE_PA_VENT_URL = BEHANDLINGER_URL + "/endre-pa-vent";
    private static final String BEHANDLINGER_HENLEGG_URL = BEHANDLINGER_URL + "/henlegg";
    private static final String BEHANDLINGER_GJENOPPTA_URL = BEHANDLINGER_URL + "/gjenoppta";
    private static final String BEHANDLINGER_BYTT_ENHET_URL = BEHANDLINGER_URL + "/bytt-enhet";
    private static final String BEHANDLINGER_ALLE_URL = BEHANDLINGER_URL + "/alle?saksnummer=%s";
    private static final String BEHANDLINGER_OPNE_FOR_ENDRINGER_URL = BEHANDLINGER_URL + "/opne-for-endringer";
    private static final String BEHANDLINGER_ANNEN_PART_BEHANDLING_URL = BEHANDLINGER_URL + "/annen-part-behandling?saksnummer=%s";
    
    public BehandlingerKlient(HttpSession session) {
        super(session);
    }
    
    public Behandling getBehandling(int behandlingId) {
        String url = hentRestRotUrl() + String.format(BEHANDLINGER_URL_GET, behandlingId);
        return null;
    }
    
    public String postBehandlinger() {
        String url = hentRestRotUrl() + BEHANDLINGER_URL;
        return null;
    }
    
    public void putBehandlinger() {
        String url = hentRestRotUrl() + BEHANDLINGER_URL;
    }
    
    public Behandling status(int behandlingId, int gruppe) {
        String url = hentRestRotUrl() + String.format(BEHANDLINGER_STATUS_URL, behandlingId, gruppe);
        return null;
    }
    
    public void settPaVent(BehandlingPaVent behandling) {
        String url = hentRestRotUrl() + BEHANDLINGER_SETT_PA_VENT_URL;
    }
    
    public void endrePaVent(BehandlingPaVent behandling) {
        String url = hentRestRotUrl() + BEHANDLINGER_ENDRE_PA_VENT_URL;
    }
    
    public void henlegg(BehandlingHenlegg behandling) {
        String url = hentRestRotUrl() + BEHANDLINGER_HENLEGG_URL;
    }
    
    public void gjenoppta(BehandlingIdPost behandling) {
        String url = hentRestRotUrl() + BEHANDLINGER_GJENOPPTA_URL;
    }
    
    public void byttEnhet(BehandlingByttEnhet behandling) {
        String url = hentRestRotUrl() + BEHANDLINGER_BYTT_ENHET_URL;
    }
    
    public List<Behandling> alle(long saksnummer) throws IOException {
        String url = hentRestRotUrl() + String.format(BEHANDLINGER_ALLE_URL, saksnummer);
        return getOgHentJson(url, new TypeToken<List<Behandling>>() {}, StatusRange.STATUS_SUCCESS);
    }
    
    public void opneForEndringer(BehandlingIdPost behandling) {
        String url = hentRestRotUrl() + BEHANDLINGER_OPNE_FOR_ENDRINGER_URL;
    }
    
    public Behandling annenPartBehandling(long saksnummer) {
        String url = hentRestRotUrl() + String.format(BEHANDLINGER_ANNEN_PART_BEHANDLING_URL, saksnummer);
        return null;
    }
    
}
