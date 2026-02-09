package no.nav.pdl;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.kobylynskyi.graphql.codegen.model.graphql.GraphQLResult;

import no.nav.foreldrepenger.vtp.testmodell.util.JacksonObjectMapperTestscenario;

public abstract class PdlTestBase {

    protected static final JsonMapper JSON_MAPPER = JacksonObjectMapperTestscenario.getJsonMapper();

    // Hjelpemetode som oversetter resultat (LinkedHashMap) til objektgraf (GraphQLResult). Forenkler testing.
    @SuppressWarnings({"unchecked", "rawtypes"})
    protected  <T extends GraphQLResult> T konverterTilGraphResponse(Map<String, Object> response, ObjectReader objectReader) throws JsonProcessingException {
        var json = JSON_MAPPER.writeValueAsString(response);
        var graphResponse = objectReader.readValue(json);
        return (T) graphResponse;
    }
}
