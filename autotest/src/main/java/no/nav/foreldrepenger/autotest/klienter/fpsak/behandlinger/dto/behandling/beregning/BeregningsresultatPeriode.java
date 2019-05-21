package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BeregningsresultatPeriode {

    protected LocalDate fom;
    protected LocalDate tom;
    protected int dagsats;
    protected BeregningsresultatPeriodeAndel[] andeler;

    public LocalDate getFom() {
        return fom;
    }

    public LocalDate getTom() {
        return tom;
    }

    public int getDagsats() {
        return dagsats;
    }

    public BeregningsresultatPeriodeAndel[] getAndeler() {
        return andeler;
    }

}
