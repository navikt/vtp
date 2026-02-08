package no.nav.foreldrepenger.vtp.testmodell.util;

import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JacksonObjectMapperTestscenario {

    private static final JsonMapper MAPPER = createJsonMapper();

    private static JsonMapper createJsonMapper() {
        return JsonMapper.builder()
                .addModule(new Jdk8Module())
                .addModule(new JavaTimeModule())
                .defaultTimeZone(TimeZone.getTimeZone("Europe/Oslo"))
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE)
                .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
                .defaultPropertyInclusion(JsonInclude.Value.construct(JsonInclude.Include.NON_NULL, JsonInclude.Include.NON_NULL))
                .visibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE)
                .visibility(PropertyAccessor.SETTER, JsonAutoDetect.Visibility.NONE)
                .visibility(PropertyAccessor.IS_GETTER, JsonAutoDetect.Visibility.NONE)
                .visibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
                .visibility(PropertyAccessor.CREATOR, JsonAutoDetect.Visibility.ANY)
                .build();
    }

    public static JsonMapper getJsonMapper() {
        return MAPPER;
    }

    public static String writeValueAsString(Object object) {
        try {
            return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Kunne ikke serialisere fra " + e);
        }
    }
}
