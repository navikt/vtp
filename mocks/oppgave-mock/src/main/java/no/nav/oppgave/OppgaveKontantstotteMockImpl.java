package no.nav.oppgave;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.container.ContainerRequestContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.*;


@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("oppgave-kontantstotte/api/v1/oppgaver")
public class OppgaveKontantstotteMockImpl {
    private static final Logger LOG = LoggerFactory.getLogger(OppgaveKontantstotteMockImpl.class);

    //TODO (TEAM FAMILIE) Lag mock-responser fra scenario NOSONAR
    private Oppgave.Builder oppgaveMock = new Oppgave.Builder()
            .medTema("KON")
            .medAktivDato(LocalDate.now())
            .medVersjon(1)
            .medStatus(Oppgavestatus.OPPRETTET)
            .medPrioritet(Prioritet.NORM)
            .medOppgavetype("BEH_SAK");

    @SuppressWarnings("unused")
    @GET
    @Path("/{id}")
    @ApiOperation(value = "Henter oppgave for en gitt id", response = OppgaveJson.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "X-Correlation-ID", required = true, dataType = "string", paramType = "header")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Konsument mangler gyldig token"),
            @ApiResponse(code = 403, message = "Bruker er ikke autorisert for denne operasjonen"),
            @ApiResponse(code = 404, message = "Det finnes ingen oppgave for angitt id"),
            @ApiResponse(code = 409, message = "Konflikt"),
            @ApiResponse(code = 500, message = "Ukjent feilsituasjon har oppstått i Oppgave")
    })
    public Response hentOppgave(@PathParam("id") Long id, @Context ContainerRequestContext ctx) {
        return Response.ok(new OppgaveJson(oppgaveMock.medId(id).build())).build();
    }


    @SuppressWarnings("unused")
    @GET
    @ApiOperation(value = "Søk etter oppgaver", response = FinnOppgaveResponse.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "X-Correlation-ID", required = true, dataType = "string", paramType = "header")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Ugyldig input"),
            @ApiResponse(code = 401, message = "Konsument mangler gyldig token"),
            @ApiResponse(code = 403, message = "Bruker er ikke autorisert for denne operasjonen"),
            @ApiResponse(code = 500, message = "Ukjent feilsituasjon har oppstått i Oppgave")
    })
    public Response finnOppgaver(@Valid @BeanParam OppgaveSearchRequest searchRequest, @Context ContainerRequestContext ctx) {
        LOG.info("Søker etter oppgaver for: {}", searchRequest);

        return Response.ok()
                .entity(new FinnOppgaveResponse(
                        List.of(new OppgaveJson(oppgaveMock.medId(123456789L).build())),
                        1))
                        .build();
    }


    @SuppressWarnings("unused")
    @PUT
    @Path("/{id}")
    @ApiOperation(value = "Endrer en eksisterende oppgave", response = OppgaveJson.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "X-Correlation-ID", required = true, dataType = "string", paramType = "header")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Oppgave endret", responseHeaders = @ResponseHeader(name = "location", description = "Angir URI til den endrede oppgaven")),
            @ApiResponse(code = 400, message = "Ugyldig input"),
            @ApiResponse(code = 401, message = "Konsument mangler gyldig token"),
            @ApiResponse(code = 403, message = "Bruker er ikke autorisert for denne operasjonen"),
            @ApiResponse(code = 409, message = "Konflikt"),
            @ApiResponse(code = 500, message = "Ukjent feilsituasjon har oppstått i Oppgave")
    })
    public Response endreOppgave(@Valid @ApiParam(value = "Oppgaven som skal endres", required = true) OppgaveJson oppgaveJson,
                                 @Context UriInfo uriInfo,
                                 @PathParam("id") Long id,
                                 @QueryParam("brukerId") @ApiParam(hidden = true) String brukerId,
                                 @Context ContainerRequestContext ctx) {

        LOG.info("Mottatt endringsrequest: {}", oppgaveJson);
        return Response.ok().entity(oppgaveJson).build();
    }
}
