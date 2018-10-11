package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import org.apache.http.HttpResponse;

import no.nav.foreldrepenger.autotest.klienter.fpsak.FpsakKlient;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.BehandlingByttEnhet;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.BehandlingHenlegg;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.BehandlingIdPost;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.BehandlingNy;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.BehandlingPaVent;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.BehandlingResourceRequest;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.Ytelsefordeling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.BekreftedeAksjonspunkter;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Aksjonspunkt;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Familiehendelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.InnsynInfo;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.KlageInfo;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.KontrollerFaktaData;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Personopplysning;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Soknad;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Verge;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Vilkar;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.arbeid.InntektArbeidYtelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.Beregningsgrunnlag;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.Beregningsresultat;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.BeregningsresultatMedUttaksplan;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.medlem.Medlem;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.opptjening.Opptjening;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.uttak.PeriodeGrense;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.uttak.Stonadskontoer;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.uttak.UttakResultatPerioder;
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
    
    private static final String BEHANDLING_URL = "/behandling";
    private static final String BEHANDLING_PERSONOPPLYSNINGER_URL = BEHANDLING_URL + "/person/personopplysninger";
    private static final String BEHANDLING_VERGE_URL = BEHANDLING_URL + "/person/verge";
    private static final String BEHANDLING_PERSON_MEDLEMSKAP = BEHANDLING_URL + "/person/medlemskap";
    private static final String BEHANDLING_ENGANGSSTØNAD_URL = BEHANDLING_URL + "/beregningsresultat/engangsstonad";
    private static final String BEHANDLING_FORELDREPENGER_URL = BEHANDLING_URL + "/beregningsresultat/foreldrepenger";
    private static final String BEHANDLING_BEREGNINGSGRUNNALG_URL = BEHANDLING_URL + "/beregningsgrunnlag";
    private static final String BEHANDLING_VILKAAR_URL = BEHANDLING_URL + "/vilkar?behandlingId=%s";
    private static final String BEHANDLING_AKSJONSPUNKT_URL = BEHANDLING_URL + "/aksjonspunkt";
    private static final String BEHANDLING_AKSJONSPUNKT_GET_URL = BEHANDLING_AKSJONSPUNKT_URL + "?behandlingId=%s";
    private static final String BEHANDLING_AKSJONSPUNKT_OVERSTYR_URL = BEHANDLING_AKSJONSPUNKT_URL + "/overstyr";
    private static final String BEHANDLING_SOKNAD_URL = BEHANDLING_URL + "/soknad";
    private static final String BEHANDLING_FAMILIE_HENDELSE_URL = BEHANDLING_URL + "/familiehendelse";
    private static final String BEHANDLING_OPPTJENING_URL = BEHANDLING_URL + "/opptjening";
    private static final String BEHANDLING_INNTEKT_ARBEID_YTELSE_URL = BEHANDLING_URL + "/inntekt-arbeid-ytelse";
    private static final String BEHANDLING_INNSYN_URL = BEHANDLING_URL + "/innsyn?behandlingId=%s";
    private static final String BEHANDLING_KLAGE_URL = BEHANDLING_URL + "/klage?behandlingId=%s";
    private static final String BEHANDLING_YTELSEFORDELING_URL = BEHANDLING_URL + "/ytelsefordeling";
    
    private static final String BEHANDLING_UTTAK = BEHANDLING_URL + "/uttak";
    private static final String BEHANDLING_UTTAK_KONTROLLER_FAKTA_PERIODER_URL = BEHANDLING_UTTAK + "/kontroller-fakta-perioder";
    private static final String BEHANDLING_UTTAK_STONADSKONTOER_URL = BEHANDLING_UTTAK + "/stonadskontoer";
    private static final String BEHANDLING_UTTAK_RESULTAT_PERIODER_URL = BEHANDLING_UTTAK + "/resultat-perioder";
    private static final String BEHANDLING_UTTAK_PERIODE_GRENSE_URL = BEHANDLING_UTTAK + "/periode-grense";
    
    
    public BehandlingerKlient(HttpSession session) {
        super(session);
    }
    
    /*
     * Hent Behandling data
     */
    public Behandling getBehandling(int behandlingId) throws IOException {
        String url = hentRestRotUrl() + String.format(BEHANDLINGER_URL_GET, behandlingId);
        return getOgHentJson(url, Behandling.class, StatusRange.STATUS_SUCCESS);
    }
    
    public <T extends Behandling> T getBehandling(int behandlingId, Class<T> returnType) throws IOException {
        String url = hentRestRotUrl() + String.format(BEHANDLINGER_URL_GET, behandlingId);
        return getOgHentJson(url, returnType, StatusRange.STATUS_SUCCESS);
    }
    
    /*
     * Initier hent behandling
     */
    public void postBehandlinger(BehandlingResourceRequest behandling) throws IOException {
        String url = hentRestRotUrl() + BEHANDLINGER_URL;
        postJson(url, behandling);
    }
    
    /*
     * Opprett ny behandling
     */
    public void putBehandlinger(BehandlingNy behandling) throws IOException {
        String url = hentRestRotUrl() + BEHANDLINGER_URL;
        putJson(url, behandling, StatusRange.STATUS_SUCCESS);
    }

    /*
     * Hent status for behandling
     */
    public HttpResponse status(int behandlingId, Integer gruppe) throws IOException {
        session.setRedirect(false);
        String url = hentRestRotUrl() + String.format(BEHANDLINGER_STATUS_URL, behandlingId, gruppe);
        HttpResponse response = getJson(url);
        session.setRedirect(true);
        return response;
    }
    
    public boolean erStatusOk(int behandlingId, Integer gruppe) throws IOException {
        return StatusRange.STATUS_REDIRECT.inRange(status(behandlingId, gruppe).getStatusLine().getStatusCode());
    }
    
    /*
     * Set behandling på vent
     */
    public void settPaVent(BehandlingPaVent behandling) throws IOException {
        String url = hentRestRotUrl() + BEHANDLINGER_SETT_PA_VENT_URL;
        postOgVerifiser(url, behandling, StatusRange.STATUS_SUCCESS);
    }
    
    /*
     * Endre behandling på vent
     */
    public void endrePaVent(BehandlingPaVent behandling) throws IOException {
        String url = hentRestRotUrl() + BEHANDLINGER_ENDRE_PA_VENT_URL;
        postOgVerifiser(url, behandling, StatusRange.STATUS_SUCCESS);
    }
    
    /*
     * Henlegg behandling
     */
    public void henlegg(BehandlingHenlegg behandling) throws IOException {
        String url = hentRestRotUrl() + BEHANDLINGER_HENLEGG_URL;
        postOgVerifiser(url, behandling, StatusRange.STATUS_SUCCESS);
    }
    
    /*
     * Gjenoppta behandling på vent
     */
    public void gjenoppta(BehandlingIdPost behandling) throws IOException {
        String url = hentRestRotUrl() + BEHANDLINGER_GJENOPPTA_URL;
        postOgVerifiser(url, behandling, StatusRange.STATUS_SUCCESS);
    }
    
    /*
     * Byt behandlende enhet
     */
    public void byttEnhet(BehandlingByttEnhet behandling) throws IOException {
        String url = hentRestRotUrl() + BEHANDLINGER_BYTT_ENHET_URL;
        postOgVerifiser(url, behandling, StatusRange.STATUS_SUCCESS);
    }
    
    /*
     * Hent alle behandlinger
     */
    public List<Behandling> alle(long saksnummer) throws IOException {
        String url = hentRestRotUrl() + String.format(BEHANDLINGER_ALLE_URL, saksnummer);
        return getOgHentJson(url, hentObjectMapper().getTypeFactory().constructCollectionType(ArrayList.class, Behandling.class), StatusRange.STATUS_SUCCESS);
    }
    
    /*
     * ??
     */
    public void opneForEndringer(BehandlingIdPost behandling) throws IOException {
        String url = hentRestRotUrl() + BEHANDLINGER_OPNE_FOR_ENDRINGER_URL;
        postJson(url, behandling);
    }
    
    /*
     * Hent behandling for annen part
     */
    public Behandling annenPartBehandling(long saksnummer) throws IOException {
        String url = hentRestRotUrl() + String.format(BEHANDLINGER_ANNEN_PART_BEHANDLING_URL, saksnummer);
        return getOgHentJson(url, Behandling.class, StatusRange.STATUS_SUCCESS);
    }
    
    /*
     * Hent personopplysninger for behandling
     */
    public Personopplysning behandlingPersonopplysninger(BehandlingResourceRequest behandling) throws IOException {
        String url = hentRestRotUrl() + BEHANDLING_PERSONOPPLYSNINGER_URL;
        return postOgHentJson(url, behandling, Personopplysning.class, StatusRange.STATUS_SUCCESS);
    }
    
    /*
     * Hent verge for behandling
     */
    public Verge behandlingVerge(BehandlingResourceRequest behandling) throws IOException {
        String url = hentRestRotUrl() + BEHANDLING_VERGE_URL;
        return postOgHentJson(url, behandling, Verge.class, StatusRange.STATUS_SUCCESS);
    }
    
    /*
     * Hent medlemskap for behandling
     */
    public Medlem behandlingMedlemskap(BehandlingResourceRequest behandling) throws IOException {
        String url = hentRestRotUrl() + BEHANDLING_PERSON_MEDLEMSKAP;
        return postOgHentJson(url, behandling, Medlem.class, StatusRange.STATUS_SUCCESS);
    }
    
    /*
     * Hent beregningsgrunnlag for behandling
     */
    public Beregningsgrunnlag behandlingBeregningsgrunnlag(BehandlingResourceRequest behandling) throws IOException {
        String url = hentRestRotUrl() + BEHANDLING_BEREGNINGSGRUNNALG_URL;
        return postOgHentJson(url, behandling, Beregningsgrunnlag.class, StatusRange.STATUS_SUCCESS);
    }
    
    /*
     * Hent beregningsresultat engangstønad for behandling
     */
    public Beregningsresultat behandlingBeregningsresultatEngangsstønad(BehandlingResourceRequest behandling) throws IOException {
        String url = hentRestRotUrl() + BEHANDLING_ENGANGSSTØNAD_URL;
        return postOgHentJson(url, behandling, Beregningsresultat.class, StatusRange.STATUS_SUCCESS);
    }
    
    /*
     * Hent beregningsresultat foreldrepenger for behandling
     */
    public BeregningsresultatMedUttaksplan behandlingBeregningsresultatForeldrepenger(BehandlingResourceRequest behandling) throws IOException {
        String url = hentRestRotUrl() + BEHANDLING_FORELDREPENGER_URL;
        return postOgHentJson(url, behandling, BeregningsresultatMedUttaksplan.class, StatusRange.STATUS_SUCCESS);
    }
    
    /*
     * Hent vilkår for behandling
     */
    public List<Vilkar> behandlingVilkår(int behandlingsId) throws IOException {
        String url = hentRestRotUrl() + String.format(BEHANDLING_VILKAAR_URL, behandlingsId);
        return getOgHentJson(url, hentObjectMapper().getTypeFactory().constructCollectionType(ArrayList.class, Vilkar.class), StatusRange.STATUS_SUCCESS);
    }
    
    /*
     * Hent aksjonspunkter for behandling
     */
    public List<Aksjonspunkt> getBehandlingAksjonspunkt(int behandlingsId) throws IOException {
        String url = hentRestRotUrl() + String.format(BEHANDLING_AKSJONSPUNKT_GET_URL, behandlingsId);
        return getOgHentJson(url, hentObjectMapper().getTypeFactory().constructCollectionType(ArrayList.class, Aksjonspunkt.class), StatusRange.STATUS_SUCCESS);
    }
    
    public void postBehandlingAksjonspunkt(BekreftedeAksjonspunkter aksjonsunkter) throws IOException {
        String url = hentRestRotUrl() + BEHANDLING_AKSJONSPUNKT_URL;
        postOgVerifiser(url, aksjonsunkter, StatusRange.STATUS_SUCCESS);
    }
    
    /*
     * Overstyring
     */
    public void overstyr(BekreftedeAksjonspunkter aksjonsunkter) throws IOException {
        String url = hentRestRotUrl() + BEHANDLING_AKSJONSPUNKT_OVERSTYR_URL;
        postOgVerifiser(url, aksjonsunkter, StatusRange.STATUS_SUCCESS);
    }
    
    /*
     * Hent søknad for behandling
     */
    public Soknad behandlingSøknad(BehandlingResourceRequest behandling) throws IOException {
        String url = hentRestRotUrl() + BEHANDLING_SOKNAD_URL;
        return postOgHentJson(url, behandling, Soknad.class, StatusRange.STATUS_SUCCESS);
    }
    
    /*
     * hent familiehendelse for behandling
     */
    public Familiehendelse behandlingFamiliehendelse(BehandlingResourceRequest behandling) throws IOException {
        String url = hentRestRotUrl() + BEHANDLING_FAMILIE_HENDELSE_URL;
        return postOgHentJson(url, behandling, Familiehendelse.class, StatusRange.STATUS_SUCCESS);
    }
    
    /*
     * Hent opptjening for behandling
     */
    public Opptjening behandlingOpptjening(BehandlingResourceRequest behandling) throws IOException {
        String url = hentRestRotUrl() + BEHANDLING_OPPTJENING_URL;
        return postOgHentJson(url, behandling, Opptjening.class, StatusRange.STATUS_SUCCESS);
    }
    
    /*
     * hent inntekt arbeid ytelse for behandling
     */
    public InntektArbeidYtelse behandlingInntektArbeidYtelse(BehandlingResourceRequest behandling) throws IOException {
        String url = hentRestRotUrl() + BEHANDLING_INNTEKT_ARBEID_YTELSE_URL;
        return postOgHentJson(url, behandling, InntektArbeidYtelse.class, StatusRange.STATUS_SUCCESS);
    }
    
    /*
     * Hent insynsinfo for behandling
     */
    public InnsynInfo innsyn(int behandlingId) throws IOException {
        String url = hentRestRotUrl() + String.format(BEHANDLING_INNSYN_URL, behandlingId);
        return getOgHentJson(url, InnsynInfo.class, StatusRange.STATUS_SUCCESS);
    }
    
    /*
     * Hent klageinfo for behandling
     */
    public KlageInfo klage(int behandlingId) throws IOException {
        String url = hentRestRotUrl() + String.format(BEHANDLING_KLAGE_URL, behandlingId);
        return getOgHentJson(url, KlageInfo.class, StatusRange.STATUS_SUCCESS);
    }
    
    /*
     * Hent info om ytelsesfordeling
     */
    public Ytelsefordeling ytelsefordeling(BehandlingResourceRequest behandling) throws IOException {
        String url = hentRestRotUrl() + BEHANDLING_YTELSEFORDELING_URL;
        return postOgHentJson(url, behandling, Ytelsefordeling.class, StatusRange.STATUS_SUCCESS);
    }
    
    /*
     * hent stønadskontoer for behandling
     */
    public Stonadskontoer behandlingUttakStonadskontoer(BehandlingResourceRequest behandling) throws IOException {
        String url = hentRestRotUrl() + BEHANDLING_UTTAK_STONADSKONTOER_URL;
        return postOgHentJson(url, behandling, Stonadskontoer.class, StatusRange.STATUS_SUCCESS);
    }
    
    /*
     * hent kontroller fakta for behandling
     */
    public KontrollerFaktaData behandlingKontrollerFaktaPerioder(BehandlingResourceRequest behandling) throws IOException {
        String url = hentRestRotUrl() + BEHANDLING_UTTAK_KONTROLLER_FAKTA_PERIODER_URL;
        return postOgHentJson(url, behandling, KontrollerFaktaData.class, StatusRange.STATUS_SUCCESS);
    }
    
    /*
     * hent resultat perioder for behandling
     */
    public UttakResultatPerioder behandlingUttakResultatPerioder(BehandlingResourceRequest behandling) throws IOException {
        String url = hentRestRotUrl() + BEHANDLING_UTTAK_RESULTAT_PERIODER_URL;
        return postOgHentJson(url, behandling, UttakResultatPerioder.class, StatusRange.STATUS_SUCCESS);
    }
    
    /*
     * hent periode grense for behandlign
     */
    
    public PeriodeGrense behandlingUttakPeriodeGrense(BehandlingResourceRequest behandling) throws IOException {
        String url = hentRestRotUrl() + BEHANDLING_UTTAK_PERIODE_GRENSE_URL;
        return postOgHentJson(url, behandling, PeriodeGrense.class, StatusRange.STATUS_SUCCESS);
    }
    
}
