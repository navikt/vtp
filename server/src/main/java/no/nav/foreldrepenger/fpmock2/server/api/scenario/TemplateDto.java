package no.nav.foreldrepenger.fpmock2.server.api.scenario;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Beskriver en template,inklusiv liste av variable og deres verdier. */
public class TemplateDto extends TemplateReferanse {

    @JsonInclude(content = Include.NON_EMPTY)
    @JsonProperty("variabler")
    private Map<String, String> variabler = new HashMap<>();

    public TemplateDto(String key, String navn, Map<String, String> vars) {
        super(key, navn);
        if (vars != null) {
            this.variabler.putAll(vars);
        }
    }

    public Map<String, String> getVariabler() {
        return Collections.unmodifiableMap(variabler);
    }
}
