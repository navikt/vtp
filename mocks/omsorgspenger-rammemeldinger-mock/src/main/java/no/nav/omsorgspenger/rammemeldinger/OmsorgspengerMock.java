package no.nav.omsorgspenger.rammemeldinger;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Api(tags = {"Omsorgspenger/AleneOmOmsorgen"})
@Path("/omsorgspenger")
public class OmsorgspengerMock {

    @SuppressWarnings("unused")
    @POST
    @Path("/hentAleneOmOmsorgen")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "foreldrepenger", notes = ("Returnerer foreldrepenger fra Infotrygd"))
    public AleneOmOmsorgenResponse aleneOmOmsorgen(RammemeldingRequest request) {
        return new AleneOmOmsorgenResponse();
    }

    @SuppressWarnings("unused")
    @POST
    @Path("/hentOverfoeringer")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "foreldrepenger", notes = ("Returnerer foreldrepenger fra Infotrygd"))
    public OverføringerResponse overføringer(RammemeldingRequest request) {
        return new OverføringerResponse();
    }
}
