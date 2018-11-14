package no.nav.foreldrepenger.autotest.klienter.fpsak.prosesstask.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskParametereDto {
    protected String callId;
    protected String fagsakId;
    protected String behandlingId;
    protected String aktoerId;
    
    
    public String getCallId() {
        return callId;
    }
    public String getFagsakId() {
        return fagsakId;
    }
    public String getBehandlingId() {
        return behandlingId;
    }
    public String getAktoerId() {
        return aktoerId;
    }
}
