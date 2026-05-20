package no.nav.pdl;

import java.util.Map;

import no.nav.foreldrepenger.graphql.GraphQLResult;
import no.nav.foreldrepenger.util.JacksonObjectMapperTestscenario;
import tools.jackson.databind.ObjectReader;
import tools.jackson.databind.json.JsonMapper;

public abstract class PdlTestBase {

    protected static final JsonMapper JSON_MAPPER = JacksonObjectMapperTestscenario.getJsonMapper();

    // Hjelpemetode som oversetter resultat (LinkedHashMap) til objektgraf (GraphQLResult). Forenkler testing.
    @SuppressWarnings({"unchecked", "rawtypes"})
    protected  <T extends GraphQLResult> T konverterTilGraphResponse(Map<String, Object> response, ObjectReader objectReader)  {
        var json = JSON_MAPPER.writeValueAsString(response);
        var graphResponse = objectReader.readValue(json);
        return (T) graphResponse;
    }
}
