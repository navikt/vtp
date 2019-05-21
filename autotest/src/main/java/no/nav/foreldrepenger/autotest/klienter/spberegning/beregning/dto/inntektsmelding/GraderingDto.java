package no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.dto.inntektsmelding;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GraderingDto {

    protected LocalDate fom;
    protected LocalDate tom;
    protected Double prosent;
    
    public GraderingDto() {
    }
}
