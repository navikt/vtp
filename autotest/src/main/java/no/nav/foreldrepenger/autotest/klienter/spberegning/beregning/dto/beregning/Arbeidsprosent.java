package no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.dto.beregning;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Arbeidsprosent {
    public Double verdi;

    public Arbeidsprosent() { }

    public Arbeidsprosent(Double verdi){
        this.verdi = verdi;
    }

    public Double getVerdi() {
        return verdi;
    }

    public void setVerdi(Double verdi) {
        this.verdi = verdi;
    }
}
