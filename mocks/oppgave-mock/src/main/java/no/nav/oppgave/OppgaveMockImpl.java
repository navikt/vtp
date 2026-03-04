package no.nav.oppgave;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
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

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("oppgave/api/v1/oppgaver")
public class OppgaveMockImpl {

    private static final Logger LOG = LoggerFactory.getLogger(OppgaveMockImpl.class);

    private static final AtomicLong NUM = new AtomicLong(1000);
    private static final Map<Long, ObjectNode> oppgaver = new ConcurrentHashMap<>();

    @POST
    public Response opprettOppgave(
            @Valid ObjectNode oppgave,
            @Context HttpHeaders httpHeaders) {
        Optional<Response> validert = validerIkkeFunksjonelt(httpHeaders);
        if (validert.isPresent()) {
            return validert.get();
        }

        Long id = NUM.getAndIncrement();
        oppgave.put("id", id);

        oppgaver.put(id, oppgave);

        LOG.info("Opprettet oppgave [{}]: {}", id, oppgave);

        return Response.status(Response.Status.CREATED)
                .entity(oppgave)
                .build();
    }

    @GET
    public Response hentOppgaver(@Context HttpHeaders httpHeaders, @Context UriInfo uriInfo) {
        Optional<Response> validert = validerIkkeFunksjonelt(httpHeaders);
        if (validert.isPresent()) {
            return validert.get();
        }

        MultivaluedMap<String, String> queries = uriInfo.getQueryParameters();

        List<ObjectNode> matching = new ArrayList<>();

        oppgaver.forEach((id, oppgave) -> {
            AtomicInteger matches = new AtomicInteger();
            AtomicInteger possibleMatches = new AtomicInteger();
            queries.forEach((queryKey, queryValues) -> {
                if (oppgave.hasNonNull(queryKey) && !queryValues.isEmpty()) {
                    possibleMatches.getAndIncrement();
                    JsonNode oppgaveValue = oppgave.get(queryKey);
                    for (String queryValue : queryValues) {
                        switch (oppgaveValue.getNodeType()) {
                            case STRING:
                                if (oppgaveValue.asText().equals(queryValue)) matches.getAndIncrement();
                                break;
                            case NUMBER:
                                if (oppgaveValue.asLong() == Long.parseLong(queryValue)) matches.getAndIncrement();
                                break;
                            case BOOLEAN:
                                if (oppgaveValue.asBoolean() == queryValue.equals("true")) matches.getAndIncrement();
                                break;
                            default:
                                throw new IllegalStateException("Ikke støttet NodeType " + oppgaveValue.getNodeType().name());
                        }
                    }
                }
            });
            if (matches.get() == possibleMatches.get()) matching.add(oppgave);
        });

        return Response.ok()
                .entity(new HentOppgaverResponse(matching))
                .build();
    }

    @SuppressWarnings("resource")
    @GET
    @Path("/{id}")
    public Response hentOppgave(@PathParam("id") Long id, @Context HttpHeaders httpHeaders) {
        Optional<Response> validert = validerIkkeFunksjonelt(httpHeaders);
        if (validert.isPresent()) {
            return validert.get();
        }
        LOG.info("Henter oppgave med id: {}", id);
        return oppgaver.get(id) != null ? Response.ok(oppgaver.get(id)).build() : Response.status(Response.Status.BAD_REQUEST).build();
    }

    @PATCH
    @Path("/{id}")
    public Response patchOppgave(@Valid ObjectNode patch,
                                 @PathParam("id") Long id,
                                 @Context UriInfo uriInfo,
                                 @Context HttpHeaders httpHeaders) {
        Optional<Response> validert = validerIkkeFunksjonelt(httpHeaders);
        if (validert.isPresent()) {
            return validert.get();
        }
        LOG.info("Mottatt patch oppgave id {}, request {}", id, patch);

        ObjectNode oppgave = oppgaver.get(id);
        if (oppgave == null)
            return Response.status(Response.Status.BAD_REQUEST).build();

        var nystatus = patch.get("status");
        oppgave.put("status", nystatus.textValue());
        return Response.ok(oppgave).build();
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
