package no.nav.foreldrepenger.autotest.klienter.fpsak.prosesstask.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProsesstaskDto {

    protected int prosessTaskId;
    protected String naaVaaerendeStatus;
    
    public ProsesstaskDto(int prosessTaskId, String naaVaaerendeStatus) {
        super();
        this.prosessTaskId = prosessTaskId;
        this.naaVaaerendeStatus = naaVaaerendeStatus;
    }
}
