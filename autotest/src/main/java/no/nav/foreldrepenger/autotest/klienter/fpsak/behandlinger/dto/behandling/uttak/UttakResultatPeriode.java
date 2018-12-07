package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.uttak;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UttakResultatPeriode {

    protected LocalDate fom = null;
    protected LocalDate tom = null;

    protected List<UttakResultatPeriodeAktivitet> aktiviteter = null;

    protected Kode periodeResultatType = null;
    protected String begrunnelse = null;
    protected Kode periodeResultatÅrsak = null;
    protected Kode manuellBehandlingÅrsak = null;
    protected Kode graderingAvslagÅrsak = null;
    protected Boolean flerbarnsdager = null;
    protected Boolean samtidigUttak;
    protected Boolean graderingInnvilget;
    protected Kode periodeType = null;
    protected Kode utsettelseType = null;
    protected Kode oppholdÅrsak = null;

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

    public Kode getOppholdÅrsak() {
        return oppholdÅrsak;
    }

    public void setOppholdÅrsak(Kode oppholdÅrsak) {
        this.oppholdÅrsak = oppholdÅrsak;
    }
}
