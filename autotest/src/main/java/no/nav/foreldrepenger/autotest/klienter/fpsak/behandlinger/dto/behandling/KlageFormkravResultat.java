package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KlageFormkravResultat {
    protected Long paKlagdBehandlingId;
    protected String begrunnelse;
    protected boolean erKlagerPart;
    protected boolean erKlageKonkret;
    protected boolean erKlagefirstOverholdt;
    protected boolean erSignert;
    //protected List<KlageAvvistÅrsak> avvistArsaker;


    public Long getPaKlagdBehandlingId() {
        return paKlagdBehandlingId;
    }

    public void setPaKlagdBehandlingId(Long paKlagdBehandlingId) {
        this.paKlagdBehandlingId = paKlagdBehandlingId;
    }

    public String getBegrunnelse() {
        return begrunnelse;
    }

    public void setBegrunnelse(String begrunnelse) {
        this.begrunnelse = begrunnelse;
    }

    public boolean isErKlagerPart() {
        return erKlagerPart;
    }

    public void setErKlagerPart(boolean erKlagerPart) {
        this.erKlagerPart = erKlagerPart;
    }

    public boolean isErKlageKonkret() {
        return erKlageKonkret;
    }

    public void setErKlageKonkret(boolean erKlageKonkret) {
        this.erKlageKonkret = erKlageKonkret;
    }

    public boolean isErKlagefirstOverholdt() {
        return erKlagefirstOverholdt;
    }

    public void setErKlagefirstOverholdt(boolean erKlagefirstOverholdt) {
        this.erKlagefirstOverholdt = erKlagefirstOverholdt;
    }

    public boolean isErSignert() {
        return erSignert;
    }

    public void setErSignert(boolean erSignert) {
        this.erSignert = erSignert;
    }
}
