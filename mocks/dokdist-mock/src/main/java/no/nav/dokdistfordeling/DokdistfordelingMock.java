package no.nav.dokdistfordeling;


import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Api(tags = {"Dokdist"})
@Path("dokdist/v1/distribuerjournalpost")
public class DokdistfordelingMock {
    private static final Logger LOG = LoggerFactory.getLogger(DokdistfordelingMock.class);

    @POST
    public Response lagJournalpost(String request) {
        LOG.info("Distribuer dokument request: [{}]", request);

        return Response.status(200).entity("{ \"bestillingsId\": \""+ UUID.randomUUID().toString() +"\"}").build();
    }

}
