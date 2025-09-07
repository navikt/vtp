package no.nav.pdl;

import java.util.Map;
import java.util.TimeZone;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kobylynskyi.graphql.codegen.model.graphql.GraphQLResult;

public abstract class PdlTestBase {

    protected static final ObjectMapper objectMapper = createObjectMapper();

    protected static ObjectMapper createObjectMapper() {
        return new ObjectMapper()
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule())
                .setTimeZone(TimeZone.getTimeZone("Europe/Oslo"))
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS)
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true)
                .enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY)
                .enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    // Hjelpemetode som oversetter resultat (LinkedHashMap) til objektgraf (GraphQLResult). Forenkler testing.
    protected  <T extends GraphQLResult> T konverterTilGraphResponse(Map<String, Object> response, ObjectReader objectReader) throws JsonProcessingException {
        var json = objectMapper.writeValueAsString(response);
        var graphResponse = objectReader.readValue(json);
        return (T) graphResponse;
    }
}
