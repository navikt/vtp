package no.nav.tps.proxy.api.v1.innsyn;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.swagger.annotations.Api;
import no.nav.tps.proxy.api.v1.innsyn.dto.Personinfo;
import no.nav.tps.proxy.api.v1.innsyn.dto.Relasjon;

@Path("/api/v1/innsyn")
@Produces(MediaType.APPLICATION_JSON)
@Api(tags = {"innsyn-controller"})
public class InnsynMock {

    @SuppressWarnings("unused")
    @GET
    @Path("/person")
    public Personinfo hentPersoninfoForIdent(@NotNull @HeaderParam("Authorization") String authToken,
                                             @NotNull @HeaderParam("Nav-Call-Id") String callId,
                                             @NotNull @HeaderParam("Nav-Consumer-Id") String consumerId,
                                             @NotNull @HeaderParam("Nav-Personident") String personident) {

        return Personinfo.builder().build();
    }

    @SuppressWarnings("unused")
    @GET
    @Path("/relasjon")
    public List<Relasjon> hentRelasjonsinfoForIdent(@NotNull @HeaderParam("Authorization") String authToken,
                                                    @NotNull @HeaderParam("Nav-Call-Id") String callId,
                                                    @NotNull @HeaderParam("Nav-Consumer-Id") String consumerId,
                                                    @NotNull @HeaderParam("Nav-Personident") String personident) {

        return List.of(Relasjon.builder().build());
    }

    @SuppressWarnings("unused")
    @GET
    @Path("/barn")
    public List<Relasjon> hentBarneListeForIdent(@NotNull @HeaderParam("Authorization") String authToken,
                                                    @NotNull @HeaderParam("Nav-Call-Id") String callId,
                                                    @NotNull @HeaderParam("Nav-Consumer-Id") String consumerId,
                                                    @NotNull @HeaderParam("Nav-Personident") String personident) {

        return List.of(Relasjon.builder().build());
    }

}
