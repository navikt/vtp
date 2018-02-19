package no.nav.tjeneste.virksomhet.infotrygdfelles.v1.modell;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import no.nav.tjeneste.virksomhet.infotrygdfelles.v1.modell.InfotrygdSvar;

@Entity(name = "InfotrygdYtelse")
@Table(name = "INFOTRYGDYTELSE")
public class InfotrygdYtelse {

    @Id
    @Column(name = "ID", nullable = false)
    Long id;

    @ManyToOne
    @JoinColumn(name = "INFOTRYGDSVAR_ID", nullable = false)
    InfotrygdSvar infotrygdSvar;

    @Column(name = "SAK_ID")
    String sakId;

    @Column(name = "REGISTRERT", nullable = false)
    LocalDateTime registrert;

    @Column(name = "TEMA")
    String tema;

    @Column(name = "BEHANDLINGSTEMA")
    String behandlingstema;

    @Column(name = "TYPE")
    String type;

    @Column(name = "STATUS")
    String status;

    @Column(name = "RESULTAT")
    String resultat;

    @Column(name = "SAKSBEHANDLER_ID")
    String saksbehandlerId;

    @Column(name = "VEDTATT")
    LocalDateTime vedtatt;

    @Column(name = "IVERKSATT")
    LocalDateTime iverksatt;

    @Column(name = "ENDRET")
    LocalDateTime endret;

    @Column(name = "OPPHOER_FOM")
    LocalDateTime opphoerFom;


    InfotrygdYtelse() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSakId() {
        return sakId;
    }

    public void setSakId(String sakId) {
        this.sakId = sakId;
    }

    public InfotrygdSvar getInfotrygdSvar() {
        return infotrygdSvar;
    }

    public LocalDateTime getRegistrert() {
        return registrert;
    }

    public void setRegistrert(LocalDateTime registrert) {
        this.registrert = registrert;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public String getBehandlingstema() {
        return behandlingstema;
    }

    public void setBehandlingstema(String behandlingstema) {
        this.behandlingstema = behandlingstema;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResultat() {
        return resultat;
    }

    public void setResultat(String resultat) {
        this.resultat = resultat;
    }

    public String getSaksbehandlerId() {
        return saksbehandlerId;
    }

    public void setSaksbehandlerId(String saksbehandlerId) {
        this.saksbehandlerId = saksbehandlerId;
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

    public LocalDateTime getOpphoerFom() {
        return opphoerFom;
    }

    public void setOpphoerFom(LocalDateTime opphoerFom) {
        this.opphoerFom = opphoerFom;
    }
}
