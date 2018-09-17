package no.nav.sigrun;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = { "Sigrun/beregnetskatt" })
@Path("/api/beregnetskatt")
public class SigrunMock {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "beregnetskatt", notes = ("Returnerer beregnetskatt fra Sigrun"))
    public String buildPermitResponse() {
        return "[\n" +
            "  {\n" +
            "    \"tekniskNavn\": \"personinntektFiskeFangstFamiliebarnehage\",\n" +
            "    \"verdi\": \"50000\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"tekniskNavn\": \"personinntektNaering\",\n" +
            "    \"verdi\": \"50000\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"tekniskNavn\": \"personinntektBarePensjonsdel\",\n" +
            "    \"verdi\": \"50000\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"tekniskNavn\": \"svalbardLoennLoennstrekkordningen\",\n" +
            "    \"verdi\": \"5000\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"tekniskNavn\": \"personinntektLoenn\",\n" +
            "    \"verdi\": \"50000\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"tekniskNavn\": \"svalbardPersoninntektNaering\",\n" +
            "    \"verdi\": \"50000\"\n" +
            "  }\n" +
            "]";
    }
}
