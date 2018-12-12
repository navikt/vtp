package no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.dto.beregning;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AktivitetsAvtaleDto {
    protected String id;
    protected LocalDate opphørArbeidsforhold;
    protected LocalDate oppstartArbeidsforhold;
    protected Double arbeidsprosent;
    
    public AktivitetsAvtaleDto() {
    }
}
