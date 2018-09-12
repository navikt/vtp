package no.nav.foreldrepenger.fpmock2.testmodell.util;

import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;

import no.nav.foreldrepenger.fpmock2.testmodell.repo.TemplateVariable;

/** Scanner input for template variable og finner type, path etc. */
public class FindTemplateVariables {

    static final Pattern TEMPLATE_VARIABLE_PATTERN = Pattern.compile("\\$\\{(.+)\\}");

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final FindTemplateVariableModule module = new FindTemplateVariableModule();

    public FindTemplateVariables() {
        objectMapper.setVisibility(PropertyAccessor.GETTER, Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.SETTER, Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.CREATOR, Visibility.ANY);

        objectMapper.registerModule(module);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        objectMapper.setInjectableValues(new InjectableValues.Std() {
            @Override
            public Object findInjectableValue(Object valueId, DeserializationContext ctxt, BeanProperty forProperty, Object beanInstance)
                    throws JsonMappingException {
                // skipper all injection uten å feile
                return null;
            }
        });
    }

    public Set<TemplateVariable> getDiscoveredVariables() {
        return module.getVars();
    }

    public void scanForVariables(Class<?> targetClass, Reader reader) {
        if(reader==null) {
            return;
        }
        try {
            // ignore return, fanger opp variabler i #module ved deserialisering
            objectMapper.readValue(reader, targetClass);
        } catch (IOException e) {
            throw new IllegalArgumentException("Kunne ikke deserialisere til " + targetClass.getName(), e);
        }
    }

    static class FindTemplateVariableModule extends SimpleModule {
        private final Set<TemplateVariable> vars = new HashSet<>();

        private FindTemplateVariableModule() {
            this.setDeserializerModifier(new BeanDeserializerModifier() {
                @Override
                public JsonDeserializer<?> modifyDeserializer(DeserializationConfig config, BeanDescription beanDesc, JsonDeserializer<?> deserializer) {
                    return new FindTemplateVariableDeserializer(super.modifyDeserializer(config, beanDesc, deserializer), vars);
                }
            });
        }

        public Set<TemplateVariable> getVars() {
            return Collections.unmodifiableSet(vars);
        }

    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    static class FindTemplateVariableDeserializer extends JsonDeserializer implements ContextualDeserializer, ResolvableDeserializer {

        private final JsonDeserializer<?> delegate;
        private final Set<TemplateVariable> vars;

        public FindTemplateVariableDeserializer(JsonDeserializer<?> delegate, Set<TemplateVariable> vars) {
            this.delegate = delegate;
            this.vars = vars;
        }

        /** glue-code. */
        @Override
        public void resolve(DeserializationContext ctxt) throws JsonMappingException {
            if (delegate instanceof ResolvableDeserializer) {
                ((ResolvableDeserializer) delegate).resolve(ctxt);
            }
        }

        /** glue-code. */
        @Override
        public JsonDeserializer createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
            JsonDeserializer delSer = delegate;
            if (delSer instanceof ContextualDeserializer) {
                delSer = ((ContextualDeserializer) delegate).createContextual(ctxt, property);
            }
            if (delSer == delegate) {
                return this;
            }
            return new FindTemplateVariableDeserializer(delSer, this.vars);
        }

        /** sjekk om inneholder variabel referanse og ta vare på det. */
        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (!p.currentToken().isScalarValue()) {
                return delegate.deserialize(p, ctxt);
            }
            String text = p.getText();
            Matcher matcher = TEMPLATE_VARIABLE_PATTERN.matcher(text);
            if (matcher.find()) {
                // antar p.t. kun en variabel per node
                String varName = matcher.group(1);
                JsonStreamContext pc = p.getParsingContext();
                String path = getPath(pc);
                Class<?> targetClass = delegate.handledType();

                vars.add(new TemplateVariable(targetClass, varName, path, null));
            }
            // returnerer null slik at dette ikke går i beina på senere parsing
            return null;
        }

    }

    static String getPath(JsonStreamContext streamContext) {
        JsonStreamContext pc = streamContext;
        StringBuilder sb = new StringBuilder(pc.getCurrentName());
        pc = pc.getParent();
        while (pc != null && pc.getCurrentName() != null) {
            sb.insert(0, pc.getCurrentName() + ".");
            pc = pc.getParent();
        }
        return sb.toString();
    }
}
