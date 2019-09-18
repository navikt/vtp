package no.nav.foreldrepenger.vtp.server.api.kafka;

import com.fasterxml.jackson.annotation.JsonProperty;


public class KafkaTopicDto {

    @JsonProperty("name")
    private String name;

    @JsonProperty("internal")
    private Boolean internal;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getInternal() {
        return internal;
    }

    public void setInternal(Boolean internal) {
        this.internal = internal;
    }

}
