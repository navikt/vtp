package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.beregningsgrunnlag;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AktivitetTomDatoMappingDto {

    protected LocalDate tom;
    protected List<BeregningAktivitetDto> aktiviteter;

    public LocalDate getTom() {
        return tom;
    }

    public List<BeregningAktivitetDto> getAktiviteter() {
        return aktiviteter;
    }
}
