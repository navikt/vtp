package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AsyncPollingStatus {

    protected String status;
    protected String eta;
    protected String message;
    protected Integer pollIntervalMillis;
    protected String location;
    protected String cancelUri;
    protected Boolean readOnly;
    protected Boolean pending;
    
    public boolean isPending() {
        return pending.booleanValue();
    }
    
    public String getMessage() {
        return message;
    }
}
