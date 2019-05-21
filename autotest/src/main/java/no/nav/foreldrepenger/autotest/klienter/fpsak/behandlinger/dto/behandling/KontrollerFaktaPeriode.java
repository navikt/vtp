package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.uttak.Arbeidsgiver;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.uttak.UttakDokumentasjon;
import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KontrollerFaktaPeriode {

    protected LocalDate tom;
    protected LocalDate fom;
    protected Kode uttakPeriodeType;
    protected Kode utsettelseÅrsak;
    protected Kode overføringÅrsak;
    protected Kode oppholdÅrsak;
    protected Kode resultat;
    protected List<UttakDokumentasjon> dokumentertePerioder;
    protected BigDecimal arbeidstidsprosent;
    protected String begrunnelse;
    protected Boolean bekreftet;
    protected Arbeidsgiver arbeidsgiver;
    protected Boolean erArbeidstaker;
    protected boolean erFrilanser;
    protected boolean erSelvstendig;
    protected boolean samtidigUttak;

    public LocalDate getTom() {
        return tom;
    }
    public void setTom(LocalDate tom) {
        this.tom = tom;
    }
    public BigDecimal getArbeidstidsprosent() {
        return arbeidstidsprosent;
    }
    public void setArbeidstidsprosent(BigDecimal arbeidstidsprosent) {
        this.arbeidstidsprosent = arbeidstidsprosent;
    }
    public String getBegrunnelse() {
        return begrunnelse;
    }
    public void setBegrunnelse(String begrunnelse) {
        this.begrunnelse = begrunnelse;
    }
    public Boolean getBekreftet() {
        return bekreftet;
    }
    public void setBekreftet(Boolean bekreftet) {
        this.bekreftet = bekreftet;
    }
    public Kode getResultat() {
        return resultat;
    }
    public void setResultat(Kode resultat) {
        this.resultat = resultat;
    }
    public List<UttakDokumentasjon> getDokumentertePerioder() {
        return dokumentertePerioder;
    }
    public void setDokumentertePerioder(List<UttakDokumentasjon> dokumentertePerioder) {
        this.dokumentertePerioder = dokumentertePerioder;
    }
    public LocalDate getFom() {
        return fom;
    }
    public void setFom(LocalDate fom) {
        this.fom = fom;
    }
}
