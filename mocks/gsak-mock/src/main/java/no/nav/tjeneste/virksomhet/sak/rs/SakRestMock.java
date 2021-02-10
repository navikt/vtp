package no.nav.tjeneste.virksomhet.sak.rs;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
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
import no.nav.tjeneste.virksomhet.sak.v1.GsakRepo;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(tags = "Sak Mock")
@Path("sak/api/v1/saker")
public class SakRestMock {
    private static final Logger LOG = LoggerFactory.getLogger(SakRestMock.class);


    @Context
    private GsakRepo gsakRepo;

    @POST
    @ApiOperation(value = "Opprett sak", response = SakJson.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-Correlation-ID", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "Authorization", required = true, dataType = "string", paramType = "header")

    })
    public Response opprettSak(
            @Valid @ApiParam(value = "Sakem som opprettes", required = true) ObjectNode sak,
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
    @ApiOperation(value = "Hent oppgaver", response = SakJson.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-Correlation-ID", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "Authorization", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "tema", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "fagsakNr", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "applikasjon", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "aktoerId", dataType = "string", paramType = "query")
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
    @ApiOperation(value = "Henter sak for en gitt id", response = SakJson.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "X-Correlation-ID", required = true, dataType = "string", paramType = "header")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Konsument mangler gyldig token"),
            @ApiResponse(code = 403, message = "Bruker er ikke autorisert for denne operasjonen"),
            @ApiResponse(code = 404, message = "Det finnes ingen oppgave for angitt id"),
            @ApiResponse(code = 409, message = "Konflikt"),
            @ApiResponse(code = 500, message = "Ukjent feilsituasjon har oppstått i Oppgave")
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
