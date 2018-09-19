package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.uttak;

import java.time.LocalDate;
import java.util.List;

import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;

public class UttakResultatPeriode {

    public LocalDate fom = null;

    public LocalDate tom = null;

    public List<UttakResultatPeriodeAktivitet> aktiviteter = null;

    public Kode periodeResultatType = null;

    public String begrunnelse = null;

    public Kode periodeResultatÅrsak = null;

    public Kode manuellBehandlingÅrsak = null;

    public Boolean flerbarnsdager = null;

    public Kode periodeType = null;

    public Kode utsettelseType = null;

    public Object gradertAktivitet = null;

}
