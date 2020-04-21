package no.nav.medl2.rest.api.v1;

import java.util.List;
import java.util.Set;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Providers;

import no.nav.tjenester.medlemskapsunntak.api.v1.Medlemskapsunntak;

import static no.nav.medl2.rest.api.v1.MedlemskapsunntakApiParams.*;
import static no.nav.tjenester.medlemskapsunntak.api.v1.HttpRequestConstants.HEADER_NAV_PERSONIDENT;
import static no.nav.tjenester.medlemskapsunntak.api.v1.HttpRequestConstants.PARAM_EKSKLUDER_KILDER;
import static no.nav.tjenester.medlemskapsunntak.api.v1.HttpRequestConstants.PARAM_FRA_OG_MED;
import static no.nav.tjenester.medlemskapsunntak.api.v1.HttpRequestConstants.PARAM_INKLUDER_SPORINGSINFO;
import static no.nav.tjenester.medlemskapsunntak.api.v1.HttpRequestConstants.PARAM_STATUSER;
import static no.nav.tjenester.medlemskapsunntak.api.v1.HttpRequestConstants.PARAM_TIL_OG_MED;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Path("medl2/api/v1/medlemskapsunntak")
@Produces(MediaType.APPLICATION_JSON)
@Api(tags = {"Medlemskapsunntak"})
public class MedlemskapsunntakMock {

    @Context
    private Providers providers;

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
    @ApiOperation(API_OPERATION_MEDLEMSKAPSUNNTAK_I_PERIODE)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<Medlemskapsunntak> hentMedlemskapsunntakIPeriode(
            @ApiParam(API_PARAM_STATUSER) @QueryParam(PARAM_STATUSER) Set<String> statuser,
            @ApiParam(API_PARAM_FRA_OG_MED) @QueryParam(PARAM_FRA_OG_MED) Dato fraOgMed,
            @ApiParam(API_PARAM_TIL_OG_MED) @QueryParam(PARAM_TIL_OG_MED) Dato tilOgMed,
            @ApiParam(API_PARAM_INKLUDER_SPORINGSINFO_PERSON) @QueryParam(PARAM_INKLUDER_SPORINGSINFO) boolean sporing,
            @ApiParam(API_PARAM_EKSKLUDER_KILDER) @QueryParam(PARAM_EKSKLUDER_KILDER) Set<String> ekskluder,
            @ApiParam(value = API_PARAM_PERSONIDENT, required = true) @NotNull @QueryParam(HEADER_NAV_PERSONIDENT) String ident) {

        return List.of();
    }

    private static class Dato {
        private static final ObjectMapper objectMapper = new ObjectMapper();
        static {
            objectMapper.registerModule(new Jdk8Module());
            objectMapper.registerModule(new JavaTimeModule());
        }

    }
}

