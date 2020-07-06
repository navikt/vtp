package no.nav.tjeneste.virksomhet.arbeidsfordeling.rest;

import static java.util.Collections.singletonList;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import io.swagger.annotations.ApiParam;

import no.nav.norg2.model.Norg2RsEnhet;

@Path("/norg2/api/v1/enhet")
public class EnhetRestMock {
    @GET
    @Produces({ "application/json;charset=UTF-8" })
    @io.swagger.annotations.ApiOperation(value = "Returnerer en filtrert liste av alle enheter", response = Norg2RsEnhet.class, responseContainer = "List", tags={ "enhet", })
    @io.swagger.annotations.ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = Norg2RsEnhet.class, responseContainer = "List"),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad request"),
            @io.swagger.annotations.ApiResponse(code = 404, message = "Not found"),
            @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })
    public Response getAllEnheterUsingGET(@ApiParam(value = "Enhetsstatus resulterende enheter skal filtreres på", allowableValues="UNDER_ETABLERING, AKTIV, UNDER_AVVIKLING, NEDLAGT") @QueryParam("enhetStatusListe") List<String> enhetStatusListe
            ,@ApiParam(value = "Enhetsnumre for filtrering av enheter") @QueryParam("enhetsnummerListe") List<String> enhetsnummerListe
            ,@ApiParam(value = "Hvorvidt enheter skal være oppgavebehandlende", allowableValues="UFILTRERT, KUN_OPPGAVEBEHANDLERE, INGEN_OPPGAVEBEHANDLERE") @QueryParam("oppgavebehandlerFilter") String oppgavebehandlerFilter
            ,@Context SecurityContext securityContext)
            throws NotFoundException {

        return Response.ok(singletonList(enEnhet())).build();
    }

    @GET
    @Path("/navkontor/{geografiskOmraade}")
    @Produces({ "application/json;charset=UTF-8" })
    @io.swagger.annotations.ApiOperation(value = "Returnerer en NAV kontor som er en lokalkontor for spesifisert geografisk område", response = Norg2RsEnhet.class, tags={ "enhet", })
    @io.swagger.annotations.ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = Norg2RsEnhet.class),
            @io.swagger.annotations.ApiResponse(code = 404, message = "Not Found"),
            @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })
    public Response getEnhetByGeografiskOmraadeUsingGET(@ApiParam(value = "Geografisk identifikator for NAV kontoret",required=true) @PathParam("geografiskOmraade") String geografiskOmraade
            ,@ApiParam(value = "Diskresjonskode på saker et NAV kontor kan behandle", allowableValues="SPFO, SPSF, ANY") @QueryParam("disk") String disk
            ,@Context SecurityContext securityContext)
            throws NotFoundException {

        return Response.ok(enEnhet()).build();
    }

    private Norg2RsEnhet enEnhet() {
        Norg2RsEnhet norg2RsEnhet = new Norg2RsEnhet();
        norg2RsEnhet.setEnhetId(4407L);
        norg2RsEnhet.setNavn("NAV Arbeid og ytelser Tønsberg");
        norg2RsEnhet.setOppgavebehandler(true);
        return norg2RsEnhet;
    }
}
