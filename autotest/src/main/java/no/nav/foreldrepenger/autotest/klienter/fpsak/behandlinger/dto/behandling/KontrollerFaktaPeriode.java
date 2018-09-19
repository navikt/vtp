package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.uttak.UttakDokumentasjon;
import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;

public class KontrollerFaktaPeriode {

    public LocalDate tom = null;
    public LocalDate fom = null;

    public Kode uttakPeriodeType = null;
    public Kode utsettelseÅrsak = null;
    public Kode overføringÅrsak = null;
    public Kode resultat = null;
    public List<UttakDokumentasjon> dokumentertePerioder = new ArrayList<>();
    
    public BigDecimal arbeidstidsprosent = null;
    public String begrunnelse = null;
    public Boolean bekreftet = null;
    public String orgnr = null;
    public Boolean erArbeidstaker = null;
}
