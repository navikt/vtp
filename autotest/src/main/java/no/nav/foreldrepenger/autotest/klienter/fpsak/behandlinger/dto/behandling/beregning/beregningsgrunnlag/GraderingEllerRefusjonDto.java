package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.beregningsgrunnlag;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GraderingEllerRefusjonDto {

    protected boolean erRefusjon;
    protected boolean erGradering;
    protected LocalDate fom;
    protected LocalDate tom;

    public boolean isErRefusjon() {
        return erRefusjon;
    }

    public boolean isErGradering() {
        return erGradering;
    }

    public LocalDate getFom() {
        return fom;
    }

    public LocalDate getTom() {
        return tom;
    }
}
