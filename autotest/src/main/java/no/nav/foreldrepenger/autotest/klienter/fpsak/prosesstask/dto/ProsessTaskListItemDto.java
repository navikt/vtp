package no.nav.foreldrepenger.autotest.klienter.fpsak.prosesstask.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProsessTaskListItemDto {

    protected int id;
    protected String taskType;
    protected String nesteKjøringEtter; 
    protected String gruppe;
    protected String sekvens;
    protected String status;
    protected String sistKjørt;
    protected String sisteFeilKode;
    protected TaskParametereDto taskParametre;
    
    public int getId() {
        return id;
    }
    
    public String getStatus() {
        return status;
    }
    
    public String getTaskType() {
        return taskType;
    }
    public TaskParametereDto getTaskParametre() {
        return taskParametre;
    }
}
