package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KlageInfo {
    protected KlageVurderingResultat klageVurderingResultatNFP;
    protected KlageVurderingResultat klageVurderingResultatNK;
    protected KlageFormkravResultat klageFormkravResultatNFP;
    protected KlageFormkravResultat klageFormkravResultatKA;

    public KlageVurderingResultat getKlageVurderingResultatNFP() { return klageVurderingResultatNFP; }

    public void setKlageVurderingResultatNFP(KlageVurderingResultat klageVurderingResultatNFP) {
        this.klageVurderingResultatNFP = klageVurderingResultatNFP;
    }

    public KlageVurderingResultat getKlageVurderingResultatNK() { return klageVurderingResultatNK; }

    public void setKlageVurderingResultatNK(KlageVurderingResultat klageVurderingResultatNK) {
        this.klageVurderingResultatNK = klageVurderingResultatNK;
    }

    public KlageFormkravResultat getKlageFormkravResultatNFP() { return klageFormkravResultatNFP; }

    public void setKlageFormkravResultatNFP(KlageFormkravResultat klageFormkravResultatNFP) {
        this.klageFormkravResultatNFP = klageFormkravResultatNFP;
    }

    public KlageFormkravResultat getKlageFormkravResultatKA() { return klageFormkravResultatKA; }

    public void setKlageFormkravResultatKA(KlageFormkravResultat klageFormkravResultatKA) {
        this.klageFormkravResultatKA = klageFormkravResultatKA;
    }
}
