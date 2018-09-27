package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.uttak;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UttakResultatPeriode {

    private LocalDate fom = null;
    private LocalDate tom = null;

    private List<UttakResultatPeriodeAktivitet> aktiviteter = null;

    private Kode periodeResultatType = null;
    protected String begrunnelse = null;
    private Kode periodeResultatÅrsak = null;
    protected Kode manuellBehandlingÅrsak = null;
    private Kode graderingAvslagÅrsak = null;
    protected Boolean flerbarnsdager = null;
    protected Boolean samtidigUttak;
    private Boolean graderingInnvilget;
    protected Kode periodeType = null;
    private Kode utsettelseType = null;

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

    public Object getGradertArbeidsprosent() {
        return gradertAktivitet.prosentArbeid.longValue();
    }
    

}
