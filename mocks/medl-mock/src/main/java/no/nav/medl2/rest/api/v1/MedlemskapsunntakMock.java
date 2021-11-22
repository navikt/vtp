package no.nav.medl2.rest.api.v1;

import static no.nav.medl2.rest.api.v1.MedlemskapsunntakApiParams.API_OPERATION_MEDLEMSKAPSUNNTAK;
import static no.nav.medl2.rest.api.v1.MedlemskapsunntakApiParams.API_OPERATION_MEDLEMSKAPSUNNTAK_I_PERIODE;
import static no.nav.medl2.rest.api.v1.MedlemskapsunntakApiParams.API_PARAM_INKLUDER_SPORINGSINFO;
import static no.nav.medl2.rest.api.v1.MedlemskapsunntakApiParams.API_PARAM_UNNTAK_ID;
import static no.nav.tjenester.medlemskapsunntak.api.v1.HttpRequestConstants.HEADER_NAV_CALL_ID;
import static no.nav.tjenester.medlemskapsunntak.api.v1.HttpRequestConstants.HEADER_NAV_CONSUMER_ID;
import static no.nav.tjenester.medlemskapsunntak.api.v1.HttpRequestConstants.HEADER_NAV_PERSONIDENT;
import static no.nav.tjenester.medlemskapsunntak.api.v1.HttpRequestConstants.PARAM_EKSKLUDER_KILDER;
import static no.nav.tjenester.medlemskapsunntak.api.v1.HttpRequestConstants.PARAM_FRA_OG_MED;
import static no.nav.tjenester.medlemskapsunntak.api.v1.HttpRequestConstants.PARAM_INKLUDER_SPORINGSINFO;
import static no.nav.tjenester.medlemskapsunntak.api.v1.HttpRequestConstants.PARAM_STATUSER;
import static no.nav.tjenester.medlemskapsunntak.api.v1.HttpRequestConstants.PARAM_TIL_OG_MED;

import java.util.List;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriInfo;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.tjenester.medlemskapsunntak.api.v1.Medlemskapsunntak;

@Path("medl2/api/v1/medlemskapsunntak")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(tags = {"Medlemskapsunntak"})
public class MedlemskapsunntakMock {

    @Context
    private TestscenarioBuilderRepository scenarioRepository;

    @SuppressWarnings("unused")
    @GET
    @Path("/{unntakId}")
    @ApiOperation(API_OPERATION_MEDLEMSKAPSUNNTAK)
    public Medlemskapsunntak hentMedlemskapsunntak(
            @ApiParam(API_PARAM_INKLUDER_SPORINGSINFO) @QueryParam(PARAM_INKLUDER_SPORINGSINFO) Boolean inkluderSporing,
            @ApiParam(value = API_PARAM_UNNTAK_ID, required = true) @NotNull @PathParam("unntakId") Long unntakId) {
        return null;
    }

    @SuppressWarnings("unused")
    @GET
    @ApiOperation(value = API_OPERATION_MEDLEMSKAPSUNNTAK_I_PERIODE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = HEADER_NAV_CALL_ID, required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = HEADER_NAV_CONSUMER_ID, required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = HEADER_NAV_PERSONIDENT, required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "Authorization", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = PARAM_FRA_OG_MED, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = PARAM_TIL_OG_MED, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = PARAM_STATUSER, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = PARAM_EKSKLUDER_KILDER, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = PARAM_INKLUDER_SPORINGSINFO, dataType = "string", paramType = "query")
    })
    public List<Medlemskapsunntak> hentMedlemskapsunntakIPeriode(@Context HttpHeaders httpHeaders,
                                                                 @Context UriInfo uriInfo) {
        String ident = httpHeaders.getHeaderString(HEADER_NAV_PERSONIDENT);
        return new MedlemskapsunntakAdapter(scenarioRepository).finnMedlemsunntak(ident);
    }

}

