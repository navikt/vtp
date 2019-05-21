package no.nav.infotrygdfeed;

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
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.FeedRepositoryImpl;


@Api(tags = {"Infotrygdmock/hendelser"})
@Path("/infotrygd/hendelser")
public class InfotrygdfeedMock {

    FeedRepositoryImpl feedRepository;

    private static final Logger LOG = LoggerFactory.getLogger(InfotrygdfeedMock.class);

    public InfotrygdfeedMock() {
        feedRepository = FeedRepositoryImpl.getInstance();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "hendelse", notes = ("Returnerer hendelser fra Infotrygd"))
    public Response buildHendelse(@Context UriInfo uri,
                                  @Context HttpHeaders httpHeaders,
                                  @QueryParam("aktorId") String aktorId,
                                  @QueryParam("fomDato") String fomDato) {


        LOG.info("Infotrygdhendelse kalt for AktørId {} med FomDato {}", aktorId, fomDato);

        String response = new InfotrygdfeedGenerator().lagInfotrygdFeedResponse(aktorId, fomDato, feedRepository);

        return Response.status(200).entity(response).build();

    }


}
