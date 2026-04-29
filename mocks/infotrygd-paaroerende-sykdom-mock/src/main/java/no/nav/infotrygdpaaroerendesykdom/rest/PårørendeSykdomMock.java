package no.nav.infotrygdpaaroerendesykdom.rest;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import jakarta.enterprise.context.RequestScoped;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/paaroerendeSykdom")
@RequestScoped
public class PårørendeSykdomMock {

    @SuppressWarnings("unused")
    @POST
    @Path("/saker")
    @Produces({"application/json"})
    public Response hentSakUsingPost(PersonRequest personRequest) {
        var result = personRequest.fnr().stream()
                .flatMap(fnr -> Stream.of(new SakResult(List.of(), List.of())))
                .toList();
        return Response.ok(result).build();
    }

    @SuppressWarnings("unused")
    @POST
    @Path("/grunnlag")
    @Produces({"application/json"})
    public Response paaroerendeSykdomUsingPost(PersonRequest personRequest) {
        return Response.ok(List.of()).build();
    }


    @SuppressWarnings("unused")
    @POST
    @Path("/vedtakForPleietrengende")
    @Produces({(MediaType.APPLICATION_JSON)})
    public Response finnVedtakForPleietrengendeUsingPost(PersonRequest personRequest) {
        return Response.ok(List.of()).build();
    }


    @POST
    @Path("/rammevedtak/omsorgspenger")
    @Produces({(MediaType.APPLICATION_JSON)})
    // Finner rammevedtak basert på fødselsnummeret til søker.
    public Response finnRammevedtakForOmsorgspenger(@NotNull PersonRequest request) {
        if (request.fnr.size() != 1){
            throw new IllegalArgumentException("Forventet nøyaktig ett FNR, fikk " + request.fnr.size());
        }
        return Response.ok(List.of()).build();
    }

    public record PersonRequest(@NotNull LocalDate fom, LocalDate tom, @NotNull List<String> fnr, Boolean inkluderHistoriskeIdenter) {
    }
}
