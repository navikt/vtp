package no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ForeslaaDto {
    protected String tema;
    protected int aktorId;
    protected String gosysSakId;
    protected Integer oppgaveId;
    
    
    public ForeslaaDto(String tema, int aktorId, String gosysSakId) {
        super();
        this.tema = tema;
        this.aktorId = aktorId;
        this.gosysSakId = gosysSakId;
    }
}
