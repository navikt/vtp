package no.nav.foreldrepenger.fpmock2.testmodell.util;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;

public class StringVarDeserializer extends JsonDeserializer<String> implements ContextualDeserializer, ResolvableDeserializer {
    private final JsonDeserializer<?> delegate;
    private final VariabelContainer vars;

    public StringVarDeserializer(JsonDeserializer<?> delegate, VariabelContainer vars) {
        this.delegate = delegate;
        this.vars = vars;
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
        return new StringVarDeserializer(delSer, this.vars);
    }

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String text = p.getText();

        Matcher matcher = FindTemplateVariables.TEMPLATE_VARIABLE_PATTERN.matcher(text);
        if (!matcher.matches()) {
            return text;
        }

        String reformatted = text;

        for (Map.Entry<String, String> v : vars.getVars().entrySet()) {
            reformatted = reformatted.replace("${" + v.getKey() + "}", (v.getValue() == null ? "" : v.getValue()));
        }
        if (Objects.equals(reformatted, text)) {
            // har ikke funnet deklarasjon for påkrevd variabel.
            vars.computeIfAbsent(matcher.group(1), n -> null);
//            throw new IllegalStateException("Mangler variabel deklarasjon for [" + text + "], path=" + FindTemplateVariables.getPath(p.getParsingContext()));
        }

        return reformatted;

    }

}
