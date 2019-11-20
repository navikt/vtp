package no.nav.infotrygdks;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = {"infotrygd-kontantstotte"})
@Path("/infotrygd-kontantstotte/v1/harBarnAktivKontantstotte")
public class InfotrygdKontantstotteMock {
    private static final Logger LOG = LoggerFactory.getLogger(InfotrygdKontantstotteMock.class);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "infotrygd-kontantstotte", notes = (""))
    public Response harBarnAktivKontantstøtte(@HeaderParam("fnr") String fnr) {
        LOG.info("infotrygd-kontantstotte. fnr: {}", fnr);

        return Response.status(200).entity("{ \"harAktivKontantstotte\": false }").build();
    }
}