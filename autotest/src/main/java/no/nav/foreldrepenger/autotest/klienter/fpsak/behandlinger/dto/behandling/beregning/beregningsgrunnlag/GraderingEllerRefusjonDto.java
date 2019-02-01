package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.beregningsgrunnlag;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GraderingEllerRefusjonDto {

    protected boolean erRefusjon;
    protected boolean erGradering;
    protected LocalDate fom;
    protected LocalDate tom;
    
}
