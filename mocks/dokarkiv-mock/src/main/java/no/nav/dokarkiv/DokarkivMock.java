package no.nav.dokarkiv;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = {"Dokarkiv"})
@Path("/dokarkiv/rest/journalpostapi/v1")
public class DokarkivMock {
    private static final Logger LOG = LoggerFactory.getLogger(DokarkivMock.class);

    @POST
    @Path("/journalpost")
    @ApiOperation(value = "lag journalpost", notes = (""))
    public Response lagJournalpost(@QueryParam("foersoekFerdigstill") Boolean foersoekFerdigstill) {
        LOG.info("Dokarkiv. Lag journalpost. foersoekFerdigstill: {}", foersoekFerdigstill);

        return Response.status(200).entity("{ \"journalpostId\": 1, \"melding\": \"foo\", \"journalpostferdigstilt\": false, \"dokumenter\": [ { \"dokumentInfoId\": \"12345\"} ] }").build();
    }
}
