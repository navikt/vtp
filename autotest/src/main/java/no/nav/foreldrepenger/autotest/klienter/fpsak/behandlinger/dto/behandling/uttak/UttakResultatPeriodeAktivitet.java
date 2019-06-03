package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.uttak;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UttakResultatPeriodeAktivitet {

    protected Kode stønadskontoType = null;
    protected BigDecimal trekkdagerDesimaler = null;
    protected BigDecimal prosentArbeid = null;
    protected BigDecimal utbetalingsgrad = null;
    protected Kode uttakArbeidType = null;
    protected Arbeidsgiver arbeidsgiver;
    
    public BigDecimal getUtbetalingsgrad() {
        return utbetalingsgrad;
    }
    public void setUtbetalingsgrad(BigDecimal utbetalingsgrad) {
        this.utbetalingsgrad = utbetalingsgrad;
    }
    public BigDecimal getTrekkdagerDesimaler() {
        return trekkdagerDesimaler;
    }
    public void setTrekkdagerDesimaler(BigDecimal trekkdagerDesimaler) {
        this.trekkdagerDesimaler = trekkdagerDesimaler;
    }
    public Kode getStønadskontoType() {
        return stønadskontoType;
    }
    public void setStønadskontoType(Kode stønadskontoType) {
        this.stønadskontoType = stønadskontoType;
    }
    public Arbeidsgiver getArbeidsgiver() {
        return arbeidsgiver;
    }
    public Kode getUttakArbeidType() {
        return uttakArbeidType;
    }
    public BigDecimal getProsentArbeid() {
        return prosentArbeid;
    }
}
