package no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ForeslaaDto {
    @JsonProperty("tema")
    protected String tema;

    @JsonProperty("aktorId")
    protected Long aktorId;

    @JsonProperty("gosysSakId")
    protected String gosysSakId;

    @JsonProperty("oppgaveId")
    protected Long oppgaveId;
    
    
    public ForeslaaDto(String tema, Long aktorId, String gosysSakId) {
        super();
        this.tema = tema;
        this.aktorId = aktorId;
        this.gosysSakId = gosysSakId;
    }
}