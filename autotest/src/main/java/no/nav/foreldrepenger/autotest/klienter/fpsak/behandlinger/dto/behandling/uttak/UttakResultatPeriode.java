package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.uttak;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UttakResultatPeriode {

    public LocalDate fom = null;
    public LocalDate tom = null;

    public List<UttakResultatPeriodeAktivitet> aktiviteter = null;

    public Kode periodeResultatType = null;
    public String begrunnelse = null;
    public Kode periodeResultatÅrsak = null;
    public Kode manuellBehandlingÅrsak = null;
    public Kode graderingAvslagÅrsak = null;
    public Boolean flerbarnsdager = null;
    public Boolean samtidigUttak;
    public Boolean graderingInnvilget;
    public Kode periodeType = null;
    public Kode utsettelseType = null;

    public UttakResultatPeriodeAktivitet gradertAktivitet = null;
    

}
