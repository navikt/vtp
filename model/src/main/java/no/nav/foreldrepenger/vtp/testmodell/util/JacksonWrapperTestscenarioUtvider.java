package no.nav.foreldrepenger.vtp.testmodell.util;

import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;

/** NB - Single-use only. Variable lest/skrevet caches internt i modul. Variable som brukes vil deles på tvers av invokeringer.*/
public class JacksonWrapperTestscenarioUtvider extends JacksonWrapperTestscenario {

    private final VariabelContainer vars;
    private final InjectableValues.Std injectableValues = new InjectableValues.Std();

    public JacksonWrapperTestscenarioUtvider(VariabelContainer vars) {
        Objects.requireNonNull(vars, "vars");
        this.vars = vars;
    }

    public ObjectMapper lagCopyAvObjectMapperOgUtvideMedVars() {
        ObjectMapper objectMapper = lagCopyAvObjectMapper();
        objectMapper.registerModule(new DeserializerModule(vars));
        addInjectable(VariabelContainer.class, vars);
        objectMapper.setInjectableValues(injectableValues);
        return objectMapper;
    }

    public VariabelContainer getVars() {
        return vars;
    }

    public void addVars(VariabelContainer vars) {
        this.vars.putAll(vars);
    }

    public void addVars(Map<String, String> vars2) {
        this.vars.putAll(vars2);
    }

    public void addInjectable(Class<?> type, Object val) {
        injectableValues.addValue(type, val);
    }
}
