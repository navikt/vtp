package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.omsorgspenger;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OmsorgspengerModell {
    @JsonProperty("rammemeldinger")
    private OmsorgspengerRammemeldingerModell rammemeldinger;

    public OmsorgspengerRammemeldingerModell getRammemeldinger() {
        if(rammemeldinger == null) {
            rammemeldinger = new OmsorgspengerRammemeldingerModell();
        }
        return rammemeldinger;
    }

    public void setRammemeldinger(OmsorgspengerRammemeldingerModell rammemeldinger) {
        this.rammemeldinger = rammemeldinger;
    }
}