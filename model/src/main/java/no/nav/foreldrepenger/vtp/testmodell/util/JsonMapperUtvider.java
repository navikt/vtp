package no.nav.foreldrepenger.vtp.testmodell.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;

/** NB - Single-use only. Variable lest/skrevet caches internt i modul. Variable som brukes vil deles på tvers av invokeringer.*/
public class JsonMapperUtvider extends JsonMapper {

    private final VariabelContainer vars;
    private final InjectableValues.Std injectableValues = new InjectableValues.Std();

    public JsonMapperUtvider(VariabelContainer vars) {
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

    public Set<String> findVariables(Reader... jsonInput) {

        Set<String> vars = new TreeSet<>();
        Pattern varPattern = Pattern.compile("\\$\\{(.+)\\}");
        for (Reader r : jsonInput) {
            try (BufferedReader buffer = new BufferedReader(r)) {
                String text = buffer.lines().collect(Collectors.joining(System.lineSeparator()));
                Matcher m = varPattern.matcher(text);
                while (m.find()) {
                    String varName = m.group(1);
                    vars.add(varName);
                }
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
        return Collections.unmodifiableSet(vars);

    }
}
