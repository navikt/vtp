package no.nav.foreldrepenger.fpmock2.testmodell.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;

class StringVarSerializer extends JsonSerializer<String> implements ContextualSerializer, ResolvableSerializer {

    private static final List<String> VAR_PARAM_NAMES = Arrays.asList(
        "orgnr",
        "saksnummer" /* arenasak */,
        "sakId" /* infotrygdsak */);

    private final NavigableMap<String, List<String>> valueToKey;

    private JsonSerializer<?> delegate;

    public StringVarSerializer(JsonSerializer<?> delegate, NavigableMap<String, List<String>> valueToKey) {
        this.delegate = delegate;
        this.valueToKey = valueToKey;
    }

    @Override
    public void resolve(SerializerProvider provider) throws JsonMappingException {
        if (delegate instanceof ResolvableSerializer) {
            ((ResolvableSerializer) delegate).resolve(provider);
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        JsonSerializer<?> ser = delegate;
        if (ser instanceof ContextualSerializer) {
            ser = ((ContextualSerializer) delegate).createContextual(prov, property);
        }
        if (ser == delegate) {
            return this;
        }
        return new StringVarSerializer(ser, valueToKey);
    }

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        String currentName = gen.getOutputContext().getCurrentName();

        if ("ident".equals(currentName)) {
            String type = gen.getOutputContext().getParent().getCurrentValue().getClass().getAnnotation(JsonTypeName.class).value().replaceAll("@", "");
            // fant en ident, ta vare på verdi og skriv ut variabel.
            valueToKey.computeIfAbsent(type, k -> new ArrayList<>()).add(value); // blir duplikater her, men finner alltid første

            // sjekk om nøkkel er registrert som noe annet
            Optional<Entry<String, List<String>>> realKey = valueToKey.entrySet().stream().filter(e -> !"ident".equals(e.getKey()))
                .filter(e -> e.getValue().contains(value)).findFirst();
            if (realKey.isPresent()) {
                int pos = realKey.get().getValue().indexOf(value);
                gen.writeString("${" + realKey.get().getKey() + (pos + 1) + "}"); // skriver tilbake key
            } else {
                int pos = valueToKey.get(type).indexOf(value);
                String key = createKey(type, pos);
                gen.writeString("${" + key + "}"); // skriver tilbake key
            }
        } else {
            if (VAR_PARAM_NAMES.contains(currentName)) {
                String type = currentName;
                valueToKey.computeIfAbsent(type, k -> new ArrayList<>()).add(value); // blir duplikater her, men finner alltid første

                // sjekk om nøkkel er registrert som noe annet
                Optional<Entry<String, List<String>>> realKey = valueToKey.entrySet().stream().filter(e -> !"ident".equals(e.getKey()))
                    .filter(e -> e.getValue().contains(value)).findFirst();
                if (realKey.isPresent()) {
                    int pos = realKey.get().getValue().indexOf(value);
                    gen.writeString("${" + createKey(realKey.get().getKey(), pos) + "}"); // skriver tilbake key
                } else {
                    int pos = valueToKey.get(type).indexOf(value);
                    gen.writeString("${" + type + (pos + 1) + "}"); // skriver tilbake key
                }

            } else {
                gen.writeString(value);
            }
        }
    }

    public static String createKey(String type, int pos) {
        return type + (pos + 1);
    }

}
