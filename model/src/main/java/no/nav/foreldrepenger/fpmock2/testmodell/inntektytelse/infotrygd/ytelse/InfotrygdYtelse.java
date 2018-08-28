package no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.infotrygd.ytelse;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.infotrygd.InfotrygdSakBehandlingtema;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.infotrygd.InfotrygdSakResultat;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.infotrygd.InfotrygdSakStatus;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.infotrygd.InfotrygdSakType;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.infotrygd.InfotrygdTema;

/**
 * @see https://confluence.adeo.no/pages/viewpage.action?pageId=220537850 for ytterligere dokumentasjon av denne strukturen.
 */
public class InfotrygdYtelse {

    @JsonProperty("sakId")
    private String sakId;

    /** Registert tidspunkt. Påkrevd. */
    @JsonProperty("registrert")
    private LocalDateTime registrert;

    @JsonProperty("vedtatt")
    private LocalDateTime vedtatt;

    @JsonProperty("iverksatt")
    private LocalDateTime iverksatt;

    @JsonProperty("endret")
    private LocalDateTime endret;

    @JsonProperty("opphør")
    private LocalDateTime opphør;

    /** Saksbehandlers ident. Påkrevd. */
    @JsonProperty("saksbehandlerId")
    private String saksbehandlerId;

    /** Stønadsklasse 1. Påkrevd */
    @JsonProperty("tema")
    private InfotrygdTema tema;

    /** Stønadsklasse 2. Påkrevd */
    @JsonProperty("behandlingstema")
    private InfotrygdSakBehandlingtema behandlingstema;

    @JsonProperty("type")
    private InfotrygdSakType sakType;

    /** Status på ytelse. Påkrevd. */
    @JsonProperty("status")
    private InfotrygdSakStatus sakStatus;

    /** (brukes ikke av FPSAK). */
    @JsonProperty("resultat")
    private InfotrygdSakResultat resultat;

    public String getSakId() {
        return sakId;
    }

    public void setSakId(String sakId) {
        this.sakId = sakId;
    }

    public LocalDateTime getRegistrert() {
        return registrert;
    }

    public void setRegistrert(LocalDateTime registrert) {
        this.registrert = registrert;
    }

    public LocalDateTime getVedtatt() {
        return vedtatt;
    }

    public void setVedtatt(LocalDateTime vedtatt) {
        this.vedtatt = vedtatt;
    }

    public LocalDateTime getIverksatt() {
        return iverksatt;
    }

    public void setIverksatt(LocalDateTime iverksatt) {
        this.iverksatt = iverksatt;
    }

    public LocalDateTime getEndret() {
        return endret;
    }

    public void setEndret(LocalDateTime endret) {
        this.endret = endret;
    }

    public LocalDateTime getOpphørFom() {
        return opphør;
    }

    public void setOpphør(LocalDateTime opphør) {
        this.opphør = opphør;
    }

    public String getSaksbehandlerId() {
        return saksbehandlerId;
    }

    public void setSaksbehandlerId(String saksbehandlerId) {
        this.saksbehandlerId = saksbehandlerId;
    }

    public InfotrygdTema getTema() {
        return tema;
    }

    public void setTema(InfotrygdTema tema) {
        this.tema = tema;
    }

    public InfotrygdSakBehandlingtema getBehandlingtema() {
        return behandlingstema;
    }

    public void setBehandlingTema(InfotrygdSakBehandlingtema behandlingTema) {
        this.behandlingstema = behandlingTema;
    }

    public InfotrygdSakType getSakType() {
        return sakType;
    }

    public void setSakType(InfotrygdSakType sakType) {
        this.sakType = sakType;
    }

    public InfotrygdSakStatus getSakStatus() {
        return sakStatus;
    }

    public void setSakStatus(InfotrygdSakStatus sakStatus) {
        this.sakStatus = sakStatus;
    }

    public InfotrygdSakResultat getResultat() {
        return resultat;
    }

    public void setResultat(InfotrygdSakResultat resultat) {
        this.resultat = resultat;
    }

}
