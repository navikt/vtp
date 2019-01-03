package no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.dto.beregning;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AktivitetsAvtaleDto {
    protected String id;
    protected LocalDate opphørArbeidsforhold;
    protected LocalDate oppstartArbeidsforhold;
    protected Double arbeidsprosent;

    public LocalDate getOpphørArbeidsforhold() {
        return opphørArbeidsforhold;
    }
    public LocalDate getOppstartArbeidsforhold() {
        return oppstartArbeidsforhold;
    }
    public Double getArbeidsprosent() {
        return arbeidsprosent;
    }
//    protected String yrkestittel; #TODO
//    protected List<LocalDate> permisjoner; #TODO
//    protected LocalDate sisteLønnsendring; #TODO
    
    public AktivitetsAvtaleDto() {
    }
}
