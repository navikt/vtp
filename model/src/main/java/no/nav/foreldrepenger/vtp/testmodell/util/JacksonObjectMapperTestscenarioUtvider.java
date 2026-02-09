package no.nav.foreldrepenger.vtp.testmodell.util;

import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.json.JsonMapper;

/** NB - Single-use only. Variable lest/skrevet caches internt i modul. Variable som brukes vil deles på tvers av invokeringer.*/
public class JacksonObjectMapperTestscenarioUtvider extends JacksonObjectMapperTestscenario {

    private final VariabelContainer vars;
    private final InjectableValues.Std injectableValues = new InjectableValues.Std();

    public JacksonObjectMapperTestscenarioUtvider(VariabelContainer vars) {
        Objects.requireNonNull(vars, "vars");
        this.vars = vars;
    }

    public JsonMapper lagCopyAvObjectMapperOgUtvideMedVars() {
        addInjectable(VariabelContainer.class, vars);
        return JacksonObjectMapperTestscenario.getJsonMapper().rebuild()
                .addModule(new DeserializerModule(vars))
                .injectableValues(injectableValues)
                .build();
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
