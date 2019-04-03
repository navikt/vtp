package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.uttak;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UttakResultatPeriode {

    protected LocalDate fom;
    protected LocalDate tom;
    protected List<UttakResultatPeriodeAktivitet> aktiviteter;
    protected Kode periodeResultatType;
    protected String begrunnelse;
    protected Kode periodeResultatÅrsak;
    protected Kode manuellBehandlingÅrsak;
    protected Kode graderingAvslagÅrsak;
    protected Boolean flerbarnsdager;
    protected Boolean samtidigUttak;
    protected BigDecimal samtidigUttaksprosent;
    protected Boolean graderingInnvilget;
    protected Kode periodeType;
    protected Kode utsettelseType;
    protected Kode oppholdÅrsak;
    protected UttakResultatPeriodeAktivitet gradertAktivitet = null;

    public void setBegrunnelse(String begrunnelse) {
        this.begrunnelse = begrunnelse;
    }

    public Kode getPeriodeResultatType() {
        return periodeResultatType;
    }

    public void setPeriodeResultatType(Kode periodeResultatType) {
        this.periodeResultatType = periodeResultatType;
    }

    public Kode getPeriodeResultatÅrsak() {
        return periodeResultatÅrsak;
    }

    public void setPeriodeResultatÅrsak(Kode periodeResultatÅrsak) {
        this.periodeResultatÅrsak = periodeResultatÅrsak;
    }

    public Kode getUtsettelseType() {
        return utsettelseType;
    }

    public void setUtsettelseType(Kode utsettelseType) {
        this.utsettelseType = utsettelseType;
    }

    public LocalDate getTom() {
        return tom;
    }

    public void setTom(LocalDate tom) {
        this.tom = tom;
    }

    public LocalDate getFom() {
        return fom;
    }

    public void setFom(LocalDate fom) {
        this.fom = fom;
    }

    public List<UttakResultatPeriodeAktivitet> getAktiviteter() {
        return aktiviteter;
    }

    public void setAktiviteter(List<UttakResultatPeriodeAktivitet> aktiviteter) {
        this.aktiviteter = aktiviteter;
    }

    public Kode getGraderingAvslagÅrsak() {
        return graderingAvslagÅrsak;
    }

    public void setGraderingAvslagÅrsak(Kode graderingAvslagÅrsak) {
        this.graderingAvslagÅrsak = graderingAvslagÅrsak;
    }

    public Boolean getGraderingInnvilget() {
        return graderingInnvilget;
    }

    public void setGraderingInnvilget(Boolean graderingInnvilget) {
        this.graderingInnvilget = graderingInnvilget;
    }

    public BigDecimal getGradertArbeidsprosent() {
        return gradertAktivitet.prosentArbeid;
    }
    
    public void setOppholdÅrsak(Kode oppholdÅrsak) {
        this.oppholdÅrsak = oppholdÅrsak;
    }
    public Kode getOppholdÅrsak() {
        return oppholdÅrsak;
    }
    
    public void setPeriodeType(Kode periodeType) {
        this.periodeType = periodeType;
    }

    public void setStønadskonto(Kode stønadskonto) {
        for (UttakResultatPeriodeAktivitet aktivitet : aktiviteter) {
            aktivitet.stønadskontoType = stønadskonto;
        }
    }

    public Boolean getFlerbarnsdager() {return flerbarnsdager;}

    public void setFlerbarnsdager(Boolean flerbarnsdager) {this.flerbarnsdager = flerbarnsdager;}

    public Boolean getSamtidigUttak() {return samtidigUttak;}

    public void setSamtidigUttak(Boolean samtidigUttak) {this.samtidigUttak = samtidigUttak;}

    public Kode getManuellBehandlingÅrsak() {return manuellBehandlingÅrsak;}

    public void setManuellBehandlingÅrsak(Kode manuellBehandlingÅrsak) {this.manuellBehandlingÅrsak = manuellBehandlingÅrsak;}
}
