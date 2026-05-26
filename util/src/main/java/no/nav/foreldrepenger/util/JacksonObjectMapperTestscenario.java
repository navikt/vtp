package no.nav.foreldrepenger.util;

import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;

import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.cfg.EnumFeature;
import tools.jackson.databind.json.JsonMapper;

public class JacksonObjectMapperTestscenario {

    private JacksonObjectMapperTestscenario() {
    }

    private static final JsonMapper MAPPER = createJsonMapper();

    private static JsonMapper createJsonMapper() {
        return JsonMapper.builder()
                .defaultTimeZone(TimeZone.getTimeZone("Europe/Oslo"))
                .disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES) // Var noen tester med null for booleans
                .enable(EnumFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE)
                .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES) // TODO: Trengs denne? Sak har kjørt lenge uten
                .changeDefaultPropertyInclusion(a -> a
                        .withValueInclusion(JsonInclude.Include.NON_NULL)
                        .withContentInclusion(JsonInclude.Include.NON_NULL))
                .changeDefaultVisibility(v -> v
                        .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                        .withIsGetterVisibility(JsonAutoDetect.Visibility.NONE)
                        .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                        .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                        .withCreatorVisibility(JsonAutoDetect.Visibility.ANY)
                        .withScalarConstructorVisibility(JsonAutoDetect.Visibility.ANY))
                .build();
    }

    public static JsonMapper getJsonMapper() {
        return MAPPER;
    }

    public static String writeValueAsString(Object object) {
        return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    }
}
