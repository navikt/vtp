package no.nav.vtp.personfeed;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.FeedRepositoryImpl;
import no.nav.tjenester.person.feed.common.v1.Feed;

@Api(tags = {"PersonFeed"})
@Path("/api/v2/personfeed/")
public class PersonfeedMock {
    private static final Logger LOG = LoggerFactory.getLogger(PersonfeedMock.class);

    FeedRepositoryImpl feedRepository;

    public PersonfeedMock() {
            feedRepository = FeedRepositoryImpl.getInstance();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "personfeed", notes = "Viser personfeed")
    public Response hentPersonFeed(
            @QueryParam("Nav-Consumer-Id") String consumerId,
            @QueryParam("sequenceId") Integer sequenceId,
            @QueryParam("pageSize") Integer pageSize) {

        LOG.info("Kall mot personfeed fra {} for sekvens {} med pageSize {}", consumerId, sequenceId, pageSize);


        Feed feed = feedRepository.hentFeed(sequenceId,pageSize);
        return Response.status(200).entity(feed).build();

    }

}
