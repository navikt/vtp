package no.nav.foreldrepenger.fpmock2.testmodell.util;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

public class JsonMapper {

    private final SimpleModule serialDeserialModule = createModule();
    private final ObjectMapper objectMapper;
    private InjectableValues.Std injectableValues = new InjectableValues.Std();

    private Map<String, String> vars = new HashMap<>();

    public JsonMapper() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(new ParameterNamesModule());
        objectMapper.registerModule(serialDeserialModule);

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

    public void addInjectable(Class<?> type, Object val) {
        injectableValues.addValue(type, val);
    }

    private SimpleModule createModule() {
        SimpleModule module = new SimpleModule("FPMOCK2", new Version(1, 0, 0, null, null, null));
        module.addDeserializer(LocalDate.class, new LocalDateDeserializer());
        
        module.setDeserializerModifier(new BeanDeserializerModifier() {
            @Override
            public JsonDeserializer<?> modifyDeserializer(DeserializationConfig config, BeanDescription beanDesc, JsonDeserializer<?> deserializer) {
                if (String.class.isAssignableFrom(beanDesc.getBeanClass())) {
                    return new StringDeserializer(super.modifyDeserializer(config, beanDesc, deserializer));
                } else {
                    return super.modifyDeserializer(config, beanDesc, deserializer);
                }
            }
        });
        return module;
    }

    class StringDeserializer extends JsonDeserializer<String> implements ContextualDeserializer, ResolvableDeserializer {
        private final JsonDeserializer<?> delegate;

        public StringDeserializer(JsonDeserializer<?> delegate) {
            this.delegate = delegate;
        }

        @Override
        public void resolve(DeserializationContext ctxt) throws JsonMappingException {
            if (delegate instanceof ResolvableDeserializer) {
                ((ResolvableDeserializer) delegate).resolve(ctxt);
            }
        }

        @SuppressWarnings({ "rawtypes" })
        @Override
        public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
            JsonDeserializer delSer = delegate;
            if (delSer instanceof ContextualDeserializer) {
                delSer = ((ContextualDeserializer) delegate).createContextual(ctxt, property);
            }
            if (delSer == delegate) {
                return this;
            }
            return new StringDeserializer(delSer);
        }

        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            String text = p.getText();

            String reformatted = text;
            for (Map.Entry<String, String> v : vars.entrySet()) {
                reformatted = reformatted.replace("${" + v.getKey() + "}", v.getValue());
            }
            return reformatted;

        }
    }

    class LocalDateDeserializer extends StdScalarDeserializer<LocalDate> {

        protected LocalDateDeserializer() {
            super(LocalDate.class);
        }

        @Override
        public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            String text = p.getText();

            String reformatted = text;
            for (Map.Entry<String, String> v : vars.entrySet()) {
                reformatted = reformatted.replace("${" + v.getKey() + "}", v.getValue());
            }

            Pattern regex = Pattern.compile("^now\\(\\)\\s*([+-])\\s*(P[0-9TDMYT].*)$");
            Matcher matcher = regex.matcher(reformatted);
            if (matcher.matches()) {
                String op = matcher.group(1);
                Period period = Period.parse(matcher.group(2));
                if ("-".equals(op)) {
                    return LocalDate.now().minus(period);
                } else {
                    return LocalDate.now().plus(period);
                }
            } else {
                return LocalDate.parse(reformatted);
            }

        }
    }

}
