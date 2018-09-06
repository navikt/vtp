package no.nav.foreldrepenger.fpmock2.server.api.scenario;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TemplateBeskrivelse {

    @JsonProperty("key")
    private String key;

    @JsonProperty("navn")
    private String navn;

    @JsonProperty("beskrivelse")
    private String beskrivelse;

    @JsonInclude(content = Include.NON_EMPTY)
    private Map<String, String> defaultVars = new HashMap<>();

    public TemplateBeskrivelse(String key, String navn, Map<String, String> defaultVars) {
        this(key, navn, null, defaultVars);
    }

    public TemplateBeskrivelse(String key, String navn) {
        this(key, navn, null, Collections.emptyMap());
    }

    public TemplateBeskrivelse(String key, String navn, String beskrivelse, Map<String, String> defaultVars) {
        this.key = key;
        this.navn = navn;
        this.beskrivelse = beskrivelse;
        if (defaultVars != null) {
            this.defaultVars.putAll(defaultVars);
        }
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

    public String getBeskrivelse() {
        return beskrivelse;
    }

    public void setBeskrivelse(String beskrivelse) {
        this.beskrivelse = beskrivelse;
    }

    public Map<String, String> getDefaultVars() {
        return Collections.unmodifiableMap(defaultVars);
    }
}
