package no.nav.foreldrepenger.fpmock2.server.api.scenario;

import com.fasterxml.jackson.annotation.JsonProperty;

/** Beskriver en template,inklusiv liste av variable og deres verdier. */
public class TemplateReferanse {

    @JsonProperty("key")
    private String key;

    @JsonProperty("navn")
    private String navn;

    public TemplateReferanse(String key, String navn) {
        this.key = key;
        this.navn = navn;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

}
