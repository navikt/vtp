package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.uttak;

import java.math.BigDecimal;

import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;

public class UttakResultatPeriodeAktivitet {

    public Kode stønadskontoType = null;
    public Integer trekkdager = null;
    public BigDecimal prosentArbeid = null;
    public String arbeidsforholdId = null;
    public String arbeidsforholdNavn = null;
    public String arbeidsforholdOrgnr = null;
    public BigDecimal utbetalingsgrad = null;
    public Kode uttakArbeidType = null;
}
