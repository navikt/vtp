package no.nav.fpmock2.personfeed;


import java.util.List;

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
import no.nav.foreldrepenger.fpmock2.testmodell.feed.PersonHendelse;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.FeedRepositoryImpl;

@Api(tags = {"PersonFeed"})
@Path("/api/v1/personfeed/")
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


        List<PersonHendelse> hendelser = feedRepository.hentAlleHendelser();
        return Response.status(200).entity(hendelser).build();

    }




}
