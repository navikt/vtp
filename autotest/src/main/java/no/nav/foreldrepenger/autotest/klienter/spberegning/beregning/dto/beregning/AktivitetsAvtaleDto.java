package no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.dto.beregning;

import java.time.LocalDate;

public class AktivitetsAvtaleDto {
    String id;
    LocalDate opphørArbeidsforhold;
    LocalDate oppstartArbeidsforhold;
    Double arbeidsprosent;
    
    public AktivitetsAvtaleDto() {
    }
}
