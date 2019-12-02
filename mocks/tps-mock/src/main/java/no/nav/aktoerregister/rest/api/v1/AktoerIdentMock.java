package no.nav.aktoerregister.rest.api.v1;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

import io.swagger.annotations.Api;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.servlet.http.HttpServletResponse;

@Api(tags = {"aktoerregister"})
@Path("/aktoerregister/api/v1/identer")
public class AktoerIdentMock {

    private static final String NAV_IDENTER_HEADER_KEY = "Nav-Personidenter";
    private static final int NAV_IDENTER_MAX_SIZE = 1000;
    private static final String IDENTGRUPPE = "identgruppe";
    private static final String AKTOERID_IDENTGRUPPE = "AktoerId";
    private static final String PERSONIDENT_IDENTGRUPPE = "NorskIdent";
    private static final String GJELDENDE = "gjeldende";

    //TODO (TEAM FAMILIE) Lag mock-responser fra scenario NOSONAR
    private String personIdentMock = "12345678910";
    private String aktørIdMock = "1234567891011";


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, IdentinfoForAktoer> alleIdenterForIdenter(@HeaderParam(NAV_IDENTER_HEADER_KEY) Set<String> requestIdenter,
                                                                 @QueryParam(IDENTGRUPPE) String identgruppe,
                                                                 @QueryParam(GJELDENDE) boolean gjeldende,
                                                                 HttpServletResponse response) {

        response.setHeader("Cache-Control", "no-cache");

        validateRequest(requestIdenter);

        Map<String, IdentinfoForAktoer> resultMap = new HashMap<>();

        Identinfo identinfo;
        if (AKTOERID_IDENTGRUPPE.equals(identgruppe)) {
            identinfo = new Identinfo(personIdentMock, PERSONIDENT_IDENTGRUPPE, true);
        } else {
            identinfo = new Identinfo(aktørIdMock, AKTOERID_IDENTGRUPPE, true);
        }

        //noinspection OptionalGetWithoutIsPresent
        resultMap.put(requestIdenter.stream().findFirst().get(), new IdentinfoForAktoer(List.of(identinfo), null)); //NOSONAR

        return resultMap;
    }

    private void validateRequest(Set<String> identer) {
        if (identer.isEmpty()) {
            throw new IllegalArgumentException("Ville kastet \"MissingIdenterException\"");
        } else if (identer.size() > NAV_IDENTER_MAX_SIZE) {
            throw new IllegalArgumentException("Ville kastet \"RequestIdenterMaxSizeException\"");
        }
    }
}
