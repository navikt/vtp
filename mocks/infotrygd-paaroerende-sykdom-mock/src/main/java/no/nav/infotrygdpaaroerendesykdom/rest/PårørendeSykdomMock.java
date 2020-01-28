package no.nav.infotrygdpaaroerendesykdom.rest;

import io.swagger.annotations.*;
import no.nav.infotrygdpaaroerendesykdom.generated.model.PaaroerendeSykdom;
import no.nav.infotrygdpaaroerendesykdom.generated.model.SakResult;

import javax.enterprise.context.RequestScoped;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.List;

@Path("/paaroerendeSykdom")
@RequestScoped
@Api(description = "the paaroerendeSykdom API")
public class PårørendeSykdomMock {

    // todo: hent data fra modell
    // todo: sjekk Autorization-token

    @GET
    @Path("/sak")
    @Produces({ "application/json" })
    @ApiOperation(value = "hentSak", notes = "", response = SakResult.class, authorizations = {
            @Authorization(value = "JWT")
    }, tags={ "paaroerende-sykdom-controller",  })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = SakResult.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
            @ApiResponse(code = 404, message = "Not Found", response = Void.class) })
    public Response hentSakUsingGET( @NotNull @ApiParam(value = "fnr",required=true)  @QueryParam("fnr") String fnr,  @NotNull @ApiParam(value = "fom",required=true)  @QueryParam("fom") LocalDate fom,  @ApiParam(value = "tom")  @QueryParam("tom") LocalDate tom) {
        SakResult result = new SakResult();
        return Response.ok(result).build();
    }

    @GET
    @Path("/grunnlag")
    @Produces({ "application/json" })
    @ApiOperation(value = "paaroerendeSykdom", notes = "", response = PaaroerendeSykdom.class, responseContainer = "List", authorizations = {
            @Authorization(value = "JWT")
    }, tags={ "paaroerende-sykdom-controller" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = PaaroerendeSykdom.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
            @ApiResponse(code = 404, message = "Not Found", response = Void.class) })
    public Response paaroerendeSykdomUsingGET1( @NotNull @ApiParam(value = "fnr",required=true)  @QueryParam("fnr") String fnr,  @NotNull @ApiParam(value = "fom",required=true)  @QueryParam("fom") LocalDate fom,  @ApiParam(value = "tom")  @QueryParam("tom") LocalDate tom) {
        PaaroerendeSykdom p = new PaaroerendeSykdom();
        List<PaaroerendeSykdom> result = List.of(p);
        return Response.ok(result).build();
    }
}
