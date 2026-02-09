package no.nav.dokdistfordeling;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Tag(name = "Dokdist")
@Path("dokdist/v1/distribuerjournalpost")
public class DokdistfordelingMock {
    private static final Logger LOG = LoggerFactory.getLogger(DokdistfordelingMock.class);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "distribuer journalpost")
    public Response distribuerjournalpost(DistribuerJournalpostRequest request) {
        LOG.info("Distribuer journalpost med jouralpostId {} for fagsystem {}", request.journalpostId(), request.bestillendeFagsystem());

        var response = new DistribuerJournalpostResponse(UUID.randomUUID().toString());
        return Response.status(200).entity(response).build();
    }

    public enum Distribusjonstidspunkt { UMIDDELBART, KJERNETID }

    public enum Distribusjonstype { VEDTAK, VIKTIG, ANNET }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record DistribuerJournalpostRequest(@NotNull String journalpostId, String batchId,
                                               @NotNull String bestillendeFagsystem, @NotNull String dokumentProdApp,
                                               @NotNull Distribusjonstype distribusjonstype,
                                               @NotNull Distribusjonstidspunkt distribusjonstidspunkt) {
        // Tar ikke med felter for adresse, overstyring eller metadata - ikke i bruk blant våre brevbestillere.
        // Kan utvide med adresse dersom aktuelt med brev til samhandler
    }

    public record DistribuerJournalpostResponse(String bestillingsId) { }

}
