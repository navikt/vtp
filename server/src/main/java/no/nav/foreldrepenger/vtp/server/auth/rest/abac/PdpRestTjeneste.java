package no.nav.foreldrepenger.vtp.server.auth.rest.abac;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "ABAC-PDP-Mock")
@Path("/asm-pdp/authorize")
public class PdpRestTjeneste {
    private static final Logger LOG = LoggerFactory.getLogger(PdpRestTjeneste.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    @POST
    @Produces("application/xacml+json")
    @Operation(description = "asm-pdp/authorize")
    public Response authorize(String entity) throws IOException {
        LOG.debug("Invoke: autorize with entry: {}", entity);
        int permits = getPermits(entity);
        return Response.ok(buildPermitResponse(permits)).build();
    }

    private int getPermits(String entity) throws IOException {
        int permits = 0;
        JsonNode xacmlJson = mapper.reader().readTree(entity);
        // tell antall Request/Resource/Attribute for å bestemme antall responser

        JsonNode request = xacmlJson.get("Request");
        if (request != null && !request.isNull()) {
            JsonNode resource = request.get("Resource");
            if (resource != null && !resource.isNull()) {
                List<JsonNode> values = resource.findValues("Attribute");
                permits = values.size();
            }

        }
        return permits;
    }

    @SuppressWarnings("unused")
    private String buildDenyResponse() {
        return " { \"Response\" : {\"Decision\" : \"Deny\",\"InfotrygdSakStatus\" : {\"StatusCode\" : {\"Value\" : " +
            "\"urn:oasis:names:tc:xacml:1.0:status:ok\",\"StatusCode\" : {\"Value\" : " +
            "\"urn:oasis:names:tc:xacml:1.0:status:ok\"}}}}}";
    }

    private String buildPermitResponse(int antallPermits) {

        // hardkoder en respons, må matche antall decisions som skal gjøres
        String permit = "{\"Decision\" : \"Permit\",\"InfotrygdSakStatus\" : {\"StatusCode\" : {\"Value\" : " +
            "\"urn:oasis:names:tc:xacml:1.0:status:ok\",\"StatusCode\" : {\"Value\" : " +
            "\"urn:oasis:names:tc:xacml:1.0:status:ok\"}}}}";

        if (antallPermits > 1) {
            int genPermits = antallPermits;
            var permitResult = new StringBuilder(permit);
            while (genPermits-- > 1) {
                permitResult.append(", ").append(permit);
            }

            return " { \"Response\" : [" + permitResult + "] }";
        } else {
            return " { \"Response\" : " + permit + "}";
        }

    }
}
