package no.nav.foreldrepenger.fpmock2.testmodell.util;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.threeten.extra.PeriodDuration;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class DeserializerModule extends SimpleModule {

    private final Map<String, String> vars;

    private LocalDate baseLocalDate = LocalDate.now();
    private LocalDateTime baseLocalDateTime = LocalDateTime.now();

    public DeserializerModule(Map<String, String> vars) {
        super("FPMOCK2-DESERIALIZER", new Version(1, 0, 0, null, null, null));
        this.vars = vars;

        this.addDeserializer(LocalDate.class, new LocalDateDeserializer());
        this.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());

        this.setDeserializerModifier(new BeanDeserializerModifier() {
            @Override
            public JsonDeserializer<?> modifyDeserializer(DeserializationConfig config, BeanDescription beanDesc, JsonDeserializer<?> deserializer) {
                if (String.class.isAssignableFrom(beanDesc.getBeanClass())) {
                    return new StringDeserializer(super.modifyDeserializer(config, beanDesc, deserializer));
                } else {
                    return super.modifyDeserializer(config, beanDesc, deserializer);
                }
            }
        });
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

            Pattern testRegexp = Pattern.compile("^(now|basedate)\\(\\)\\s*(([+-])\\s*(P[0-9TYMWDHS].*))?$");
            Matcher matcher = testRegexp.matcher(reformatted);
            if (matcher.matches()) {
                LocalDate base = baseLocalDate;
                if("basedate".equals(matcher.group(2))) {
                     base = LocalDate.now();
                }
                String op = matcher.group(3);
                if (op != null) {
                    Period period = Period.parse(matcher.group(4));
                    if ("-".equals(op)) {
                        return base.minus(period);
                    } else {
                        return base.plus(period);
                    }
                } else {
                    return base;
                }
            } else {
                return LocalDate.parse(reformatted);
            }

        }
    }

    class LocalDateTimeDeserializer extends StdScalarDeserializer<LocalDateTime> {

        protected LocalDateTimeDeserializer() {
            super(LocalDateTime.class);
        }

        @Override
        public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            String text = p.getText();

            String reformatted = text;
            for (Map.Entry<String, String> v : vars.entrySet()) {
                reformatted = reformatted.replace("${" + v.getKey() + "}", v.getValue());
            }
            Pattern testRegexp = Pattern.compile("^(now|basedate)\\(\\)\\s*(([+-])\\s*(P[0-9TYMWDHS].*))?$");
            Matcher matcher = testRegexp.matcher(reformatted);
            if (matcher.matches()) {
                LocalDateTime base = baseLocalDateTime;
                if("basedate".equals(matcher.group(2))) {
                     base = LocalDateTime.now();
                }
                String op = matcher.group(3);
                if (op != null) {
                    String per = matcher.group(4);
                    PeriodDuration period = PeriodDuration.parse(per);
                    if ("-".equals(op)) {
                        return base.minus(period);
                    } else {
                        return base.plus(period);
                    }
                } else {
                    return base;
                }
            } else {
                return LocalDateTime.parse(reformatted);
            }

        }
    }
}
