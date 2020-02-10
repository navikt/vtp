package no.nav.oppgave;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(tags = "Oppgave Mock")
@Path("api/v1/oppgaver")
public class OppgaveMockImpl {

    private static final Logger LOG = LoggerFactory.getLogger(OppgaveMockImpl.class);

    private static final Map<Long, ObjectNode> oppgaver = new ConcurrentHashMap<>();

    @POST
    @ApiOperation(value = "Opprett oppgave", response = OppgaveJson.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-Correlation-ID", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "Authorization", required = true, dataType = "string", paramType = "header")

    })
    public Response opprettOppgave(
            @Valid @ApiParam(value = "Oppgaven som opprettes", required = true) ObjectNode oppgave,
            @Context HttpHeaders httpHeaders) {
        Optional<Response> validert = validerIkkeFunksjonelt(httpHeaders);
        if (validert.isPresent()) {
            return validert.get();
        }

        Long id = (long) (oppgaver.size() + 1);
        oppgave.put("id", id);

        oppgaver.put(id, oppgave);

        LOG.info("Opprettet oppgave: {}", oppgave);

        return Response.ok()
                .entity(oppgave)
                .build();
    }

    @GET
    @ApiOperation(value = "Hent oppgaver", response = HentOppgaverResponse.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-Correlation-ID", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "Authorization", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "tema", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "oppgavetype", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "journalpostId", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "aktoerId", dataType = "string", paramType = "query")
    })
    public Response hentOppgaver(
            @Context HttpHeaders httpHeaders,
            @Context UriInfo uriInfo) {
        Optional<Response> validert = validerIkkeFunksjonelt(httpHeaders);
        if (validert.isPresent()) {
            return validert.get();
        }

        MultivaluedMap<String, String> queries = uriInfo.getQueryParameters();

        List<ObjectNode> matching = new ArrayList<>();

        oppgaver.forEach((id, oppgave) -> {
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

        return Response.ok()
                .entity(new HentOppgaverResponse(matching))
                .build();
    }

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
