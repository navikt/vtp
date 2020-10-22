package no.nav.oppgave;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(tags = "Oppgave Mock")
@Path("oppgave/api/v1/oppgaver")
public class OppgaveMockImpl {

    private static final Logger LOG = LoggerFactory.getLogger(OppgaveMockImpl.class);

    private static final AtomicLong NUM = new AtomicLong(1000);
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

        Long id = NUM.getAndIncrement();
        oppgave.put("id", id);

        oppgaver.put(id, oppgave);

        LOG.info("Opprettet oppgave [{}]: {}", id, oppgave);

        return Response.status(Response.Status.CREATED)
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

    @SuppressWarnings("resource")
    @GET
    @Path("/{id}")
    @ApiOperation(value = "Henter oppgave for en gitt id", response = OppgaveJson.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "X-Correlation-ID", required = true, dataType = "string", paramType = "header")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Konsument mangler gyldig token"),
            @ApiResponse(code = 403, message = "Bruker er ikke autorisert for denne operasjonen"),
            @ApiResponse(code = 404, message = "Det finnes ingen oppgave for angitt id"),
            @ApiResponse(code = 409, message = "Konflikt"),
            @ApiResponse(code = 500, message = "Ukjent feilsituasjon har oppstått i Oppgave")
    }
    )
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
    @ApiOperation(value = "Endrer en eksisterende oppgave", response = OppgaveJson.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "X-Correlation-ID", required = true, dataType = "string", paramType = "header")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Oppgave patchet", responseHeaders = @ResponseHeader(name = "location", description = "Angir URI til den patchede oppgaven")),
            @ApiResponse(code = 401, message = "Konsument mangler gyldig token"),
            @ApiResponse(code = 403, message = "Bruker er ikke autorisert for denne operasjonen"),
            @ApiResponse(code = 409, message = "Konflikt"),
            @ApiResponse(code = 500, message = "Ukjent feilsituasjon har oppstått i Oppgave")
    }
    )
    public Response patchOppgave(@Valid @ApiParam(value = "Oppgaven som endres", required = true) ObjectNode patch,
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
