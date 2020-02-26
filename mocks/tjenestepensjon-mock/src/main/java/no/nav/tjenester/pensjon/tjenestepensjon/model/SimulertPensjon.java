package no.nav.tjenester.pensjon.tjenestepensjon.model;

import java.util.List;

public class SimulertPensjon {

    private String tpnr;
    private String navnOrdning;
    private List<String> inkluderteOrdninger;
    private String leverandorUrl;
    private List<String> inkluderteTpnr;
    private List<String> utelatteTpnr;
    private List<Utbetalingsperiode> utbetalingsperioder;
    private String status;
    private String feilkode;
    private String feilbeskrivelse;

    public String getTpnr() {
        return tpnr;
    }

    public void setTpnr(String tpnr) {
        this.tpnr = tpnr;
    }

    public String getNavnOrdning() {
        return navnOrdning;
    }

    public void setNavnOrdning(String navnOrdning) {
        this.navnOrdning = navnOrdning;
    }

    public List<String> getInkluderteOrdninger() {
        return inkluderteOrdninger;
    }

    public void setInkluderteOrdninger(List<String> inkluderteOrdninger) {
        this.inkluderteOrdninger = inkluderteOrdninger;
    }

    public String getLeverandorUrl() {
        return leverandorUrl;
    }

    public void setLeverandorUrl(String leverandorUrl) {
        this.leverandorUrl = leverandorUrl;
    }

    public List<String> getInkluderteTpnr() {
        return inkluderteTpnr;
    }

    public void setInkluderteTpnr(List<String> inkluderteTpnr) {
        this.inkluderteTpnr = inkluderteTpnr;
    }

    public List<String> getUtelatteTpnr() {
        return utelatteTpnr;
    }

    public void setUtelatteTpnr(List<String> utelatteTpnr) {
        this.utelatteTpnr = utelatteTpnr;
    }

    public List<Utbetalingsperiode> getUtbetalingsperioder() {
        return utbetalingsperioder;
    }

    public void setUtbetalingsperioder(List<Utbetalingsperiode> utbetalingsperioder) {
        this.utbetalingsperioder = utbetalingsperioder;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFeilkode() {
        return feilkode;
    }

    public void setFeilkode(String feilkode) {
        this.feilkode = feilkode;
    }

    public String getFeilbeskrivelse() {
        return feilbeskrivelse;
    }

    public void setFeilbeskrivelse(String feilbeskrivelse) {
        this.feilbeskrivelse = feilbeskrivelse;
    }
}
