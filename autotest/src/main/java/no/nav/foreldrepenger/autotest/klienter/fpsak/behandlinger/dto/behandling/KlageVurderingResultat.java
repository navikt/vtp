package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KlageVurderingResultat {
    protected String klageVurdering;
    protected String begrunnelse;
    protected String fritekstTilBrev;
    protected String klageAvvistArsakNavn;
    protected String klageMedholdArsak;
    protected String klageMedholdArsakNavn;
    protected String klageVurderingOmgjoer;
    protected String klageVurdertAv;
    protected LocalDate vedtaksdatoPaklagdBehandling;

    public String getKlageVurdering() {
        return klageVurdering;
    }

    public void setKlageVurdering(String klageVurdering) {
        this.klageVurdering = klageVurdering;
    }

    public String getBegrunnelse() {
        return begrunnelse;
    }

    public void setBegrunnelse(String begrunnelse) {
        this.begrunnelse = begrunnelse;
    }

    public String getFritekstTilBrev() {
        return fritekstTilBrev;
    }

    public void setFritekstTilBrev(String fritekstTilBrev) {
        this.fritekstTilBrev = fritekstTilBrev;
    }

    public String getKlageAvvistArsakNavn() {
        return klageAvvistArsakNavn;
    }

    public void setKlageAvvistArsakNavn(String klageAvvistArsakNavn) {
        this.klageAvvistArsakNavn = klageAvvistArsakNavn;
    }

    public String getKlageMedholdArsak() {
        return klageMedholdArsak;
    }

    public void setKlageMedholdArsak(String klageMedholdArsak) {
        this.klageMedholdArsak = klageMedholdArsak;
    }

    public String getKlageMedholdArsakNavn() {
        return klageMedholdArsakNavn;
    }

    public void setKlageMedholdArsakNavn(String klageMedholdArsakNavn) {
        this.klageMedholdArsakNavn = klageMedholdArsakNavn;
    }

    public String getKlageVurderingOmgjoer() {
        return klageVurderingOmgjoer;
    }

    public void setKlageVurderingOmgjoer(String klageVurderingOmgjoer) {
        this.klageVurderingOmgjoer = klageVurderingOmgjoer;
    }

    public String getKlageVurdertAv() {
        return klageVurdertAv;
    }

    public void setKlageVurdertAv(String klageVurdertAv) {
        this.klageVurdertAv = klageVurdertAv;
    }

    public LocalDate getVedtaksdatoPaklagdBehandling() {
        return vedtaksdatoPaklagdBehandling;
    }

    public void setVedtaksdatoPaklagdBehandling(LocalDate vedtaksdatoPaklagdBehandling) {
        this.vedtaksdatoPaklagdBehandling = vedtaksdatoPaklagdBehandling;
    }
}
