package no.nav.foreldrepenger.fpwsproxy.oppdrag;

import static no.nav.foreldrepenger.fpwsproxy.oppdrag.OppdragskontrollTilBeregingMapper.tilBeregningDto;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.ApiOperation;
import no.nav.foreldrepenger.kontrakter.fpwsproxy.simulering.request.OppdragskontrollDto;
import no.nav.foreldrepenger.kontrakter.fpwsproxy.simulering.respons.BeregningDto;

@Path("/api/fpwsproxy/simulering")
public class FpWsProxySimuleringOppdragMock {
    private static final Logger LOG = LoggerFactory.getLogger(FpWsProxySimuleringOppdragMock.class);

    @POST
    @Path("/start")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "", notes = "Simulering av oppdragskontroll via fpwsproxy", response = BeregningDto[].class)
    public Response simulerBereging(@Valid OppdragskontrollDto oppdragskontrollDto,
                                    @QueryParam("uten_inntrekk") @DefaultValue("false") boolean utenInntrekk,
                                    @QueryParam("ytelse_type") YtelseType ytelseType) {
        var tekstMedUtenInntrekk = utenInntrekk ? "uten inntrekk for" + ytelseType : "med inntrekk";
        LOG.info("Utfører simulering {} for følgende oppdrag {}", tekstMedUtenInntrekk, oppdragskontrollDto.behandlingId());
        var simuleringsresultat = tilBeregningDto(oppdragskontrollDto, utenInntrekk, ytelseType);
        LOG.info("Simulering utført for {}", oppdragskontrollDto.behandlingId());
        return Response.ok(simuleringsresultat).build();
    }

}
