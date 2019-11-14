package no.nav.infotrygdks;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = {"infotrygd-kontantstotte"})
@Path("/infotrygd-kontantstotte/v1/harBarnAktivKontantstotte")
public class InfotrygdKontantstotteMock {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "infotrygd-kontantstotte", notes = (""))
    public Response harBarnAktivKontantstøtte(/*@Context UriInfo uri,
                                              @Context HttpHeaders httpHeaders,
                                              @QueryParam("fnr") String fnr*/) {

        return Response.status(200).entity("{ \"harAktivKontantstotte\": true }").build();
    }
}