package no.nav.foreldrepenger.fpmock2.testmodell.util;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

public class JsonMapper {

    private final ObjectMapper objectMapper;

    private InjectableValues.Std injectableValues = new InjectableValues.Std();

    private final Map<String, String> vars = new HashMap<>();

    public JsonMapper() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(new ParameterNamesModule());
        objectMapper.registerModule(new DeserializerModule(vars));

        objectMapper.setVisibility(PropertyAccessor.GETTER, Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.SETTER, Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.NONE);

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setVisibility(PropertyAccessor.CREATOR, Visibility.ANY);

        objectMapper.setSerializationInclusion(Include.NON_NULL);

        objectMapper.setInjectableValues(injectableValues);

    }

    public void addVars(Map<String, String> vars) {
        this.vars.putAll(vars);
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public ObjectMapper canonicalObjectMapper() {
        ObjectMapper canonicalObjectMapper = objectMapper.copy();
        canonicalObjectMapper.registerModule(new CanonicalSerializerModule());
        return canonicalObjectMapper;
    }

    public void addInjectable(Class<?> type, Object val) {
        injectableValues.addValue(type, val);
    }

}
