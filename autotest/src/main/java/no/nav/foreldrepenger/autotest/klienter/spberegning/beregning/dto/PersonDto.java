package no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.dto;

import java.time.LocalDate;

public class PersonDto {
    protected String navn;
    protected Integer alder;
    protected String personnummer;
    protected Boolean erKvinne;
    protected String diskresjonskode;
    protected LocalDate dodsdato;
    
    public PersonDto() {
    }
    
    
}
