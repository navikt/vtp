package no.nav.tjeneste.fpformidling;

import java.util.Collections;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.tjeneste.fpformidling.dto.BehandlingUuidDto;
import no.nav.tjeneste.fpformidling.dto.HentBrevmalerDto;

@Api("/fpformidling")
@Path("/fpformidling")
public class FpFormidlingMock {

    public FpFormidlingMock() {
    }

    @POST
    @Path("/hent-dokumentmaler")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "HentDokumentmalListe", notes = ("Returnerer tilgjengelige dokumentmaler"))
    public Response hentDokumentmalListe(BehandlingUuidDto request) {
        return Response.ok(new HentBrevmalerDto(Collections.emptyList())).build();
    }

}
