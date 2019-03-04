package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AsyncPollingStatus {

    protected Status status;
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

    public Integer getStatus() {
        return status.getHttpStatus();
    }

    public String getMessage() {
        return message;
    }


    public enum Status {
        PENDING(200),
        COMPLETE(303),
        DELAYED(418),
        CANCELLED(418),
        HALTED(418);

        private int httpStatus;

        Status(int httpStatus){
            this.httpStatus = httpStatus;
        }

        public int getHttpStatus() {
            return httpStatus;
        }
    }
}
