package no.nav.foreldrepenger.vtp.testmodell.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonWrapper {

    private final ObjectMapper mapper;

    public JacksonWrapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public String writeValueAsString(Object object) {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Kunne ikke serialisere fra " + e);
        }
    }
}
