package no.nav.foreldrepenger.fpmock2.testmodell.util;

import java.util.Collections;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.regex.Matcher;

public class VariabelContainer {

    private NavigableMap<String, String> vars = new TreeMap<>();

    public VariabelContainer() {
    }
    
    public VariabelContainer(Map<String, String> variable) {
        this.putAll(variable);
    }

    public void putVar(String key, String value) {
        vars.put(cleanKey(key), value);
    }

    public String getVar(String key) {
        return vars.get(key);
    }

    public Map<String, String> getVars() {
        return Collections.unmodifiableMap(vars);
    }

    public void putAll(VariabelContainer vars2) {
        putAll(vars2.getVars());
    }

    public void putAll(Map<String, String> vars2) {
        vars2.entrySet().forEach(e -> this.vars.put(cleanKey(e.getKey()), e.getValue()));
    }

    public void forEach(BiConsumer<String, String> action) {
        this.vars.forEach(action);
    }

    public String computeIfAbsent(String key, Function<String, String> mappingFunction) {
        String cleanKey = cleanKey(key);
        return this.vars.computeIfAbsent(cleanKey, mappingFunction);
    }
    
    private static String cleanKey(String dirtyKey) {
        if(dirtyKey==null) {
            return null;
        }
        Matcher matcher = FindTemplateVariables.TEMPLATE_VARIABLE_PATTERN.matcher(dirtyKey);
        return matcher.find() ? matcher.group(1) : dirtyKey;
    }

}
