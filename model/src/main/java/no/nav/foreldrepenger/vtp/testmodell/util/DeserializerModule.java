package no.nav.foreldrepenger.vtp.testmodell.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.threeten.extra.PeriodDuration;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeserializerModule extends SimpleModule {

    private final VariabelContainer vars;

    public DeserializerModule(VariabelContainer vars) {
        super("VTP-DESERIALIZER", new Version(1, 0, 0, null, null, null));
        this.vars = vars;

        this.addDeserializer(LocalDate.class, new LocalDateDeserializer());
        this.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());

        this.setDeserializerModifier(new BeanDeserializerModifier() {
            @Override
            public JsonDeserializer<?> modifyDeserializer(DeserializationConfig config, BeanDescription beanDesc, JsonDeserializer<?> deserializer) {
                if (String.class.isAssignableFrom(beanDesc.getBeanClass())) {
                    return new StringVarDeserializer(super.modifyDeserializer(config, beanDesc, deserializer), vars);
                } else {
                    return super.modifyDeserializer(config, beanDesc, deserializer);
                }
            }
        });
    }

    private LocalDateTime initTimeVar(String basedt) {
        return LocalDateTime.parse(vars.computeIfAbsent(basedt, n -> LocalDateTime.now().toString()));
    }

    class LocalDateDeserializer extends StdScalarDeserializer<LocalDate> {

        protected LocalDateDeserializer() {
            super(LocalDate.class);
        }

        @Override
        public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            String reformatted = p.getText();
            for (Map.Entry<String, String> v : vars.getVars().entrySet()) {
                reformatted = reformatted.replace("${" + v.getKey() + "}", v.getValue());
            }

            Pattern testRegexp = Pattern.compile("^(now|basedate)\\(\\)\\s*(([+-])\\s*(P[0-9TYMWDHS].*))*$");
            Matcher matcher = testRegexp.matcher(reformatted);
            if (matcher.matches()) {
                String baseref = matcher.group(1);
                LocalDate base = initTimeVar(baseref).toLocalDate();
                if (matcher.group(2) != null && matcher.group(3) != null) {
                    String[] split = matcher.group(2).split("(?<=(\\w(?!\\w)|\\+|-(?!=)))\\s*");
                    for (int i = 0; i < split.length; i += 2) {
                        Period period = Period.parse(split[i+1]);
                        if ("-".equals(split[i])) {
                            base = base.minus(period);
                        } else {
                            base = base.plus(period);
                        }
                    }
                    return base;
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
            String reformatted = p.getText();
            for (Map.Entry<String, String> v : vars.getVars().entrySet()) {
                reformatted = reformatted.replace("${" + v.getKey() + "}", v.getValue());
            }
            Pattern testRegexp = Pattern.compile("^(now|basedate)\\(\\)\\s*(([+-])\\s*(P[0-9TYMWDHS].*))*$");
            Matcher matcher = testRegexp.matcher(reformatted);
            if (matcher.matches()) {
                String baseref = matcher.group(1);
                LocalDateTime base = initTimeVar(baseref);
                if (matcher.group(2) != null && matcher.group(3) != null) {
                    String[] split = matcher.group(2).split("(?<=(\\w(?!\\w)|\\+|-(?!=)))\\s*");
                    for (int i = 0; i < split.length; i += 2) {
                        PeriodDuration period = PeriodDuration.parse(split[i+1]);
                        if ("-".equals(split[i])) {
                            base = base.minus(period);
                        } else {
                            base = base.plus(period);
                        }
                    }
                    return base;
                } else {
                    return base;
                }
            } else {
                return LocalDateTime.parse(reformatted);
            }

        }
    }
}
