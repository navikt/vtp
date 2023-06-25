package no.nav.tjeneste.virksomhet.sak.rs;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import no.nav.tjeneste.virksomhet.sak.v1.GsakRepo;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Sak Mock")
@Path("sak/api/v1/saker")
public class SakRestMock {
    private static final Logger LOG = LoggerFactory.getLogger(SakRestMock.class);


    @Context
    private GsakRepo gsakRepo;

    @POST
    @Operation(description = "Opprett sak", responses = {
            @ApiResponse(responseCode = "OK", description = "paaroerende-sykdom-controller", content = @Content(schema = @Schema(implementation = SakJson.class)))
            }, parameters = {
            @Parameter(name = "X-Correlation-ID", required = true, in = ParameterIn.HEADER),
            @Parameter(name = "Authorization", required = true, in = ParameterIn.HEADER)
    })
    public Response opprettSak(
            @Valid @Parameter(description = "Sakem som opprettes", required = true) ObjectNode sak,
            @Context HttpHeaders httpHeaders) {
        Optional<Response> validert = validerIkkeFunksjonelt(httpHeaders);
        if (validert.isPresent()) {
            return validert.get();
        }

        return Response.status(Response.Status.CREATED)
                .entity(gsakRepo.leggTilSak(sak))
                .build();
    }

    @GET
    @Operation(description = "Hent oppgaver",
            responses = {
            @ApiResponse(responseCode = "OK", description = "paaroerende-sykdom-controller", content = @Content(schema = @Schema(implementation = SakJson.class)))
            }, parameters = {
            @Parameter(name = "X-Correlation-ID", required = true, in = ParameterIn.HEADER),
            @Parameter(name = "Authorization", required = true, in = ParameterIn.HEADER),
            @Parameter(name = "tema", in = ParameterIn.QUERY),
            @Parameter(name = "fagsakNr", in = ParameterIn.QUERY),
            @Parameter(name = "applikasjon", in = ParameterIn.QUERY),
            @Parameter(name = "aktoerId", in = ParameterIn.QUERY)
    })
    public Response finnSaker(
            @Context HttpHeaders httpHeaders,
            @Context UriInfo uriInfo) {
        Optional<Response> validert = validerIkkeFunksjonelt(httpHeaders);
        if (validert.isPresent()) {
            return validert.get();
        }

        MultivaluedMap<String, String> queries = uriInfo.getQueryParameters();

        List<ObjectNode> matching = new ArrayList<>();

        gsakRepo.alleSaker().forEach((id, oppgave) -> {
            AtomicInteger matches = new AtomicInteger();
            queries.forEach((queryKey, queryValues) -> {
                if (oppgave.hasNonNull(queryKey) && !queryValues.isEmpty()) {
                    JsonNode oppgaveValue = oppgave.get(queryKey);
                    String firstQueryValue = queryValues.get(0);
                    switch (oppgaveValue.getNodeType()) {
                        case STRING:
                            if (oppgaveValue.asText().equals(firstQueryValue)) matches.getAndIncrement();
                            break;
                        case NUMBER:
                            if (oppgaveValue.asLong() == Long.parseLong(firstQueryValue)) matches.getAndIncrement();
                            break;
                        case BOOLEAN:
                            if (oppgaveValue.asBoolean() == firstQueryValue.equals("true")) matches.getAndIncrement();
                            break;
                        default:
                            throw new IllegalStateException("Ikke støttet NodeType " + oppgaveValue.getNodeType().name());
                    }
                }
            });
            if (matches.get() == queries.size()) matching.add(oppgave);
        });
        if (matching.isEmpty()) {
            var builder = SakJson.getBuilder();
            queries.forEach((queryKey, queryValues) -> {
               if ("fagsakNr".equals(queryKey) && !queryValues.isEmpty()) {
                   builder.medId(Long.parseLong(queryValues.get(0)));
                   builder.medFagsakNr(queryValues.get(0));
               }
            });
            return Response.ok(List.of(builder.build())).build();
        }

        return Response.ok().entity(matching).build();
    }

    @GET
    @Path("/{id}")
    @Operation(description = "Henter sak for en gitt id",
            responses = {
                @ApiResponse(responseCode = "OK", description = "Hentet oppgave", content = @Content(schema = @Schema(implementation  = SakJson.class))),
                @ApiResponse(responseCode = "UNAUTHORIZED", description = "Konsument mangler gyldig token"),
                @ApiResponse(responseCode = "FORBIDDEN", description = "Bruker er ikke autorisert for denne operasjonen"),
                @ApiResponse(responseCode = "NOT_FOUND", description = "Det finnes ingen oppgave for angitt id"),
                @ApiResponse(responseCode = "CONFLICT", description = "Konflikt"),
                @ApiResponse(responseCode = "INTERNAL_SERVER_ERROR", description = "Ukjent feilsituasjon har oppstått i Oppgave")
            }, parameters = {
                @Parameter(name = "X-Correlation-ID", required = true, in = ParameterIn.HEADER),
                @Parameter(name = "Authorization"),
                @Parameter(name = "tema"),
                @Parameter(name = "fagsakNr"),
                @Parameter(name = "applikasjon"),
                @Parameter(name = "aktoerId")
    })
    public Response hentSak(@PathParam("id") Long id, @Context HttpHeaders httpHeaders) {
        Optional<Response> validert = validerIkkeFunksjonelt(httpHeaders);
        if (validert.isPresent()) {
            return validert.get();
        }
        var sak = gsakRepo.hentSak(id);
        return sak != null ? Response.ok(sak).build() : Response.status(Response.Status.BAD_REQUEST).build();
    }

    @SuppressWarnings("resource")
    private Optional<Response> validerIkkeFunksjonelt(HttpHeaders httpHeaders) {
        // Validerer token på rett format
        String jwt = httpHeaders.getHeaderString("Authorization");
        if (jwt == null || !jwt.startsWith("Bearer")) {
            LOG.error("Ugyldig/manglende Authorization header");
            return Optional.of(Response.status(Response.Status.BAD_REQUEST).build());

        }
        // Caliderer at correlation id er satt
        String correlationId = httpHeaders.getHeaderString("X-Correlation-ID");
        if (correlationId == null || correlationId.isBlank()) {
            return Optional.of(Response.status(Response.Status.BAD_GATEWAY).build());
        }

        return Optional.empty();
    }

}
