package no.nav.foreldrepenger.fpmock2.testmodell.util;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;


/**
 * @threadsafety Ikke trådsafe eller gjenbrukbar.
 */
public class CanonicalSerializerModule extends SimpleModule {

    private final NavigableMap<String, List<String>> valueToKey = new TreeMap<>();
    
    public CanonicalSerializerModule() {
        super("FPMOCK2-CANONICAL-SERIALIZER", new Version(1, 0, 0, null, null, null));

        this.addSerializer(new LocalDateSerializer());
        this.addSerializer(new LocalDateTimeSerializer());
        this.setSerializerModifier(new BeanSerializerModifier() {
            @Override
            public JsonSerializer<?> modifySerializer(SerializationConfig config, BeanDescription beanDesc, JsonSerializer<?> serializer) {

                if (String.class.isAssignableFrom(beanDesc.getBeanClass())) {
                    StringVarSerializer varSerializer = new StringVarSerializer(super.modifySerializer(config, beanDesc, serializer), valueToKey);
                    return varSerializer;
                } else {
                    return super.modifySerializer(config, beanDesc, serializer);
                }

            }
        });
    }

    public Map<String, String> getNewVars() {
        Map<String, String> vars = new TreeMap<>();
        for(Map.Entry<String, List<String>> entry : valueToKey.entrySet()) {
            List<String> vals = new ArrayList<>(new LinkedHashSet<>(entry.getValue()));
            for(int i = 0;i<vals.size();i++) {
                vars.put(StringVarSerializer.createKey(entry.getKey(), i), vals.get(i));
            }
        }
        return Collections.unmodifiableMap(vars);
    }
    
    private class LocalDateSerializer extends StdScalarSerializer<LocalDate> {

        public LocalDateSerializer() {
            super(LocalDate.class);
        }

        @Override
        public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider provider)
                throws java.io.IOException {

            if (value == null) {
                gen.writeString((String) null);
                return;
            }
            LocalDate now = LocalDate.now();
            if (now.isEqual(value)) {
                gen.writeString("now()");
            } else if (value.isBefore(now)) {
                gen.writeString("now() - " + Period.between(value, now));
            } else {
                gen.writeString("now() + " + Period.between(now, value));
            }

        };

    }

    private class LocalDateTimeSerializer extends StdScalarSerializer<LocalDateTime> {

        public LocalDateTimeSerializer() {
            super(LocalDateTime.class);
        }

        @Override
        public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider provider)
                throws java.io.IOException {

            if (value == null) {
                gen.writeString((String) null);
                return;
            }
            value = canonicalTime(value);
            LocalDateTime now = canonicalTime(LocalDateTime.now());

            if (now.isEqual(value)) {
                gen.writeString("now()");
            } else if (value.isBefore(now)) {
                gen.writeString("now() - " + Duration.between(value, now));
            } else {
                gen.writeString("now() + " + Duration.between(now, value));
            }

        }

        private LocalDateTime canonicalTime(LocalDateTime value) {
            return value.withNano(0).withSecond(0).withMinute(0);
        };

    }
}
