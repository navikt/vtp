package no.nav.medl2.rest.api.v1;

import static no.nav.medl2.rest.api.v1.MedlemskapsunntakApiParams.API_OPERATION_MEDLEMSKAPSUNNTAK;
import static no.nav.medl2.rest.api.v1.MedlemskapsunntakApiParams.API_OPERATION_MEDLEMSKAPSUNNTAK_I_PERIODE;
import static no.nav.medl2.rest.api.v1.MedlemskapsunntakApiParams.API_OPERATION_MEDLEMSKAP_PERIODER;
import static no.nav.medl2.rest.api.v1.MedlemskapsunntakApiParams.API_PARAM_INKLUDER_SPORINGSINFO;
import static no.nav.medl2.rest.api.v1.MedlemskapsunntakApiParams.API_PARAM_UNNTAK_ID;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriInfo;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;

@Path("medl2/api/v1")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Medlemskapsunntak")
public class MedlemskapsunntakMock {

    private static final String HEADER_NAV_CONSUMER_ID = "Nav-Consumer-Id";
    private static final String HEADER_NAV_CALL_ID = "Nav-Call-Id";
    private static final String HEADER_NAV_PERSONIDENT = "Nav-Personident";
    private static final String PARAM_FRA_OG_MED = "fraOgMed";
    private static final String PARAM_TIL_OG_MED = "tilOgMed";
    private static final String PARAM_STATUSER = "statuser";
    private static final String PARAM_EKSKLUDER_KILDER = "ekskluderKilder";
    private static final String PARAM_INKLUDER_SPORINGSINFO = "inkluderSporingsinfo";

    @Context
    private TestscenarioBuilderRepository scenarioRepository;

    @SuppressWarnings("unused")
    @GET
    @Path("/medlemskapsunntak/{unntakId}")
    @Operation(description = API_OPERATION_MEDLEMSKAPSUNNTAK)
    public Medlemskapsunntak hentMedlemskapsunntak(
            @Parameter(name = API_PARAM_INKLUDER_SPORINGSINFO) @QueryParam(PARAM_INKLUDER_SPORINGSINFO) Boolean inkluderSporing,
            @Parameter(name = API_PARAM_UNNTAK_ID, required = true) @NotNull @PathParam("unntakId") Long unntakId) {
        return null;
    }

    @SuppressWarnings("unused")
    @GET
    @Path("/medlemskapsunntak")
    @Operation(description = API_OPERATION_MEDLEMSKAPSUNNTAK_I_PERIODE, parameters = {
        @Parameter(name = HEADER_NAV_CALL_ID, required = true, in = ParameterIn.HEADER),
        @Parameter(name = HEADER_NAV_CONSUMER_ID, required = true, in = ParameterIn.HEADER),
        @Parameter(name = HEADER_NAV_PERSONIDENT, required = true, in = ParameterIn.HEADER),
        @Parameter(name = "Authorization", required = true, in = ParameterIn.HEADER),
        @Parameter(name = PARAM_FRA_OG_MED, in = ParameterIn.HEADER),
        @Parameter(name = PARAM_TIL_OG_MED, in = ParameterIn.HEADER),
        @Parameter(name = PARAM_STATUSER, in = ParameterIn.HEADER),
        @Parameter(name = PARAM_EKSKLUDER_KILDER, in = ParameterIn.HEADER),
        @Parameter(name = PARAM_INKLUDER_SPORINGSINFO, in = ParameterIn.HEADER)
    })
    public List<Medlemskapsunntak> hentMedlemskapsunntakIPeriode(@Context HttpHeaders httpHeaders,
                                                                 @Context UriInfo uriInfo) {
        String ident = httpHeaders.getHeaderString(HEADER_NAV_PERSONIDENT);
        return new MedlemskapsunntakAdapter(scenarioRepository).finnMedlemsunntak(ident);
    }

    @POST
    @Path("/periode/soek")
    @Operation(description = API_OPERATION_MEDLEMSKAP_PERIODER)
    public List<Medlemskapsunntak> hentMedlemsperioder(@NotNull MedlemRequest request) {
        return new MedlemskapsunntakAdapter(scenarioRepository).finnMedlemsunntak(request.personident());
    }

    public record MedlemRequest(@NotNull String personident) { }

}

