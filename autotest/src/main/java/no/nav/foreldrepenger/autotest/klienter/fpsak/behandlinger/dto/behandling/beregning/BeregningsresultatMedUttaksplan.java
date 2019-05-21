package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BeregningsresultatMedUttaksplan {

    protected boolean sokerErMor;
    protected LocalDate opphoersdato;
    protected BeregningsresultatPeriode[] perioder;

    public boolean isSokerErMor() {
        return sokerErMor;
    }

    public LocalDate getOpphoersdato() {
        return opphoersdato;
    }

    public BeregningsresultatPeriode[] getPerioder() {
        return perioder;
    }

}
