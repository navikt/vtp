package no.nav.dokdistfordeling;

import java.util.UUID;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import no.nav.dokdistfordeling.generated.model.DistribuerJournalpostRequestToModel;
import no.nav.dokdistfordeling.generated.model.DistribuerJournalpostResponseToModel;

@Tag(name = "Dokdist")
@Path("dokdist/v1/distribuerjournalpost")
public class DokdistfordelingMock {
    private static final Logger LOG = LoggerFactory.getLogger(DokdistfordelingMock.class);

    @POST
    @Operation(description = "distribuer journalpost")
    public Response distribuerjournalpost(DistribuerJournalpostRequestToModel request) {
        LOG.info("Distribuer journalpost med jouralpostId {} for fagsystem {}", request.getJournalpostId(), request.getBestillendeFagsystem());

        DistribuerJournalpostResponseToModel response = new DistribuerJournalpostResponseToModel();
        response.setBestillingsId(UUID.randomUUID().toString());

        return Response.status(200).entity(response).build();
    }

}
