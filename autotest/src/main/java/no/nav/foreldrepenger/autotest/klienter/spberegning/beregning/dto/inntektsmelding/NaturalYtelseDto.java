package no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.dto.inntektsmelding;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NaturalYtelseDto {

    protected String kode;
    protected Double beløpPerMnd;
    protected LocalDate tom;
    
    public NaturalYtelseDto() {
    }
}
