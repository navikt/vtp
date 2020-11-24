package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.omsorgspenger;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Kilde {
    @JsonProperty("id")
    private String id;

    @JsonProperty("type")
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
