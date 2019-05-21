package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Beregningsresultat {
    protected int beregnetTilkjentYtelse;
    protected int satsVerdi;
    protected int antallBarn;
    
    
    public int getBeregnetTilkjentYtelse() {
        return beregnetTilkjentYtelse;
    }
    
    public int getSatsVerdi() {
        return satsVerdi;
    }
}
