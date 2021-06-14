package no.nav.dokdistfordeling;

import java.util.UUID;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.dokdistfordeling.generated.model.DistribuerJournalpostRequestToModel;
import no.nav.dokdistfordeling.generated.model.DistribuerJournalpostResponseToModel;

@Api(tags = {"Dokdist"})
@Path("dokdist/v1/distribuerjournalpost")
public class DokdistfordelingMock {
    private static final Logger LOG = LoggerFactory.getLogger(DokdistfordelingMock.class);

    @POST
    @ApiOperation(value = "distribuer journalpost")
    public Response distribuerjournalpost(DistribuerJournalpostRequestToModel request) {
        LOG.info("Distribuer journalpost request: [{}]", request);

        DistribuerJournalpostResponseToModel response = new DistribuerJournalpostResponseToModel();
        response.setBestillingsId(UUID.randomUUID().toString());

        return Response.status(200).entity(response).build();
    }

}
